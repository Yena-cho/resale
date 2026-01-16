package co.finger.socket.router;

import co.finger.socket.router.common.SocketServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wisehouse@finger.co.kr
 */
@Configuration
public class SpringConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);

    @Bean
    protected BlockingQueue<ByteBuf> tx1() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    protected BlockingQueue<ByteBuf> tx2() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    protected BlockingQueue<ByteBuf> tx3() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    protected BlockingQueue<ByteBuf> tx4() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * 콘솔 수신
     *
     * @return
     */
    @Bean
    public SocketServer consoleSocketServer(BlockingQueue<ByteBuf> tx4) {
        SocketServer socketServer = new SocketServer();
        socketServer.setPort(19032);
        socketServer.setHandlerFactory(() -> {
            final List<ChannelHandler> channelHandlers = new ArrayList<>();
            channelHandlers.add(new LoggingHandler(SocketServer.class, LogLevel.TRACE));
            channelHandlers.add(new ShinhanMessageDecoder());
            channelHandlers.add(new PutQueueHandler(tx4));
            return channelHandlers;
        });
        new Thread(socketServer).start();
        return socketServer;
    }
}
