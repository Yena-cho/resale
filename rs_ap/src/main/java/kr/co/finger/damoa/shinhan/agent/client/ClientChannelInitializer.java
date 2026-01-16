package kr.co.finger.damoa.shinhan.agent.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import kr.co.finger.damoa.shinhan.agent.handler.ClientHandler;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaIdleHandler;
import kr.co.finger.msgagent.codec.VirtualAccountMsgCodec;
import kr.co.finger.msgagent.codec.VirtualAccountMsgDecoder;
import kr.co.finger.msgagent.codec.VirtualAccountMsgEncoder;
import kr.co.finger.msgagent.layout.MessageFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 다모아 클라이언트의 코덱과 처리핸들러를 등록한다.
 *
 */
public class ClientChannelInitializer extends ChannelInitializer<Channel> {

    private MessageFactory messageFactory;
    private String logLevelPipeline;
    private String charset;
    private int writerIdleTimeSeconds;
    private DamoaIdleHandler damoaIdleHandler;
    public ClientChannelInitializer(MessageFactory messageFactory, String logLevelPipeline, String charset,int writerIdleTimeSeconds,DamoaIdleHandler damoaIdleHandler) {
        this.messageFactory = messageFactory;
        this.logLevelPipeline = logLevelPipeline;
        this.charset = charset;
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
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
        Charset _charset = Charset.forName(charset);
        channel.pipeline()
                .addLast(new LoggingHandler(LogLevel.valueOf(logLevelPipeline)))
                .addLast(new VirtualAccountMsgDecoder(_charset))
                .addLast(new VirtualAccountMsgEncoder(_charset))
//                .addLast(new VirtualAccountMsgCodec(Charset.forName(charset)))
                .addLast(new IdleStateHandler(0, writerIdleTimeSeconds, 0))
                .addLast(new ClientHandler(messageFactory,damoaIdleHandler));

    }

}

