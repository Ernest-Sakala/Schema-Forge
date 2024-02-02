package com.schemaforge.forge.db;

import com.schemaforge.forge.util.EntityClassScanner;
import com.schemaforge.forge.util.MigrationClassGenerator;
import com.schemaforge.forge.util.MigrationClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MigrationInitializer {

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

        for(Class<?> entityClass : entityClassScanner.getEntityClasses()){
            migrationClassGenerator.generateMigrationClass(entityClass);
        }

        migrationManager.addMigration(migrationClassReader.getMigrationClasses());

        System.out.println("<<<<<<<<<<<<<<<<<<<< Schema Forge Initializing migrations >>>>>>>>>>>>>>>>>");

        migrationManager.runMigrations();


    }
}

