package com.schemaforge.forge.db;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MigrationInitializer {

    private final MigrationManager migrationManager;

    public MigrationInitializer(MigrationManager migrationManager) {
        this.migrationManager = migrationManager;
    }

    @PostConstruct
    public void migrate() {

        System.out.println("<<<<<<<<<<<<<<<<<<<< Initializing migrations >>>>>>>>>>>>>>>>>");

        //Arrays.stream(configuredMigrations.split(",")).map(String::trim).forEach(migrationManager::addMigration);
       // migrationManager.runMigrations();

    }
}

