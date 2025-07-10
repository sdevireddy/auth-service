package com.zen.auth.services;

import com.zaxxer.hikari.HikariDataSource;
import com.zen.auth.entitymanagers.DynamicTenantManager;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlywayMigrationService {

    private static final Logger log = LoggerFactory.getLogger(FlywayMigrationService.class);

    @Value("${tenant.datasource.url-prefix}")
    private String urlPrefix;

    @Value("${tenant.datasource.username}")
    private String username;

    @Value("${tenant.datasource.password}")
    private String password;
    
    private final DynamicTenantManager tenantManager;
    
    public FlywayMigrationService(DynamicTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    public void runMigrations(String schemaName) {
        String jdbcUrl = urlPrefix + "/" + schemaName;
        log.info("🔄 Starting Flyway migration for schema: {}", schemaName);
        log.debug("Using JDBC URL: {}", jdbcUrl);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setMaximumPoolSize(2); // Only need 1 connection for migration

        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(ds)
                    .locations("classpath:db/initialscript")
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();
            log.info("✅ Migrations completed successfully for schema: {}", schemaName);

        } catch (Exception e) {
            log.error("❌ Migration failed for schema: {}", schemaName, e);
            throw new RuntimeException("Migration failed for schema: " + schemaName, e);

        } finally {
            log.debug("🔚 Closing HikariDataSource");
            ds.close();
        }
        // Register new, long-lived pool for JPA/Hibernate
       // tenantManager.registerTenantDataSource(schemaName, jdbcUrl, username, password);
    }
}
