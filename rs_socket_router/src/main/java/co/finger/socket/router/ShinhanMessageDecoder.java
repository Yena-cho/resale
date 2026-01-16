package co.finger.socket.router;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 신한에서 온 요청을 처리하는 컨트롤러
 *
 */
public class ShinhanMessageDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShinhanMessageDecoder.class);

    public ShinhanMessageDecoder() {

    }

    // 인풋 타임아웃 30초
    private long inputTimeout = 30000;

    private Timer timeoutTimer = null;

    private void resetTimer(ChannelHandlerContext context) {
        if(timeoutTimer == null) {
            return;
        }

        timeoutTimer.cancel();
        timeoutTimer.purge();
        timeoutTimer = null;
    }

    private void startTimer(ChannelHandlerContext context) {
        final ShinhanMessageDecoder self = this;

        timeoutTimer = new Timer();
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                self.disconnect(context);
            }
        }, inputTimeout);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if(decoded != null) {
            out.add(decoded);
        }
    }

    private ByteBuf decode(ChannelHandlerContext context, ByteBuf in) {
        LOGGER.trace("in.readableBytes(): {}", in.readableBytes());
        if(in.readableBytes() == 0) {
            return null;
        }

        resetTimer(context);

        if(in.readableBytes() < 4) {
            startTimer(context);
            return null;
        }

        final String lengthString = preview(in, 30, 4);
        LOGGER.debug("lengthString: {}", lengthString);
        int length = NumberUtils.toInt(lengthString, -1);
        LOGGER.trace("length: {}", length);

        if(length < 0) {
            LOGGER.error("길이 형식이 틀렸습니다: {}", length);
            disconnect(context);
            return null;
        }

        if(in.readableBytes() < (length)) {
            startTimer(context);
            return null;
        }
        
        // 본문만 응답
//        return in;
        return in.readRetainedSlice(length+30);
    }

    private String preview(ByteBuf in, int offset, int limit) {
        int readerIndex = in.readerIndex();
        byte[] bytes = new byte[limit];
        for(int i=0; i<limit; i++) {
            bytes[i] = in.getByte(readerIndex + offset + i);
        }

        return new String(bytes, Charset.forName("8859_1"));
    }

    protected void disconnect(ChannelHandlerContext context) {
        LOGGER.info("closing connection");
        context.disconnect();
    }

}
