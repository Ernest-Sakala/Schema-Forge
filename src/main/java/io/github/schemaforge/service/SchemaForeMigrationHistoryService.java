package io.github.schemaforge.service;

import io.github.schemaforge.database.DatabaseConnection;
import io.github.schemaforge.model.SchemaForgeMigrationHistoryModel;
import io.github.schemaforge.repository.SchemaForeMigrationHistoryRepository;
import io.github.schemaforge.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.lang.reflect.Field;

@Service
public class SchemaForeMigrationHistoryService {


    private final DatabaseConnection databaseConnection;


    private final SchemaForeMigrationHistoryRepository schemaForeMigrationHistoryRepository;


    @Autowired
    public SchemaForeMigrationHistoryService(DatabaseConnection databaseConnection, SchemaForeMigrationHistoryRepository schemaForeMigrationHistoryRepository) {
        this.databaseConnection = databaseConnection;
        this.schemaForeMigrationHistoryRepository = schemaForeMigrationHistoryRepository;
    }


    @PostConstruct
    private void createHistoryTable(){

        Class<?> clazz = SchemaForgeMigrationHistoryModel.class;

        // Get declared fields
        Field[] fields = clazz.getDeclaredFields();

        String tableName = "";

        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            tableName = tableAnnotation.name();
            System.out.println("Table Name: " + tableName);
        } else {
            System.out.println("Class is not annotated with @Table");
        }

        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        for (Field field : fields) {
            String fieldName = field.getName();
            String columnName = fieldName.toUpperCase();
            String columnType = Utility.getColumnType(field.getType());

            if(fieldName.equals("id")){
                createTableQuery.append(columnName).append(" ").append("SERIAL PRIMARY KEY").append(", ");
            }else {
                createTableQuery.append(columnName).append(" ").append(columnType).append(", ");
            }

        }

        createTableQuery.append("UNIQUE (migration)");
        createTableQuery.append(")");

        try {
            databaseConnection.database().execute(createTableQuery.toString());
        }catch (DataAccessException dataAccessException){
            dataAccessException.printStackTrace();
        }

    }

    public void insertMigrationHistory(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel){
        schemaForeMigrationHistoryRepository.insertMigrationHistory(schemaForgeMigrationHistoryModel);
    }


    public SchemaForgeMigrationHistoryModel checkMigrationExists(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel){
       return schemaForeMigrationHistoryRepository.findByMigration(schemaForgeMigrationHistoryModel);
    }


    @Transactional
    public SchemaForgeMigrationHistoryModel checkMigrationExists(String migration){
        return schemaForeMigrationHistoryRepository.findByMigration(migration);
    }

    public int deleteByMigration(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel){
        return schemaForeMigrationHistoryRepository.deleteByMigration(schemaForgeMigrationHistoryModel);
    }


}
