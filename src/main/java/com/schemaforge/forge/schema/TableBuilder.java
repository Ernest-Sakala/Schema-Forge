package com.schemaforge.forge.schema;


import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class TableBuilder {
    private Map<String, String> columns;

    public TableBuilder() {
       this.columns = new HashMap<>();
    }

    public Map.Entry<String, String> addDoubleColumn(String columnName) {
        String dataType = DatabaseDataTypes.DECIMAL;
        columns.put(columnName, dataType);
        return new AbstractMap.SimpleEntry<>(columnName, dataType);
    }


    public Map.Entry<String, String> addStringColumn(String columnName) {
        String dataType = DatabaseDataTypes.VARCHAR;
        columns.put(columnName, dataType);
        return new AbstractMap.SimpleEntry<>(columnName, dataType);
    }


    public String createTable() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        // Remove the trailing comma and space
        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }


    public String addTableColumns() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        // Remove the trailing comma and space
        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }



    public String addColumns() {
        if (columns.isEmpty()) {
            throw new IllegalStateException("No columns defined for table.");
        }

        StringBuilder sqlStatement = new StringBuilder();

//        // Check if table already exists
//        if (TableExistsChecker.tableExists(tableName)) {
//            sqlStatement.append("ALTER TABLE ").append(tableName).append(" ADD ");
//        } else {
//            sqlStatement.append("CREATE TABLE ").append(tableName).append(" (");
//        }

        // Add column definitions
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sqlStatement.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        // Remove the trailing comma and space
         sqlStatement.setLength(sqlStatement.length() - 2);
//
//        if (!TableExistsChecker.tableExists(tableName)) {
//            sqlStatement.append(");");
//        }

        return sqlStatement.toString();
    }

    // Method to add a single column to an existing or new table
    public String addColumn() {

        StringBuilder sqlStatement = new StringBuilder();

        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sqlStatement.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        return sqlStatement.toString();
    }


    private static class TableExistsChecker {
        // Replace this with your logic to check if the table already exists
        static boolean tableExists(String tableName) {
            // Placeholder logic; replace it with your actual implementation
            return false;
        }
    }



}
