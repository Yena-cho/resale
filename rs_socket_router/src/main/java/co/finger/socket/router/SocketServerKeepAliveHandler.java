package co.finger.socket.router;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * HDRREQPOLL에 응답하는 핸들러
 * 
 * 요청이 오면 응답만 하면 된다. 
 *
 * @author wisehouse@finger.co.kr
 */
public class SocketServerKeepAliveHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerKeepAliveHandler.class);

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("MMddHHmmss");

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        final ByteBuf byteBuf = (ByteBuf) message;
        channelRead(context, byteBuf);
    }

    private void channelRead(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception {
        if(byteBuf.readableBytes() != 24) {
            super.channelRead(context, byteBuf);
            return;
        }

        final String messageString = byteBuf.toString(Charset.forName("8859_1"));
        channelRead(context, byteBuf, messageString);
    }

    private void channelRead(ChannelHandlerContext context, ByteBuf byteBuf, String message) throws Exception {
        final String header = StringUtils.substring(message, 0, 4); 
        final String content = StringUtils.substring(message, 4);
        final String systemId = content.substring(0, 3);
        
        if(StringUtils.length(message) != 24) {
            super.channelRead(context, byteBuf);
            return;
        }
        
        if(!StringUtils.equals(systemId, "HDR")) {
            super.channelRead(context, byteBuf);
            return;
        }

        execute(context, byteBuf, content);
    }

    private void execute(ChannelHandlerContext context, ByteBuf byteBuf, String content) throws Exception {
        String systemId = content.substring(0, 3);
        String command = content.substring(3, 10);
        
        if(!StringUtils.equals(command, "REQPOLL")) {
            super.channelRead(context, byteBuf);
            return;
        }

        responsePoll(context, content);
    }

    private void responsePoll(ChannelHandlerContext context, String content) {
        LOGGER.debug("REQPOLL 수신");

        String systemId = content.substring(0, 3);
        String command = content.substring(3, 10);
        String datetime = content.substring(10, 20);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HDRRESPOLL");
        stringBuilder.append(datetime);

        write(context, stringBuilder.toString());
    }

    private void write(final ChannelHandlerContext context, final String message) {
        LOGGER.debug("RESPOLL 송신");
        final int messageLength = message.length();
        String messageLengthString = StringUtils.leftPad(String.valueOf(messageLength), 4, '0');
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageLengthString);
        stringBuilder.append(message);

        final ByteBuf buffer = context.alloc().buffer(stringBuilder.length());
        buffer.writeCharSequence(stringBuilder.toString(), Charset.forName("8859_1"));

        context.write(buffer);
        context.flush();
    }
}
