package kr.co.finger.msgagent.config;

import kr.co.finger.msgagent.util.NettyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class NettyClientHandler {
    @Value("${netty.client.period:1000}")
    private int period;
    private ScheduledExecutorService scheduledExecutorService;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void start() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (NettyHelper.getChannelInitializer() != null) {
                        NettyHelper.createClientChannel();
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(),e);
                }
            }
        }, 1000, period, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        scheduledExecutorService.shutdown();
    }

}
