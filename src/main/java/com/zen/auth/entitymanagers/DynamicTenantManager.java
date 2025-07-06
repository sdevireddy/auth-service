package com.zen.auth.entitymanagers;

import com.zen.auth.properties.TenantDatasourceProperties;
import com.zen.auth.common.entity.ZenUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicTenantManager {

    private final EntityManagerFactoryBuilder builder;
    private final TenantDatasourceProperties tenantDatasourceProperties;

    public DynamicTenantManager(EntityManagerFactoryBuilder builder, TenantDatasourceProperties tenantDatasourceProperties) {
        this.builder = builder;
        this.tenantDatasourceProperties = tenantDatasourceProperties;
    }

    public EntityManager getEntityManagerForTenant(String tenantId) {
        String url = tenantDatasourceProperties.getUrlPrefix() + tenantId;
        System.out.println("Using Tenant DB URL: " + url);

        DataSource dataSource = DataSourceBuilder.create()
            .url(url)
            .username(tenantDatasourceProperties.getUsername())
            .password(tenantDatasourceProperties.getPassword())
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none"); 
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        LocalContainerEntityManagerFactoryBean emfBean = builder
            .dataSource(dataSource)
            .packages("com.zen.auth.common.entity", ZenUser.class.getPackage().getName())
            .persistenceUnit(tenantId)
            .properties(props)
            .build();

        emfBean.afterPropertiesSet();
        EntityManagerFactory emf = emfBean.getObject();
        System.out.println("Managed entities: " + emf.getMetamodel().getEntities());
        
        return emf.createEntityManager();
    }
}
