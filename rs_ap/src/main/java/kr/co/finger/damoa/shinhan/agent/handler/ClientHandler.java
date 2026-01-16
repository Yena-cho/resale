package kr.co.finger.damoa.shinhan.agent.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import kr.co.finger.damoa.shinhan.agent.client.ClientChannelInitializer;
import kr.co.finger.msgagent.config.NettyClientConfiguration;
import kr.co.finger.msgagent.layout.MessageFactory;
import kr.co.finger.msgagent.util.NettyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 전문서버에서 전문을 전송하는 기능을 처리하는 netty handler..
 * 테스트콜 전문 전송을 위해 ChannelDuplexHandler 을 상속받아 처리함.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private MessageFactory messageFactory;
    private DamoaIdleHandler damoaIdleHandler;

    @Value("${netty.client.connectionTimeoutMills:1000}")
    private int connectionTimeoutMills;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public ClientHandler(MessageFactory messageFactory,DamoaIdleHandler damoaIdleHandler) {
        this.messageFactory = messageFactory;
        this.damoaIdleHandler = damoaIdleHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("******************** clientchannelActive... ");
        NettyHelper.put(ctx.channel().id().asShortText());
        damoaIdleHandler.init();
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("******************** clientchannelInactive... ");
        NettyHelper.delete(ctx.channel().id().asShortText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("******************** clienthandler channelRead... ********************");
        String input = (String)msg;

        LOG.debug("<<< "+input);
        // 이쪽으로는 폴링전문을 수신함.
        damoaIdleHandler.init();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error(cause.getMessage(), cause);
        ctx.close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOG.info("******************** clienthandler userEventTriggered... ********************");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            damoaIdleHandler.whenIdle(ctx, event);
        }
    }
}
