package com.example.demo.cfg;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.demo.dao.userTest", sqlSessionFactoryRef = "userTestSqlSessionFactory")
public class DataSourceUserTestConfig  {

        @Bean(name = "userTestDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.user-test")
        public DataSource dataSource() {
            HikariDataSource ds = (HikariDataSource) DataSourceBuilder.create().build();
            ds.setConnectionTestQuery("SELECT 1");
            return ds;
        }

        @Bean(name = "userTestSqlSessionFactory")
        public SqlSessionFactory testSqlSessionFactory(@Qualifier("userTestDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            //bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/userTest/*.xml"));
            return bean.getObject();
        }

        @Bean(name = "userTestTransactionManager")
        public DataSourceTransactionManager testTransactionManager(@Qualifier("userTestDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean(name = "userTestSqlSessionTemplate")
        public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("userTestSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
}