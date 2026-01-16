package kr.co.finger.damoa.shinhan.agent.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaIdleHandler;
import kr.co.finger.damoa.shinhan.agent.handler.ServerHandler;
import kr.co.finger.msgagent.codec.VirtualAccountMsgCodec;
import kr.co.finger.msgagent.codec.VirtualAccountMsgDecoder;
import kr.co.finger.msgagent.codec.VirtualAccountMsgEncoder;

import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private String charset;
    private String logLevelPipeline;
    private int readerIdleTimeSeconds;
    private BlockingQueue<String> responseBlockingQueue;
    private DamoaIdleHandler damoaIdleHandler;
    public ServerChannelInitializer(String charset, String logLevelPipeline, int readerIdleTimeSeconds,BlockingQueue<String> responseBlockingQueue,DamoaIdleHandler damoaIdleHandler) {
        this.charset = charset;
        this.logLevelPipeline = logLevelPipeline;
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
        this.responseBlockingQueue = responseBlockingQueue;
        this.damoaIdleHandler = damoaIdleHandler;
    }

    /**
     * 채널 파이프라인 설정.
     * Netty.Server.Configuration.NettyServerConfiguration 에서 등록한 Bean 을 이용해 사용자의 통신을 처리할 Handler 도 등록.
     * Netty.Server.Handler.JsonHandler 에서 실제 사용자 요청 처리.
     *
     * @param channel
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {

        channel.pipeline()
                .addLast(new LoggingHandler(LogLevel.valueOf(logLevelPipeline)))
                .addLast(new VirtualAccountMsgDecoder(Charset.forName(charset)))
                .addLast(new VirtualAccountMsgEncoder(Charset.forName(charset)))
//                .addLast(new VirtualAccountMsgCodec(Charset.forName(charset)))
                .addLast(new IdleStateHandler(readerIdleTimeSeconds, 0, 0))
                .addLast(new ServerHandler(responseBlockingQueue,damoaIdleHandler));

    }

}

