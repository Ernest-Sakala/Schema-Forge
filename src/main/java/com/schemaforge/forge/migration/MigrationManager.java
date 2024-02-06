package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.config.SchemaForgeCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
class MigrationManager {

    private static Logger log = LoggerFactory.getLogger(MigrationManager.class);


    private final List<MigrationContainer> migrations = new ArrayList<>();

    private MigrationExecutor migrationExecutor;

    private SchemaForgeClientProperties schemaForgeClientProperties;

    private MigrationCommandsConfiguration migrationCommandsConfiguration;

    @Autowired
    public MigrationManager(MigrationExecutor migrationExecutor, SchemaForgeClientProperties schemaForgeClientProperties, MigrationCommandsConfiguration migrationCommandsConfiguration) {
        this.migrationExecutor = migrationExecutor;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
        this.migrationCommandsConfiguration = migrationCommandsConfiguration;
    }


    public void addMigration(List<MigrationContainer> migration) {
        migrations.addAll(migration);
    }

    public void runMigrations() {

        log.info("<<<<<<<<<<<<<Running Schema Forge Migrations>>>>>>>>>>>>> : command " + schemaForgeClientProperties.getCommand() + "  value " + schemaForgeClientProperties.getValue());

        migrationCommandsConfiguration.printArguments();

        for (MigrationContainer migration : migrations) {

            String query = null;
            if(schemaForgeClientProperties.getCommand().equals(SchemaForgeCommands.REVERT)){

                query = migration.getMigration().revert();

                log.info("SCHEMA FORGE <<<<<<<<<<<<<<<<<<< Rolling back Migration >>>>>>>>>>>>>>>>> " + migration.getMigrationName() + " " + query);

            }else if(schemaForgeClientProperties.getCommand().equals(SchemaForgeCommands.MIGRATE)) {

                query =  migration.getMigration().forgeSchema();
            }

            if(query != null ) {
              try {
                  migrationExecutor.execute(migration.getMigrationName().trim(),query);
              }catch (Exception exception){
                  exception.printStackTrace();
              }
            }
        }
    }



}
