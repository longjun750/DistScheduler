package com.yq.config;

import javax.sql.DataSource;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Simple to Introduction
 * className: DataSourceConfig
 *
 * @author EricYang
 * @version 2018/11/17 17:52
 */
@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.pwd-public-key}")
    private String pwdPublicKey;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.connectionProperties")
    private String properties;

    @Bean
    public DataSource dataSource() throws Exception {
        DruidDataSource configDataSource = new DruidDataSource();
        configDataSource.setUrl(url);
        configDataSource.setDriverClassName(driverClassName);
        configDataSource.setUsername(userName);
        configDataSource.setPassword(ConfigTools.decrypt(pwdPublicKey, password));
        configDataSource.setInitialSize(initialSize);
        configDataSource.setDefaultAutoCommit(true);
        configDataSource.setMinIdle(minIdle);
        configDataSource.setMaxActive(maxActive);
        configDataSource.setMaxWait(maxWait);
        configDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        configDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        configDataSource.setValidationQuery(validationQuery);
        configDataSource.setTestWhileIdle(testWhileIdle);
        configDataSource.setTestOnBorrow(testOnBorrow);
        configDataSource.setTestOnReturn(testOnReturn);
        configDataSource.setConnectionProperties(properties);

        return configDataSource;
    }
}
