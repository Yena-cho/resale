package com.finger.damoa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SessionAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableScheduling
public class ResaleNotiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResaleNotiApplication.class, args);
    }

}

