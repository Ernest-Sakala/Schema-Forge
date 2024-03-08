package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.util.EntityClassScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
class MigrationInitializer {

    private final MigrationManager migrationManager;

    private final MigrationClassReader migrationClassReader;

    private final EntityClassScanner entityClassScanner;

    private final MigrationClassGenerator migrationClassGenerator;

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    @Autowired
    public MigrationInitializer(MigrationManager migrationManager, MigrationClassReader migrationClassReader, EntityClassScanner entityClassScanner, MigrationClassGenerator migrationClassGenerator, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.migrationManager = migrationManager;
        this.migrationClassReader = migrationClassReader;
        this.entityClassScanner = entityClassScanner;
        this.migrationClassGenerator = migrationClassGenerator;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
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

