package kr.co.finger.damoa.shinhan.agent.config;

import com.finger.shinhandamoa.typehandlers.util.ConfigUtil;
import io.netty.channel.ChannelInitializer;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.concurrent.AggregateQueue;
import kr.co.finger.damoa.commons.concurrent.AggregateQueueImpl;
import kr.co.finger.damoa.commons.mybatis.SqlLoggingPlugin;
import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.shinhan.agent.client.ClientChannelInitializer;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDaoImpl;
import kr.co.finger.damoa.shinhan.agent.fsm.Context;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaIdleHandler;
import kr.co.finger.damoa.shinhan.agent.model.MessageBean;
import kr.co.finger.damoa.shinhan.agent.model.NoticeMessageBean;
import kr.co.finger.damoa.shinhan.agent.server.ServerChannelInitializer;
import kr.co.finger.damoa.shinhan.agent.util.FingerIntegrateMessaging;
import kr.co.finger.damoa.shinhan.agent.util.FingerTalk;
import kr.co.finger.msgagent.config.MsgAgentConfigIF;
import kr.co.finger.msgagent.layout.MessageFactory;
import kr.co.finger.msgagent.layout.MessageInitializer;
import kr.co.finger.msgagent.layout.TranTypeCodeResolver;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 필요한 BEAN을 생성하는 configuration 클래스.
 */
@Configuration
public class CommonConfiguration implements MsgAgentConfigIF, MessageInitializer {

    @Value("${netty.message.charset}")
    private String charset;

    @Value("${message.format.fileName:NO}")
    private String messageFormatFileName;

    @Value("${mybatis.plugin.loggingSql:false}")
    private String loggingSql;

    @Value("${damoa.queue.size:1}")
    private int size;

    @Value("${damoa.queue.timeout:1000}")
    private long timeout;

    @Value("${netty.server.readerIdleTimeSeconds:30}")
    private int readerIdleTimeSeconds;

    @Value("${netty.client.writerIdleTimeSeconds:30}")
    private int writerIdleTimeSeconds;


    @Value("${netty.server.logLevelPipeline:INFO}")
    private String serverLogLevelPipeline;

    @Value("${netty.client.logLevelPipeline:INFO}")
    private String clientLogLevelPipeline;

    @Value("${damoa.threaPool.corePoolSize:5}")
    private int corePoolSize;
    @Value("${damoa.threaPool.maximumPoolSize:10}")
    private int maximumPoolSize;
    @Value("${damoa.threaPool.keepAliveTime:60000}")
    private long keepAliveTime;

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private Context context;


    @Value("${sms.server.host}")
    private String  smsHost;
    @Value("${sms.server.port}")
    private int  smsPort;
    @Value("${sms.accessToken}")
    private String  smsAccessToken;


    @Value("${at.server.host}")
    private String atHost;
    @Value("${at.server.serviceKey}")
    private String atServiceKey;


    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * 비동기처리 결과를 담아둘 객체.
     *
     * @return
     */
    @Bean
    public ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap() {
        return new ConcurrentHashMap<String, CompletableFuture<MsgIF>>();
    }

    /**
     * 서버를 통해서 받은 전문을 주고 받을 수 있는 블로킹큐..
     *
     * @return
     */
    @Bean
    public BlockingQueue<String> responseBlockingQueue() {
        return new LinkedBlockingQueue<>(10000);
    }

    /**
     * 전문을 비동기적으로 처리하기 위한 블로킹큐..
     *
     * @return
     */
    @Bean
    public BlockingQueue<NoticeMessageBean> msgBlockingQueue() {
        return new LinkedBlockingQueue<>(10000);
    }

    /**
     * 전문을 비동기적으로 처리하기 위한 블로킹큐..
     *
     * @return
     */
    @Bean
    public AggregateQueue<MessageBean> aggregateQueue() {
        LOG.info("aggregateQueue size=" + size + " timeout=" + timeout);
        return new AggregateQueueImpl(size, timeout);
    }

