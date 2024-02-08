package com.schemaforge.forge.migration;

import com.schemaforge.forge.util.EntityClassScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class MigrationInitializer {

    private final MigrationManager migrationManager;

    private final MigrationClassReader migrationClassReader;

    private final EntityClassScanner entityClassScanner;

    private final MigrationClassGenerator migrationClassGenerator;

    @Autowired
    public MigrationInitializer(MigrationManager migrationManager, MigrationClassReader migrationClassReader, EntityClassScanner entityClassScanner, MigrationClassGenerator migrationClassGenerator) {
        this.migrationManager = migrationManager;
        this.migrationClassReader = migrationClassReader;
        this.entityClassScanner = entityClassScanner;
        this.migrationClassGenerator = migrationClassGenerator;
    }

    @PostConstruct
    public void migrate() {

        System.out.println("<<<<<<<<<<<<<<<<<<<< Schema Forge Initializing migrations >>>>>>>>>>>>>>>>>");

        for(Class<?> entityClass : entityClassScanner.getEntityClasses()){
            migrationClassGenerator.generateMigrationClass(entityClass);
        }

        migrationManager.addMigration(migrationClassReader.getMigrationClasses());

        migrationManager.runMigrations();
    }
}

