package com.zen.auth.entitymanagers;


import com.zaxxer.hikari.HikariDataSource;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.properties.TenantDatasourceProperties;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicTenantManager {

    private static final Logger log = LoggerFactory.getLogger(DynamicTenantManager.class);

    private final EntityManagerFactoryBuilder builder;
    private  TenantDatasourceProperties tenantDatasourceProperties;
    private final Map<String, EntityManagerFactory> tenantFactories = new ConcurrentHashMap<>();

    public DynamicTenantManager(EntityManagerFactoryBuilder builder,
                                TenantDatasourceProperties tenantDatasourceProperties) {
        this.builder = builder;
        this.tenantDatasourceProperties = tenantDatasourceProperties;
    }

    /**
     * Automatically builds and returns EntityManager from tenant config.
     */
    public EntityManager getEntityManagerForTenant(String tenantId) {
        log.info("🔍 Requesting EntityManager for tenant: {}", tenantId);

        if (tenantFactories.containsKey(tenantId)) {
            log.debug("♻️ Reusing cached EntityManagerFactory for tenant: {}", tenantId);
            return tenantFactories.get(tenantId).createEntityManager();
        }

        // Fallback: build from properties
        String url = tenantDatasourceProperties.getUrlPrefix() + "/" + tenantId;
        return registerTenantDataSource(tenantId, url,
                tenantDatasourceProperties.getUsername(),
                tenantDatasourceProperties.getPassword())
                .createEntityManager();
    }

    /**
     * Manually registers tenant DataSource + JPA factory using full parameters.
     * Ideal to use this after Flyway migration.
     */
    public EntityManagerFactory registerTenantDataSource(String tenantId, String jdbcUrl, String username, String password) {
        log.info("🔧 Registering tenant datasource: {}", tenantId);
        log.debug("🔗 JDBC URL: {}", jdbcUrl);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaximumPoolSize(8);
        dataSource.setPoolName("TenantPool-" + tenantId);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        LocalContainerEntityManagerFactoryBean factoryBean = builder
                .dataSource(dataSource)
                .packages("com.zen.auth.common.entity", ZenUser.class.getPackage().getName())
                .persistenceUnit(tenantId)
                .properties(props)
                .build();

        factoryBean.afterPropertiesSet();
        EntityManagerFactory emf = factoryBean.getObject();

        if (emf == null) {
            log.error("❌ Failed to create EntityManagerFactory for tenant: {}", tenantId);
            throw new IllegalStateException("Failed to register tenant: " + tenantId);
        }

        tenantFactories.put(tenantId, emf);
        log.info("✅ Registered EntityManagerFactory for tenant: {}", tenantId);

        return emf;
    }
}
