package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.util.EntityClassScanner;
import com.schemaforge.forge.util.FileGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
class MigrationInitializer {

    private final MigrationManager migrationManager;

    private final MigrationClassReader migrationClassReader;

    private final EntityClassScanner entityClassScanner;

    private final MigrationClassGenerator migrationClassGenerator;


    private final FileGenerator fileGenerator;

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    @Autowired
    public MigrationInitializer(MigrationManager migrationManager, MigrationClassReader migrationClassReader, EntityClassScanner entityClassScanner, MigrationClassGenerator migrationClassGenerator, FileGenerator fileGenerator, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.migrationManager = migrationManager;
        this.migrationClassReader = migrationClassReader;
        this.entityClassScanner = entityClassScanner;
        this.migrationClassGenerator = migrationClassGenerator;
        this.fileGenerator = fileGenerator;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }

    @PostConstruct
    public void migrate() {

        System.out.println("<<<<<<<<<<<<<<<<<<<< Schema Forge Initializing migrations >>>>>>>>>>>>>>>>>");

        try {
            fileGenerator.generateFile("{" +
                    "\n" +
                    "\t" + "\"" +"database" + "\"" + ":" + "\"" + schemaForgeClientProperties.getDatabase() +"\""+
                    "\n"+
                    "}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for(Class<?> entityClass : entityClassScanner.getEntityClasses()){
            migrationClassGenerator.generateMigrationClass(entityClass);
        }

        migrationManager.addMigration(migrationClassReader.getMigrationClasses());

        migrationManager.runMigrations();
    }
}

