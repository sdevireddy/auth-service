package com.zen.auth.services;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

@Service
public class FlywayMigrationService {
    public void runMigrations(String schemaName) {
    	
    	
        Flyway flyway = Flyway.configure()
            .dataSource("jdbc:mysql://localhost:3306/" + schemaName, "root", "password")
            .locations("classpath:db/initialscript") // SQL files
            .baselineOnMigrate(true)
            .load();
        flyway.migrate();
    }
}

