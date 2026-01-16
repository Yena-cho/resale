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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * HDRREQPOLL을 요청하는 핸들러
 * 
 * waitTimeToPoll에 한 번씩 요청하고, waitTimeToTimeout 안에 응답이 안오면 연결을 끊는다.
 *
 * @author wisehouse@finger.co.kr
 */
public class SocketClientKeepAliveHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketClientKeepAliveHandler.class);

    private Timer pollTimer;
    private Timer timeoutTimer;
    private String time;

    private long waitTimeToPoll;
    private long waitTimeToTimeout;

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("MMddHHmmss");

    public SocketClientKeepAliveHandler() {
        this(300000, 5000);
    }

    public SocketClientKeepAliveHandler(long waitTimeToPoll, long waitTimeToTimeout) {
        this.waitTimeToPoll = waitTimeToPoll;
        this.waitTimeToTimeout = waitTimeToTimeout;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        startPollTimer(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        stopPollTimer();
        super.channelInactive(ctx);
    }

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
        final String systemId = content.substring(0, 3);
        final String command = content.substring(3, 10);

        if(!StringUtils.equals(command, "RESPOLL")) {
            super.channelRead(context, byteBuf);
            return;
        }

        responsePoll(context);
    }

    private void responsePoll(ChannelHandlerContext ctx) {
        LOGGER.info("RESPOLL 수신");
        stopTimeoutTimer();
        stopPollTimer();
        startPollTimer(ctx);
    }

    private void requestPoll(ChannelHandlerContext context) {
        LOGGER.info("REQPOLL 송신");

        String datetime = DATE_FORMAT.format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HDRREQPOLL");
        stringBuilder.append(datetime);

        write(context, stringBuilder.toString());
        startTimeoutTimer(context);
    }

    private void write(final ChannelHandlerContext context, final String message) {
        int messageLength = message.length();
        String messageLengthString = StringUtils.leftPad(String.valueOf(messageLength), 4, '0');
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageLengthString);
        stringBuilder.append(message);

        final ByteBuf buffer = context.alloc().buffer(stringBuilder.length());
        buffer.writeCharSequence(stringBuilder.toString(), Charset.forName("8859_1"));

        context.write(buffer);
        context.flush();
    }

    private void startPollTimer(ChannelHandlerContext context) {
        pollTimer = new Timer();
        pollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestPoll(context);
            }
        }, waitTimeToPoll);
    }

    private void stopPollTimer() {
        if (pollTimer == null) {
            return;
        }

        pollTimer.cancel();
        pollTimer.purge();
        pollTimer = null;
    }

    private void startTimeoutTimer(ChannelHandlerContext context) {
        timeoutTimer = new Timer();
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.disconnect();
            }
        }, waitTimeToTimeout);
    }

    private void stopTimeoutTimer() {
        if (timeoutTimer == null) {
            return;
        }

        timeoutTimer.cancel();
        timeoutTimer.purge();
        timeoutTimer = null;
    }
}
