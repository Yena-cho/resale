package kr.co.finger.damoa.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"kr.co.finger.damoa.scheduler", "kr.co.finger.shinhandamoa", "kr.co.finger.shinhandamoa.data"})
@MapperScan(basePackages = "kr.co.finger.shinhandamoa.data")
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}

