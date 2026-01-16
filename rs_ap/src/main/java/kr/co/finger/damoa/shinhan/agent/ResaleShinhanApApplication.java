package kr.co.finger.damoa.shinhan.agent;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Random;

@ComponentScan({"kr.co.finger.msgagent","kr.co.finger.damoa.shinhan.agent"})
@MapperScan(basePackages = {"kr.co.finger.damoa.shinhan.agent.mapper", "kr.co.finger.shinhandamoa.data.table.mapper"})
@SpringBootApplication
public class ResaleShinhanApApplication implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random(System.nanoTime());
        ConfigurableApplicationContext context = SpringApplication.run(ResaleShinhanApApplication.class, args);
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);

    }

    //	@Override
    public void run(String... strings) throws Exception {


    }
}
