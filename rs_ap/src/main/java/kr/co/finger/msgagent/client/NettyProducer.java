package kr.co.finger.msgagent.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.msgagent.config.MyContext;
import kr.co.finger.msgagent.util.NettyHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

@Component(value = "NettyProducer")
public class NettyProducer implements NettyClient {
    @Autowired
    private Charset charset;

    private BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static Logger LOG = LoggerFactory.getLogger(NettyProducer.class);

    public void clearQueue() {
        blockingQueue.clear();
    }

    public void write(String msg) throws Exception {
        if (msg.contains("HDRREQPOLL")) {
        } else {
            msg = appendHeader(msg);
        }
        blockingQueue.put(msg);
        LOG.info("PUT Queue " + msg);
    }

    public void writeNext(String msg) throws Exception {
        blockingQueue.put(msg);
    }

    @PostConstruct
    public void handleBlockingQueue() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = blockingQueue.peek();
                    if (msg != null) {
                        handleChnnelObjectPool(msg, blockingQueue);
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }

            }
        }, 1000, 1, TimeUnit.MILLISECONDS);
    }

    private ChannelFuture getChannelFuture() throws InterruptedException {
        return NettyHelper.openConnection();
    }

    private void handleChnnelObjectPool(String msg, BlockingQueue<String> blockingQueue) throws Exception {
        ChannelFuture channelFuture = getChannelFuture();
        if (channelFuture != null) {
            Channel channel = channelFuture.channel();
            String id = channel.id().asShortText();
            boolean isOk = NettyHelper.isOk(id);
            LOG.info("ChannelId=" + id + " " + isOk);
            if (isOk == true) {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        LOG.info(">>> ]" + msg + "[");
                        String _msg = blockingQueue.take();
                        LOG.info("TAKE Queue " + _msg);

                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await(100, TimeUnit.MILLISECONDS);
            } else {
                LOG.info("channel is no active... can's send msg.. ");
                Thread.sleep(1000);
            }

        } else {
            LOG.error("valid channelFuture no exist..");
            Thread.sleep(1000);
        }
    }

    private String appendLength(String msg) {
        return Damoas.appendLength(msg, charset);
    }

    public void write(String msg, ChannelFutureListener channelFutureListener) throws Exception {
        ChannelFuture channelFuture = getChannelFuture();
        if (channelFuture != null) {
            Channel channel = channelFuture.channel();
            if (msg.startsWith("HDR")) {
                msg = appendLength(msg);
            } else {
                msg = appendHeader(msg);
            }
            LOG.info(">>> ]" + msg + "[");
            channel.writeAndFlush(msg).addListener(channelFutureListener);
        } else {
            LOG.error("valid channelFuture no exist..");
            Thread.sleep(1000);
        }

    }

    private String appendHeader(String msg) {
        int length = msg.getBytes(charset).length;
        String header = "DHRT####    " + leftPad((length + 30) + "", 5, "0") + " DHR1DHRTB   ";
        return header + msg;
    }

    private String leftPad(String value, int length, String padStr) {
        return StringUtils.leftPad(value, length, padStr);
    }

    @PostConstruct
    public void initialize() throws Exception {
    }
//
//    public boolean isOK() {
//        if (channelObjectPool != null) {
//            if (channelObjectPool.getNumActive() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }

//    public String channelId() throws Exception {
//        if (channelObjectPool != null) {
//            ChannelFuture future = null;
//            try {
//                future = channelObjectPool.borrowObject();
//                return future.channel().id().toString();
//            } finally {
//                if (future != null) {
//                    channelObjectPool.returnObject(future);
//                }
//            }
//        } else {
//            return "";
//        }
//
//    }

    @PreDestroy
    public void shutdown() throws IOException {
    }
}
