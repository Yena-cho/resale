package co.finger.socket.router;

import co.finger.socket.router.common.SocketClient;
import co.finger.socket.router.common.SocketServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 재판매 에이전트
 * 
 */
@Component
public class DamoaResellAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(DamoaResellAgent.class);

    @Autowired
    private BlockingQueue<ByteBuf> tx2;

    @Autowired
    private BlockingQueue<ByteBuf> tx4;
    
    private SocketServer server;
    
    private SocketClient client;

    @Value("${damoa-resell.remote.host}")
    private String remoteHost;

    @Value("${damoa-resell.remote.port}")
    private int remotePort;

    @Value("${damoa-resell.remote.wait-time-to-poll}")
    private int remoteWaitTimeToPoll;

    @Value("${damoa-resell.local.port}")
    private int localPort;

    @PostConstruct
    private void postConstruct() {
        server = new SocketServer();
        server.setPort(localPort);
        server.setHandlerFactory(() -> {
            final List<ChannelHandler> channelHandlers = new ArrayList<>();
            channelHandlers.add(new LoggingHandler(DamoaResellAgent.class, LogLevel.TRACE));
            channelHandlers.add(new ShinhanMessageDecoder());
//            channelHandlers.add(new SocketServerKeepAliveHandler());
            channelHandlers.add(new PutQueueHandler(tx4));
            return channelHandlers;
        });

        client = new SocketClient();
        client.setHost(remoteHost);
        client.setPort(remotePort);
        client.setHandlerFactory(() -> {
            final List<ChannelHandler> channelHandlers = new ArrayList<>();
            channelHandlers.add(new LoggingHandler(DamoaResellAgent.class, LogLevel.TRACE));
            channelHandlers.add(new ShinhanMessageDecoder());
//            channelHandlers.add(new SocketClientKeepAliveHandler(remoteWaitTimeToPoll, 5000));
            channelHandlers.add(new TakeQueueHandler(tx2));
            return channelHandlers;
        });
    }
    
    public void start() {
        new Thread(server).start();
        
        new Thread(() -> {
            while(true) {
                client.connect();
            }
        }).start();
    }
    
    public void stop() {
    }
}
