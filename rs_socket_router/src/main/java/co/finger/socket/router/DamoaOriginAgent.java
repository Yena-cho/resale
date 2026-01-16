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
 * 다모아 에이전트
 * 
 */
@Component
public class DamoaOriginAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(DamoaOriginAgent.class);

    @Autowired
    private BlockingQueue<ByteBuf> tx1;

    @Autowired
    private BlockingQueue<ByteBuf> tx4;
    
    private SocketServer server;
    
    private SocketClient client;
    
    @Value("${damoa-origin.remote.host}")
    private String remoteHost;
    
    @Value("${damoa-origin.remote.port}")
    private int remotePort;
    
    @Value("${damoa-origin.remote.wait-time-to-poll}")
    private int remoteWaitTimeToPoll;
    
    @Value("${damoa-origin.local.port}")
    private int localPort;
    
    @PostConstruct
    private void postConstruct() {
        server = new SocketServer();
        server.setPort(localPort);
        server.setHandlerFactory(() -> {
            final List<ChannelHandler> channelHandlers = new ArrayList<>();
            channelHandlers.add(new LoggingHandler(DamoaOriginAgent.class, LogLevel.TRACE));
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
            channelHandlers.add(new LoggingHandler(DamoaOriginAgent.class, LogLevel.TRACE));
            channelHandlers.add(new ShinhanMessageDecoder());
//            channelHandlers.add(new SocketClientKeepAliveHandler(remoteWaitTimeToPoll, 5000));
            channelHandlers.add(new TakeQueueHandler(tx1));
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