    /**
     * 통지전문을 처리하기 위한 블로킹큐....
     *
     * @return
     */
    @Bean
    public BlockingQueue<String> noticeBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }


    @Bean
    @Scope(value = "prototype")
    public ChannelInitializer clientChannelInitializer() {
        DamoaIdleHandler damoaIdleHandler = damoaIdleHandler();
        LOG.info("******************** clientChannelInitializer " + damoaIdleHandler);
        return new ClientChannelInitializer(messageFactory(), clientLogLevelPipeline, charset, writerIdleTimeSeconds, damoaIdleHandler());
    }

    @Bean
    public ChannelInitializer serverChannelInitializer() {
        LOG.info("******************** serverChannelInitializer ");
        return new ServerChannelInitializer(charset, serverLogLevelPipeline, readerIdleTimeSeconds, responseBlockingQueue(),damoaIdleHandler());
    }

    @PostConstruct
    @Override
    public void initializeMessageFactory() {

    }

    @PostConstruct
    public void chechState() {
        LOG.info("******************** checkState ");
        String date = DateUtils.toDateString(new Date());
        String finishFilePath = "./FINISH_"+date;
        String startFilePath = "./START_"+date;
        LOG.info("HEREDIR "+new File("./").getAbsolutePath());
        if (new File(finishFilePath).exists()) {
//            context.changeState("FINISH");
        } else {
            LOG.info("no exist "+finishFilePath);
            if (new File(startFilePath).exists()) {
                context.changeState("START");
            } else {
                LOG.info("no exist "+startFilePath);
                LOG.info("SKIP Change State....");
            }
        }
    }


    @Bean
    public MessageFactory messageFactory() {
        try {
            MessageFactory layoutFactory = new MessageFactory();
            layoutFactory.initFactory(messageFormatFileName, Charset.forName(charset));
            layoutFactory.initializeMessage(this);
            return layoutFactory;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return null;
        }
    }


    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }


    @Override
    public void initialize(Map<String, MsgIF> map) {
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String tranTypeCode = iterator.next();
            if (tranTypeCode.length() == 8) {
                CommonMessage damoaMessage = (CommonMessage) map.get(tranTypeCode);
                damoaMessage.setMsgTypeCode(tranTypeCode.substring(0, 4));
                damoaMessage.setDealTypeCode(tranTypeCode.substring(4, 8));
            }
        }
    }

    @Override
    public TranTypeCodeResolver getTranTypeCodeResolver() {
        return new TranTypeCodeResolver() {
            @Override
            public String getTranTypeCode(String msg) {
                return msg.substring(16, 24);
            }
        };
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                SqlLoggingPlugin sqlLoggingPlugin = new SqlLoggingPlugin();
                sqlLoggingPlugin.setLoggingSql(Boolean.valueOf(loggingSql));
                configuration.addInterceptor(sqlLoggingPlugin);

            }
        };

    }

    @Bean
    public Charset charset() {
        return Charset.forName(charset);
    }

    @Bean
    public DamoaIdleHandler damoaIdleHandler() {
        return new DamoaIdleHandler(messageFactory());
    }

    @Bean
    public DamoaDao damoaDao() {
        return new DamoaDaoImpl(sqlSession);
    }

    @Bean
    public FingerIntegrateMessaging fingerIntegrateMessaging(){
        FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();
        fingerIntegrateMessaging.setHost(smsHost);
        fingerIntegrateMessaging.setPort(smsPort);
        fingerIntegrateMessaging.setAccessToken(smsAccessToken);
        return fingerIntegrateMessaging;
    }

    @Bean
    public FingerTalk fingerTalk() {
        FingerTalk fingerTalk = new FingerTalk();
        fingerTalk.setUrl(atHost);
        fingerTalk.setServiceKey(atServiceKey);
        return fingerTalk;
    }

    @PostConstruct
    public void init() {
        LOG.info("ConfigUtil init..");
        ConfigUtil.init();
        LOG.info("ConfigUtil initialized....");
    }

}
