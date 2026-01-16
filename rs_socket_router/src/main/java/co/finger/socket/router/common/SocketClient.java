package co.finger.socket.router.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SocketClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketClient.class);

    private String host;
    private int port;
    private HandlerFactory handlerFactory;

    private ChannelFuture channelFuture;

    public SocketClient() {
        this.host = "127.0.0.1";
        this.port = 8082;
        this.handlerFactory = new HandlerFactory() {
            public List<ChannelHandler> createHandler() {
                return Collections.emptyList();
            }
        };
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public void connect() {
        final EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    for (ChannelHandler each : handlerFactory.createHandler()) {
                        ch.pipeline().addLast(each);
                    }
                }
            });

            LOGGER.debug("서버 접속 시작");
            channelFuture = bootstrap.connect(host, port);
            channelFuture.sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        } finally {
            if (channelFuture != null) {
                try {
                    channelFuture.channel().closeFuture().sync();
                    LOGGER.debug("서버 접속 종료");
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }

                }
            }

            try {
                workerGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }

            }

            LOGGER.debug("워커그룹 종료");
        }
    }

    public void disconnect() {
        if (channelFuture == null) {
            return;
        }

        channelFuture.channel().disconnect();
    }

}
