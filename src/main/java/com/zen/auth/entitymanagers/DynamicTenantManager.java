package com.zen.auth.entitymanagers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.stereotype.Component;

import com.zen.auth.common.entity.ZenUser;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicTenantManager {

    private final EntityManagerFactoryBuilder builder;

    @Value("${tenant.datasource.url-prefix}")
    private String urlPrefix;

    @Value("${tenant.datasource.username}")
    private String username;

    @Value("${tenant.datasource.password}")
    private String password;

    public DynamicTenantManager(EntityManagerFactoryBuilder builder) {
        this.builder = builder;
    }

    public EntityManager getEntityManagerForTenant(String tenantId) {
        String url = urlPrefix + tenantId;
        System.out.println("url" + url);

        DataSource dataSource = DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none"); // Or validate/update
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        LocalContainerEntityManagerFactoryBean emfBean = builder
            .dataSource(dataSource)
            .packages("com.zen.auth.common.entity")
            .packages(ZenUser.class)
            .persistenceUnit(tenantId+"")
            .properties(props)
            .build();

        emfBean.afterPropertiesSet();
        EntityManagerFactory emf = emfBean.getObject();
        System.out.println("Managed entities: " + emf.getMetamodel().getEntities());
        return emf.createEntityManager();
        //return emfBean.getObject().createEntityManager();
    }
}

