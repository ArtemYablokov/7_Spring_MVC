package com.yabloko.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


// ЧТОБЫ ИСПОЛЬЗОВАТЬ РЕПО !!!

@Configuration
@ComponentScan(value = "com.yabloko")
@EnableJpaRepositories(basePackages = "com.yabloko.repositories")
@EnableTransactionManagement
public class JpaConfiguration {

    @Autowired
    private DataSource dataSource;

    // НАСТРОЙКИ ХИБЕРНЕЙТА
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.yabloko.models");  // РАСПОЛОЖЕНИЕ МОДЕЛЕЙ

        // делает прослойку между SpringJPA и HIBERNATE - ADAPTER PATTERN
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);

        emf.setJpaVendorAdapter(adapter); 
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    // для СПРИНГ JPA выполняются только транзакции
    // - либо целиком выпоняются, либо не выполняются совсем !
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}