package com.schemaforge.forge.schema;


import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TableBuilder {
    private Map<String, String> columnDefinitions;

    private List<String> columnNames;

    public TableBuilder() {
       this.columnDefinitions = new HashMap<>();
       this.columnNames = new ArrayList<>();
    }

    public void addDoubleColumn(String columnName) {
        checkColumnValidity(columnName);
        columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL);
    }


    public void addStringColumn(String columnName) {
        checkColumnValidity(columnName);
        columnDefinitions.put(columnName, DatabaseDataTypes.VARCHAR);
    }



    /**
     * @author Ernest Sakala
     * @return void
     * @param columnName
     * add column name to list of column names
     *
     ***/

    public void columnName(String columnName) {
        checkColumnValidity(columnName);
        columnNames.add(columnName);
    }


    protected String createTable() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : this.columnDefinitions.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        // Remove the trailing comma and space
        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }


    protected String addTableColumns() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : this.columnDefinitions.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        // Remove the trailing comma and space
        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }



    protected String addColumns() {
        if (columnDefinitions.isEmpty()) {
            throw new IllegalStateException("No columns defined for table.");
        }

        StringBuilder sqlStatement = new StringBuilder();

//        // Check if table already exists

        // Add column definitions
        for (Map.Entry<String, String> entry : columnDefinitions.entrySet()) {
            sqlStatement.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }



        // Remove the trailing comma and space
         sqlStatement.setLength(sqlStatement.length() - 2);

        return sqlStatement.toString();
    }



    protected String dropColumns() {
        if (columnNames.isEmpty()) {
            throw new IllegalStateException("No columns defined for table.");
        }

        StringBuilder sqlStatement = new StringBuilder();

//        // Check if table already exists

        columnNames.forEach(column -> sqlStatement.append(" DROP COLUMN IF EXISTS ").append(column).append(", "));

        // Remove the trailing comma and space
        sqlStatement.setLength(sqlStatement.length() - 2);

        return sqlStatement.toString();
    }


    // DROP COLUMN IF EXISTS

    // Method to add a single column to an existing or new table
    public String addColumn() {

        StringBuilder sqlStatement = new StringBuilder();

        for (Map.Entry<String, String> entry : columnDefinitions.entrySet()) {
            sqlStatement.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        return sqlStatement.toString();
    }


    private void checkColumnValidity(String columnName){
        if(columnName == null){
            throw new NullPointerException("Column Name Cannot be null");
        }
        if(columnName.isEmpty()){
            throw new IllegalArgumentException("Column Name Cannot be empty");
        }

        if(!columnName.chars().allMatch(Character::isLetter)){
            throw new IllegalArgumentException("Column name should only contain letters");
        }
    }

}
