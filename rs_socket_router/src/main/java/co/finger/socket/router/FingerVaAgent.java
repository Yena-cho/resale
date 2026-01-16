package co.finger.socket.router;

import co.finger.socket.router.common.SocketClient;
import co.finger.socket.router.common.SocketServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 핑거 신한은행 라우터 에이전트
 *
 */
@Component
public class FingerVaAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(FingerVaAgent.class);

    @Autowired
    private BlockingQueue<ByteBuf> tx1;

    @Autowired
    private BlockingQueue<ByteBuf> tx2;

    @Autowired
    private BlockingQueue<ByteBuf> tx3;

    @Autowired
    private BlockingQueue<ByteBuf> tx4;

    private SocketServer server;

    private SocketClient client;

    @Value("${finger-va.remote.host}")
    private String remoteHost;

    @Value("${finger-va.remote.port}")
    private int remotePort;

    @Value("${finger-va.remote.wait-time-to-poll}")
    private int remoteWaitTimeToPoll;

    @Value("${finger-va.local.port}")
    private int localPort;

    @Value("${damoa-origin.remote.partner-code}")
    private String nonghyupBankPartnerCode;

    @Value("${damoa-resell.remote.partner-code}")
    private String nonghyupMfPartnerCode;

    @PostConstruct
    private void postConstruct() {
        server = new SocketServer();
        server.setPort(localPort);
        server.setHandlerFactory(() -> {
            final List<ChannelHandler> channelHandlers = new ArrayList<>();
            channelHandlers.add(new LoggingHandler(FingerVaAgent.class, LogLevel.TRACE));
            channelHandlers.add(new ShinhanMessageDecoder());
//            channelHandlers.add(new SocketServerKeepAliveHandler());

            RouteQueueHandler routeQueueHandler = new RouteQueueHandler();
            // 다모아
            routeQueueHandler.addRoute((byteBuf) -> {
                LOGGER.debug("다모아 전문여부 확인");
                final String txrxFlag = preview(byteBuf, 30+15, 1);
                if(!StringUtils.equals(txrxFlag, "2")) {
                    return false;
                }

                final String routePartnerCode = preview(byteBuf, 30+104, 8);
                // 기관코드분류: 20008153(고속도로),  20008153(기타)
                if(StringUtils.equals(routePartnerCode, "20008153") || StringUtils.equals(routePartnerCode, "20008155")) {
                    return false;
                }
                
                LOGGER.debug("전문수신기관코드: {}", routePartnerCode);
                LOGGER.debug("전문송신기관코드: {}", preview(byteBuf, 30+7, 8));
                
                return true;
            }, tx1);
            // 재판매
            routeQueueHandler.addRoute((byteBuf) -> {
                LOGGER.debug("재판매 전문여부 확인");
                final String txrxFlag = preview(byteBuf, 30+15, 1);
                if(!StringUtils.equals(txrxFlag, "2")) {
                    return false;
                }

                final String routePartnerCode = preview(byteBuf, 30+104, 8);
                // 기관코드분류: 20008153(고속도로),  20008153(기타)
                if(!StringUtils.equals(routePartnerCode, "20008153") && !StringUtils.equals(routePartnerCode, "20008155")) {
                    return false;
                }

                LOGGER.debug("전문수신기관코드: {}", routePartnerCode);
                LOGGER.debug("전문송신기관코드: {}", preview(byteBuf, 30+7, 8));

                return true;
            }, tx2);
            channelHandlers.add(routeQueueHandler);
            
            return channelHandlers;
        });

        client = new SocketClient();
        client.setHost(remoteHost);
        client.setPort(remotePort);
        client.setHandlerFactory(() -> {
            LOGGER.debug("채널 핸들러 초기화");
            final List<ChannelHandler> channelHandlers = new ArrayList<>();

            LOGGER.debug("로깅 핸들러 추가");
            channelHandlers.add(new LoggingHandler(FingerVaAgent.class, LogLevel.TRACE));

            LOGGER.debug("전문 디코더 추가");
            channelHandlers.add(new ShinhanMessageDecoder());

            LOGGER.debug("KeepAlive 추가");
//            channelHandlers.add(new SocketClientKeepAliveHandler(remoteWaitTimeToPoll, 5000));

            LOGGER.debug("테이크 큐 핸들러 추가");
            channelHandlers.add(new TakeQueueHandler(tx4));
            return channelHandlers;
        });
    }

    public void start() {
        new Thread(server).start();

        new Thread(() -> {
            while (true) {
                client.connect();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                    
                }
            }
        }).start();
    }

    private String preview(ByteBuf in, int offset, int limit) {
        int readerIndex = in.readerIndex();
        byte[] bytes = new byte[limit];
        for (int i = 0; i < limit; i++) {
            bytes[i] = in.getByte(readerIndex + offset + i);
        }

        return new String(bytes, Charset.forName("8859_1"));
    }


    public void stop() {
    }
}
