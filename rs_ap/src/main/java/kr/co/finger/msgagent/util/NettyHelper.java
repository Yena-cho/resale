package kr.co.finger.msgagent.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import kr.co.finger.msgagent.client.NettyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * NettyClient 에서 사용할 수 있는 공통 메소드를 정의함.
 */
public final class NettyHelper {
    private static InetSocketAddress clientAddress;
    private static NioEventLoopGroup eventLoopGroup;
    private static int connectionTimeoutMills;
    private static ChannelInitializer channelInitializer;
    private static ChannelFuture channelFuture;

    private static Map<String, String> TEMP = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(NettyHelper.class);

    public static void setChannelInitializer(ChannelInitializer channelInitializer) {
        NettyHelper.channelInitializer = channelInitializer;
    }

    public static ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    public static void setConnectionTimeoutMills(int connectionTimeoutMills) {
        NettyHelper.connectionTimeoutMills = connectionTimeoutMills;
    }

    public static void setEventLoopGroup(NioEventLoopGroup eventLoopGroup) {
        NettyHelper.eventLoopGroup = eventLoopGroup;
    }

    public static void setClientAddress(InetSocketAddress clientAddress) {
        NettyHelper.clientAddress = clientAddress;
    }

    /**
     * Closes the given channel asynchronously
     *
     * @param channel the channel to close
     */
    public static void close(Channel channel) {
        if (channel != null) {
            channel.close().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) {
                    LOG.trace("Channel closed: {}", future.channel());
                }
            });
        }
    }

    public static void writeWithLine(NettyProducer nettyProducer, String message) throws Exception {
        LOG.info(">>> " + message);
        nettyProducer.write(message + "\r\n");
    }

    public static void writeWithLine(Channel channel, String message) throws Exception {
        LOG.info(">>> " + message);
        channel.writeAndFlush(message + "\r\n");
    }

    public static void writeWithLineAndClose(Channel channel, String message) throws Exception {
        LOG.info(">>> " + message);
        channel.writeAndFlush(message + "\r\n").addListener(ChannelFutureListener.CLOSE);
    }


    public static ChannelFuture openConnection() throws InterruptedException {
        return channelFuture;
    }

    /**
     * 채널을 생성함..
     */
    public static void createClientChannel() {
        if (channelFuture == null || validateObject(channelFuture) == false) {
            if (isOk(channelFuture) == false) {
                Bootstrap clientBootstrap = new Bootstrap();
                clientBootstrap.channel(NioSocketChannel.class);
                clientBootstrap.group(eventLoopGroup);
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                clientBootstrap.option(ChannelOption.TCP_NODELAY, true);
                clientBootstrap.option(ChannelOption.SO_REUSEADDR, true);
                clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMills);
                clientBootstrap.handler(channelInitializer);
                channelFuture = clientBootstrap.connect(clientAddress);
            }
        }
    }

    public static boolean validateObject(ChannelFuture channelFuture) {
        if (!channelFuture.isDone()) {
            return true;
        }
        if (!channelFuture.isSuccess()) {
            return false;
        }
        Channel channel = channelFuture.channel();
        boolean answer = channel.isActive();
        return answer;
    }

    public static boolean isOk(String id) {
        boolean isOk = TEMP.containsKey(id);
        return isOk;
    }

    public static boolean isOk(ChannelFuture channelFuture) {
        if (channelFuture == null) {
            return false;
        } else {
            return isOk(channelFuture.channel().id().asShortText());
        }
    }

    public static void put(String id) {
        LOG.info("PUT " + id);
        TEMP.put(id, id);
    }

    public static void delete(String id) {
        LOG.info("delete " + id);
        TEMP.remove(id);
    }

}
