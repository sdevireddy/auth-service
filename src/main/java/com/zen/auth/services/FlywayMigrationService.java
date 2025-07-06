package com.zen.auth.services;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariDataSource;

@Service
public class FlywayMigrationService {
	public void runMigrations(String schemaName) {

	    HikariDataSource ds = new HikariDataSource();
	    ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + schemaName);
	    ds.setUsername("root");
	    ds.setPassword("password");
	    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    ds.setMaximumPoolSize(4); // Only need 1 connection for migration

	    try {
	        Flyway.configure()
	            .dataSource(ds)
	            .locations("classpath:db/initialscript") // SQL files for migration
	            .baselineOnMigrate(true)
	            .load()
	            .migrate();

	        System.out.println("✅ Migrations completed for schema: " + schemaName);

	    } catch (Exception e) {
	        System.err.println("❌ Migration failed for schema: " + schemaName);
	        e.printStackTrace();
	        throw new RuntimeException("Migration failed for schema: " + schemaName, e);

	    } finally {
	        ds.close(); // ✅ Always close to release connections
	    }
	}
}
