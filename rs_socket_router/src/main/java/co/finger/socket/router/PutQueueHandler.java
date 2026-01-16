package co.finger.socket.router;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * 들어온 데이터를 대기열에 넣는 핸들러
 * 
 * @author wisehouse@finger.co.kr
 */
public class PutQueueHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PutQueueHandler.class);
    
    private final BlockingQueue<ByteBuf> blockingQueue;
    
    public PutQueueHandler(BlockingQueue<ByteBuf> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        if(message == null) {
            LOGGER.warn("message is null");
            return;
        }
        
        if(message instanceof ByteBuf == false) {
            LOGGER.warn("message is not ByteBuf");
            return;
        }
        
        this.blockingQueue.put((ByteBuf) message);
    }
}
