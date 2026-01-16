package kr.co.finger.shinhandamoa.test;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 테스트 설정
 *
 * @author wisehouse@finger.co.kr
 */
//@SpringBootConfiguration
@SpringBootConfiguration
// @EnableAutoConfiguration
//@MapperScan(basePackages = "kr.co.finger.shinhandamoa.data")
//@ComponentScan(basePackages = "kr.co.finger.shinhandamoa.data")
@ComponentScan({"kr.co.finger.damoa.scheduler", "kr.co.finger.shinhandamoa", "kr.co.finger.shinhandamoa.data"})
@MapperScan(basePackages = "kr.co.finger.shinhandamoa.data")
//@Import({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class TestConfig {

}
