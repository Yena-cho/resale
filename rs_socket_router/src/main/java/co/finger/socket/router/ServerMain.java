package co.finger.socket.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 서버 메인
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ServerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);
    
    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServerMain.class, args);

        DamoaOriginAgent damoaOriginAgent = context.getBean("damoaOriginAgent", DamoaOriginAgent.class);
        damoaOriginAgent.start();
        
        DamoaResellAgent damoaResellAgent = context.getBean("damoaResellAgent", DamoaResellAgent.class);
        damoaResellAgent.start();

        FingerVaAgent fingerVaAgent = context.getBean("fingerVaAgent", FingerVaAgent.class);
        fingerVaAgent.start();
    }
}
