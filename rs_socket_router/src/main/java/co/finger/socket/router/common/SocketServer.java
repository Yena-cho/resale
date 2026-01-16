package co.finger.socket.router.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class SocketServer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    private int port;
    private int bossThreadCount;
    private int workerThreadCount;
    private HandlerFactory handlerFactory;

    public SocketServer() {
        this.port = 10000;
        this.bossThreadCount = 1;
        this.workerThreadCount = 1;
        this.handlerFactory = new HandlerFactory() {
            public List<ChannelHandler> createHandler() {
                return Collections.emptyList();
            }
        };
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public void run() {
        LOGGER.info("start socket server at {}", port);

        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadCount);

        ServerBootstrap serverBootstrap = null;
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    LOGGER.trace("start of initChannel");

                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    for (ChannelHandler each : handlerFactory.createHandler()) {
                        channelPipeline.addLast(each);
                    }

                    LOGGER.trace("end of initChannel");
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            LOGGER.info("shutdown socket server");
        }
    }
}
