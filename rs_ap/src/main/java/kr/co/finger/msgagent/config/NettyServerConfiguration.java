package kr.co.finger.msgagent.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class NettyServerConfiguration {
    @Value("${netty.server.transferHost}")
    private String transferHost;
    @Value("${netty.server.transferPort}")
    private int transferPort;           // 서버의 Transfer Port. tcp 등이 이용할 포트 번호이다.
    @Value("${netty.server.threadCountBoss}")
    private int threadCountBoss;        // Netty Server 의 Boss Thread 수이다.
    @Value("${netty.server.threadCountWorker}")
    private int threadCountWorker;      // Netty Server 의 Worker Thread 수이다.
    @Value("${netty.server.logLevelPipeline}")
    private String logLevelPipeline;

    @Autowired
    private ChannelInitializer serverChannelInitializer;
    /**
     * Netty Server 의 Boss Thread 설정.
     * 추후 http, udp 등의 설정이 필요할 경우 case 을 추가하여 설정을 변경할 수 있다.
     *
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(threadCountBoss);
    }

    /**
     * Netty Server 의 Worker Thread 설정.
     * 추후 http, udp 등의 설정이 필요할 경우 case 을 추가하여 설정을 변경할 수 있다.
     *
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(threadCountWorker);
    }

    /**
     * Transfer Port 설정.
     * @return
     */
    @Bean
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(transferHost,transferPort);
    }

    /**
     * Netty ServerBootStrap 설정.
     * LogLevel 을 지정해주고 사용자의 입력을 처리해줄 Handler 을 등록해주는데, Netty.Server.Initializer.MsgAgentConfigIF 을 통해 이를 설정해준다.
     * 그리고 Transfer Type 에 따ㄹ channel 을 등록해준다.
     *
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap() {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class);

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        serverBootstrap
                .childHandler(createChnnaelIntializer());

        return serverBootstrap;

    }

    public ChannelInitializer createChnnaelIntializer() {
        return serverChannelInitializer;
    }
}
