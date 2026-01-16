package co.finger.socket.router;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * 들어온 데이터를 대기열에 넣는 핸들러
 * 
 * @author wisehouse@finger.co.kr
 */
public class TakeQueueHandler extends ChannelDuplexHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TakeQueueHandler.class);
    
    private final BlockingQueue<ByteBuf> blockingQueue;

    private Thread txThread;
    
    public TakeQueueHandler(BlockingQueue<ByteBuf> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        startTxThread(context);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        super.channelInactive(context);
        stopTxThread(context);
    }

    private void stopTxThread(final ChannelHandlerContext context) {
        LOGGER.debug("txThread: {}", txThread);
        
        if(txThread != null) {
            LOGGER.debug("쓰레드 중지");
            LOGGER.debug("context: {}", context);
            LOGGER.debug("thread: {}", txThread);
            txThread.interrupt();
            txThread = null;
        }
    }

    private void startTxThread(final ChannelHandlerContext context) {
        txThread = new Thread(() -> {
            while(Thread.currentThread().isAlive()) {
                try {
                    LOGGER.debug("wait for take: {}", context);
                    ByteBuf message = blockingQueue.take();
                    LOGGER.debug("take message: {}", message);

                    LOGGER.debug("threadStatus: {}", Thread.currentThread().getState().name());
                    
                    
                    if(Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    
                    if(message == null) {
                        continue;
                    }
                    
                    LOGGER.debug("context: {}", context);
                    LOGGER.debug("thread: {}", Thread.currentThread());
                    context.writeAndFlush(message);
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                    
                    break;
                }
            }
            LOGGER.debug("TxThread 종료");
        });
        
        txThread.start();
        LOGGER.debug("thread: {}", txThread);
    }
}
