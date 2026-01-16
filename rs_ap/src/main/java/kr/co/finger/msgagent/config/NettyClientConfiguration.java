package kr.co.finger.msgagent.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import kr.co.finger.msgagent.util.NettyHelper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Configuration
public class NettyClientConfiguration {
    @Value("${netty.client.transferHost:noHost}")
    private String transferHost;           // 서버의 Transfer Port. tcp 등이 이용할 포트 번호이다.
    @Value("${netty.client.transferPort:9999}")
    private int transferPort;           // 서버의 Transfer Port. tcp 등이 이용할 포트 번호이다.
    @Value("${netty.client.threadCountWorker:1}")
    private int threadCountWorker;      // Netty Server 의 Worker Thread 수이다.
    @Value("${netty.client.connectionTimeoutMills:1000}")
    private int connectionTimeoutMills;
    @Value("${netty.client.logLevelPipeline:INFO}")
    private String logLevelPipeline;

    @Value("${netty.client.pool.maxTotal:1}")
    private int maxTotal;
    @Value("${netty.client.pool.maxIdle:1}")
    private int maxIdle;
    @Value("${netty.client.pool.minIdle:1}")
    private int minIdle;
    @Value("${netty.client.pool.timeBetweenEvictionRunsMillis:1000}")
    private int timeBetweenEvictionRunsMillis;
//    @Value("${netty.client.pool.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;

    @Autowired
    private ChannelInitializer clientChannelInitializer;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Bean
    @Scope(value = "prototype")
    public NioEventLoopGroup clientWorkerGroup() {
        return new NioEventLoopGroup(threadCountWorker);
    }
    /**
     * @return
     */
    @Bean
    public InetSocketAddress clientAddress() {
        return new InetSocketAddress(transferHost, transferPort);
    }

    public ChannelInitializer createChnnaelIntializer() {
        return clientChannelInitializer;
    }

    @PostConstruct
    public void initialize() throws Exception {
        try {
            NettyHelper.setChannelInitializer(createChnnaelIntializer());
            NettyHelper.setClientAddress(clientAddress());
            NettyHelper.setConnectionTimeoutMills(connectionTimeoutMills);
            NettyHelper.setEventLoopGroup(clientWorkerGroup());
            NettyHelper.openConnection();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

    }



}
