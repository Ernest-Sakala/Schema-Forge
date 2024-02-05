package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
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

    @Autowired
    public MigrationManager(MigrationExecutor migrationExecutor, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.migrationExecutor = migrationExecutor;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }


    public void addMigration(List<MigrationContainer> migration) {
        migrations.addAll(migration);
    }

    public void runMigrations() {

        for (MigrationContainer migration : migrations) {

            String query;
            if(schemaForgeClientProperties.isRollbackMigrations()){

                query = migration.getMigration().revert();

                log.info("SCHEMA FORGE <<<<<<<<<<<<<<<<<<< Rolling back Migration >>>>>>>>>>>>>>>>> " + migration.getMigrationName() + " " + query);
            }else {
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
