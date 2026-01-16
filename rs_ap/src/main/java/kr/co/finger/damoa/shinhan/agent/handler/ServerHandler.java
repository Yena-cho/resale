package kr.co.finger.damoa.shinhan.agent.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import kr.co.finger.damoa.model.msg.MsgIF;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private BlockingQueue<String> responseBlockingQueue;
    private DamoaIdleHandler damoaIdleHandler;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public ServerHandler(BlockingQueue<String> responseBlockingQueue, DamoaIdleHandler damoaIdleHandler) {
        this.responseBlockingQueue = responseBlockingQueue;
        this.damoaIdleHandler = damoaIdleHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("******************** serverchannelActive... ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("******************** serverchannelInactive... ");
    }

    /**
     * 관리전문중에 걸려내야 할 것들은 걸러낸다.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("******************** serverhandler channelRead... ********************");
        String input = (String) msg;
        damoaIdleHandler.init();
        if(input.startsWith("HDRREQPOLL")){
            // 폴링전문이 오면 바로 응답을 줌.
            handlePolling(ctx,input);
        }else {

            responseBlockingQueue.put(input);
        }
    }

    private void handlePolling(ChannelHandlerContext ctx,String polling){
        LOG.info("******************** serverhandler handlePolling... ********************");
        polling = StringUtils.replace(polling,"HDRREQPOLL","HDRRESPOLL");
        polling = "0020"+polling;
        ctx.channel().writeAndFlush(polling);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error(cause.getMessage(), cause);
        ctx.close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }
}
