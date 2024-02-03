package com.schemaforge.forge.migration;

import com.schemaforge.forge.database.DatabaseConnection;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.service.SchemaForeMigrationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class ExecuteMigration {

    private final DatabaseConnection databaseConnection;

    private final SchemaForeMigrationHistoryService schemaForeMigrationHistoryService;

    @Autowired
    public ExecuteMigration(DatabaseConnection databaseConnection, SchemaForeMigrationHistoryService schemaForeMigrationHistoryService) {
        this.databaseConnection = databaseConnection;
        this.schemaForeMigrationHistoryService = schemaForeMigrationHistoryService;
    }

    @Transactional
    public void execute(String migration, String query){

        try{

            databaseConnection.database().execute(query);

            SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = new SchemaForgeMigrationHistoryModel();

            schemaForgeMigrationHistoryModel.setDescription(migration);
            schemaForgeMigrationHistoryModel.setScript(migration+".java");
            schemaForgeMigrationHistoryModel.setVersion(1);
            schemaForgeMigrationHistoryModel.setType("JAVA");
            schemaForgeMigrationHistoryModel.setCreatedDate(LocalDateTime.now());
            schemaForgeMigrationHistoryModel.setModifiedDate(LocalDateTime.now());

            schemaForeMigrationHistoryService.


        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
