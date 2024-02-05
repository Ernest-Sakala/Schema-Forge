package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.database.DatabaseConnection;
import com.schemaforge.forge.exception.MigrationAlreadyExistException;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.service.SchemaForeMigrationHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class MigrationExecutor {

    private static Logger log = LoggerFactory.getLogger(MigrationExecutor.class);

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

            if(!schemaForgeClientProperties.isRollbackMigrations()){
                if(schemaForgeMigrationHistory != null) {
                    if (schemaForgeMigrationHistory.getMigration().equals(migration)) {
                        throw new MigrationAlreadyExistException();
                    }
                }

                databaseConnection.database().execute(query);

                schemaForeMigrationHistoryService.insertMigrationHistory(schemaForgeMigrationHistoryModel);
            }else {

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
