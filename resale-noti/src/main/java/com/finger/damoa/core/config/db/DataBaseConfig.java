//package com.finger.note.core.config.db;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.JdbcType;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
///**
// *
// * datasource
// * @author 윤준호
// * @since 2022.05.17
// *
// */
//@Configuration
//@MapperScan(basePackages = {"com.finger.note.mapper.**"})
//@EnableTransactionManagement
//public class DataBaseConfig {
//
//    @Value("${mybatis.mapper-locations}")
//    private String mapperLocation;
//
//    @Value("${mybatis.config-location}")
//    private String configLocation;
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.datasource.hikari")
//    public HikariConfig hikariConfig() {
//        return new HikariConfig();
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        return new HikariDataSource(hikariConfig());
//    }
//
//	@Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception{
//
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setConfigLocation(context.getResource(configLocation));
//        sqlSessionFactoryBean.setMapperLocations(context.getResources(mapperLocation));
//
//        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
//        sqlSessionFactory.getConfiguration().setAggressiveLazyLoading(true);
//        sqlSessionFactory.getConfiguration().setCacheEnabled(false);
//        sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL); // 입력값이 null 인경우 방지
//        sqlSessionFactory.getConfiguration().setCallSettersOnNulls(true); // 조회값자체가 null 인경우 column은 나오도록
//        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true); // a_b to aB
//        sqlSessionFactory.getConfiguration().setDatabaseId("postgresql");
//
//        return sqlSessionFactory;
//    }
//
//	@Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//}