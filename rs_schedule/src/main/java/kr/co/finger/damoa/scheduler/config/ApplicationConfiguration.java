package kr.co.finger.damoa.scheduler.config;

import kr.co.finger.damoa.scheduler.dao.BatchWorkerDao;
import kr.co.finger.damoa.scheduler.dao.BatchWorkerDaoImpl;
import kr.co.finger.msgio.layout.LayoutFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class ApplicationConfiguration {

    @Value("${damoa.messageFactory.cashMessageFilePath:cashMessage.xml}")
    private String cashMessageFilePath;

    @Value("${damoa.messageFactory.withdrawalMessageFilePath:message.xml}")
    private String withdrawalMessageFilePath;

    @Value("${damoa.messageFactory.corpInfoMessageFilePath:corp-message.xml}")
    private String corpInfoMessageFilePath;

    @Value("${damoa.message.charset:KSC5601}")
    private String charset;

    @Value("${damoa.cash.filePath:./yyyy/MM/dd}")
    private String cashFilePath;

    @Value("${mybatis.plugin.loggingSql:false}")
    private String loggingSql;

    @Autowired
    private SqlSession sqlSession;

    @Bean
    public BatchWorkerDao batchWorkerDao(){
        return new BatchWorkerDaoImpl(sqlSession);
    }

    @Bean
    public LayoutFactory cashLayoutFactory() throws Exception {
        LayoutFactory layoutFactory = new LayoutFactory();
        layoutFactory.initFactory(cashMessageFilePath, Charset.forName(charset));
        return layoutFactory;
    }
    @Bean
    public LayoutFactory withdrawalLayoutFactory() throws Exception {
        LayoutFactory layoutFactory = new LayoutFactory();
        layoutFactory.initFactory(withdrawalMessageFilePath, Charset.forName(charset));
        return layoutFactory;
    }
    @Bean
    public LayoutFactory corpInfoLayoutFactory() throws Exception {
        LayoutFactory layoutFactory = new LayoutFactory();
        layoutFactory.initFactory(corpInfoMessageFilePath, Charset.forName(charset));
        return layoutFactory;
    }

    @Bean
    public String cashFilePath() {
        return cashFilePath;
    }

    @Bean
    public Charset charSet() {
        return Charset.forName(charset);
    }
}
