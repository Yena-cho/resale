package kr.co.finger.msgagent.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * NettyServer 클래스.
 * 쓰레드로 기동함.
 * 그렇지 않으면 이곳에서 블록이 됨.
 */
@Component
public class NettyServer implements Runnable{

    @Autowired
    private ServerBootstrap serverBootstrap;
    @Autowired
    private InetSocketAddress inetSocketAddress;

    private Channel channel;

    private Logger LOG = LoggerFactory.getLogger(getClass());
    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    public void run() {
        try {
            LOG.info("start... " + inetSocketAddress);

            ChannelFuture channelFuture = serverBootstrap.bind(inetSocketAddress).sync(); // (7)
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                }
            });
            channel = channelFuture.channel().closeFuture().sync().channel();
            LOG.info("started... " + inetSocketAddress);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        if (channel != null) {
            channel.close();
            channel.parent().close();
        }

    }

}
