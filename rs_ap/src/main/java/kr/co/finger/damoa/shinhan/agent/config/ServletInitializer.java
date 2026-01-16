package kr.co.finger.damoa.shinhan.agent.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
//        return createSpringApplicationBuilder().sources(DamoaShinhanAgentApplication.class);
    }
}
