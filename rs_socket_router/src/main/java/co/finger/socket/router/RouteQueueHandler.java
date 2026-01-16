package co.finger.socket.router;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;

/**
 * 들어온 데이터를 대기열에 넣는 핸들러
 * 
 * @author wisehouse@finger.co.kr
 */
public class RouteQueueHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteQueueHandler.class);
    private List<Pair<Predicate<ByteBuf>, BlockingQueue<ByteBuf>>> routeTable;


    public RouteQueueHandler() {
        routeTable = new ArrayList<>();
    }
    
    public void addRoute(Predicate<ByteBuf> predicate, BlockingQueue<ByteBuf> blockingQueue) {
        final Pair<Predicate<ByteBuf>, BlockingQueue<ByteBuf>> route = new ImmutablePair<>(predicate, blockingQueue);
        this.routeTable.add(route);
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

        for (Pair<Predicate<ByteBuf>, BlockingQueue<ByteBuf>> each : routeTable) {
            Predicate<ByteBuf> predicate = each.getKey();
            if(!predicate.test((ByteBuf) message)) {
                continue;
            }

            BlockingQueue<ByteBuf> blockingQueue = each.getValue();
            blockingQueue.put((ByteBuf) message);
        }
    }
}
