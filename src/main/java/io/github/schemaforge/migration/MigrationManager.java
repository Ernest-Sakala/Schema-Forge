package io.github.schemaforge.migration;

import io.github.schemaforge.config.SchemaForgeClientProperties;
import io.github.schemaforge.config.SchemaForgeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MigrationManager {

    private static final Logger log = LoggerFactory.getLogger(MigrationManager.class);


    private final List<MigrationContainer> migrations = new ArrayList<>();

    private final MigrationExecutor migrationExecutor;

    private final SchemaForgeClientProperties schemaForgeClientProperties;


    @Autowired
    public MigrationManager(MigrationExecutor migrationExecutor, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.migrationExecutor = migrationExecutor;
        this.schemaForgeClientProperties = schemaForgeClientProperties;

    }


    public void addMigration(List<MigrationContainer> migration) {
        migrations.addAll(migration);
    }

    public boolean runMigrations() {

        log.info("<<<<<<<<<<<<<Running Schema Forge Migrations>>>>>>>>>>>>> : command " + schemaForgeClientProperties.getCommand() + "  value " + schemaForgeClientProperties.getValue());

        boolean running  = false;


        for (MigrationContainer migration : migrations) {

            running = true;

            String query = null;
            if(schemaForgeClientProperties.getCommand().equals(SchemaForgeConstants.REVERT)){

                query = migration.getMigration().revert();

                log.info("SCHEMA FORGE <<<<<<<<<<<<<<<<<<< Rolling back Migration >>>>>>>>>>>>>>>>> {} {}", migration.getMigrationName(), query);

            }else if(schemaForgeClientProperties.getCommand().equals(SchemaForgeConstants.MIGRATE)) {

                query =  migration.getMigration().forgeSchema();


                log.info("SCHEMA FORGE <<<<<<<<<<<<<<<<<<Schema forge migrating Migration >>>>>>>>>>>>>>>>> {} {}", migration.getMigrationName(), query);

            }

            if(query != null ) {
              try {
                  migrationExecutor.execute(migration.getMigrationName().trim(),query);
              }catch (Exception exception){
                  exception.printStackTrace();
              }
            }
        }


        return true;
    }



}
