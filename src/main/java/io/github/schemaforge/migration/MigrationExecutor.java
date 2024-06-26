package io.github.schemaforge.migration;

import io.github.schemaforge.config.SchemaForgeClientProperties;
import io.github.schemaforge.config.SchemaForgeConstants;
import io.github.schemaforge.database.DatabaseConnection;
import io.github.schemaforge.model.SchemaForgeMigrationHistoryModel;
import io.github.schemaforge.service.SchemaForeMigrationHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class MigrationExecutor {

    private static final Logger log = LoggerFactory.getLogger(MigrationExecutor.class);

    private final DatabaseConnection databaseConnection;

    private final SchemaForeMigrationHistoryService schemaForeMigrationHistoryService;

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    @Autowired
    public MigrationExecutor(DatabaseConnection databaseConnection, SchemaForeMigrationHistoryService schemaForeMigrationHistoryService, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.databaseConnection = databaseConnection;
        this.schemaForeMigrationHistoryService = schemaForeMigrationHistoryService;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }

    @Transactional
    public void execute(String migration, String query){

        try{

            migration = migration+".java".trim();

            SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = new SchemaForgeMigrationHistoryModel();

            schemaForgeMigrationHistoryModel.setDescription(migration + " By Schema Forge");
            schemaForgeMigrationHistoryModel.setMigration(migration);
            schemaForgeMigrationHistoryModel.setVersion(1);
            schemaForgeMigrationHistoryModel.setType("JAVA");
            schemaForgeMigrationHistoryModel.setCreatedDate(LocalDateTime.now());
            schemaForgeMigrationHistoryModel.setModifiedDate(LocalDateTime.now());


            SchemaForgeMigrationHistoryModel schemaForgeMigrationHistory = schemaForeMigrationHistoryService.checkMigrationExists(schemaForgeMigrationHistoryModel);

            if(schemaForgeClientProperties.getCommand().equals(SchemaForgeConstants.MIGRATE)){

                boolean executeQuery = true;
                if(schemaForgeMigrationHistory != null) {
                    if (schemaForgeMigrationHistory.getMigration().equals(migration)) {
                        executeQuery = false;
                    }
                }

                if(executeQuery) {
                    databaseConnection.database().execute(query);
                    schemaForeMigrationHistoryService.insertMigrationHistory(schemaForgeMigrationHistoryModel);
                }


            }else if(schemaForgeClientProperties.getCommand().equals(SchemaForgeConstants.REVERT)){

                databaseConnection.database().execute(query);

                int deleted = schemaForeMigrationHistoryService.deleteByMigration(schemaForgeMigrationHistory);

                if(deleted > 0){
                    log.info("Removed Migration From Schema History " + schemaForgeMigrationHistory.getMigration());
                }

            }


        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
