package com.zen.auth.entitymanagers;

import jakarta.persistence.EntityManagerFactory;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.zen.auth.repository",
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager"
)
public class DefaultDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource defaultDataSource() {
        return defaultDataSourceProperties()
            .initializeDataSourceBuilder()
            .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
            EntityManagerFactoryBuilder builder
    ) {
        return builder
            .dataSource(defaultDataSource())
            .packages("com.zen.auth.common.entity")
            .persistenceUnit("default")
            .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("defaultEntityManagerFactory") EntityManagerFactory factory
    ) {
        return new JpaTransactionManager(factory);
    }
}

