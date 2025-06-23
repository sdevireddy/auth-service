package com.zen.auth.services;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariDataSource;

@Service
public class FlywayMigrationService {
	public void runMigrations(String schemaName) {

		/*
		 * Flyway flyway = Flyway.configure() .dataSource("jdbc:mysql://localhost:3306/"
		 * + schemaName, "root", "password") .locations("classpath:db/initialscript") //
		 * SQL files .baselineOnMigrate(true) .load(); flyway.migrate(); }
		 */
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + schemaName);
		ds.setUsername("root");
		ds.setPassword("password");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		try {
			Flyway.configure().dataSource(ds).locations("classpath:db/initialscript") // SQL files
					.baselineOnMigrate(true).load().migrate();
		} finally {
			if (ds != null) {
				ds.close(); // ✅ Closes all connections & cleans up pool
			}
		}
	}
}
