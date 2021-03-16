package com.za.crud_mvc_hibernate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:db.properties")
@EnableTransactionManagement
public class JPAConfig {

    private final Environment environment;

    public JPAConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("db.driver"));
        dataSource.setUrl(environment.getRequiredProperty("db.url"));
        dataSource.setUsername(environment.getRequiredProperty("db.username"));
        dataSource.setPassword(environment.getRequiredProperty("db.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource){

        LocalContainerEntityManagerFactoryBean manager = new LocalContainerEntityManagerFactoryBean();
        manager.setDataSource(dataSource);
        manager.setPackagesToScan("com.za.crud_mvc_hibernate.model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        manager.setJpaVendorAdapter(vendorAdapter);
        manager.setJpaProperties(additionalProperties());

        return manager;
    }

    private Properties additionalProperties(){

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto",
                environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect",
                environment.getRequiredProperty("hibernate.dialect"));

        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
