package com.schemaforge.forge.service;

import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.lang.reflect.Field;

import static com.schemaforge.forge.util.Utility.getColumnType;

@Service
public class SchemaForeMigrationHistoryService {


    private final DatabaseConnection databaseConnection;


    @Autowired
    public SchemaForeMigrationHistoryService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
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
            String columnType = getColumnType(field.getType());
            createTableQuery.append(columnName).append(" ").append(columnType).append(", ");
        }

        createTableQuery = new StringBuilder(createTableQuery.substring(0, createTableQuery.length() - 2));
        createTableQuery.append(")");

        System.out.println("History table Create Query : " + createTableQuery);

        try {
            databaseConnection.database().execute(createTableQuery.toString());
        }catch (DataAccessException dataAccessException){
            dataAccessException.printStackTrace();
        }

    }

    public void insertMigrationHistory(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel){

        String sql = "INSERT INTO your_table (id, version, description, type, script, createddate, modifieddate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        databaseConnection.database().
    }
}
