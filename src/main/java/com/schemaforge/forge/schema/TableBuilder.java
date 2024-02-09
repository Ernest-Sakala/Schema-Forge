package com.schemaforge.forge.schema;


import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TableBuilder {
    private Map<String, String> columnDefinitions;

    private Stack<String> columnNamesStack;

    private List<String> columnNames;

    private ColumnBuilder columnBuilder;

    public TableBuilder() {
       this.columnDefinitions = new HashMap<>();
       this.columnNames = new ArrayList<>();
       this.columnNamesStack = new Stack<>();
        this.columnBuilder = new ColumnBuilder();
    }

    public ColumnBuilder addDoubleColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addDoubleColumn(columnName);
        return columnBuilder;
    }


    public ColumnBuilder addStringColumn(String columnName) {
        checkColumnValidity(columnName);
        columnDefinitions.put(columnName, DatabaseDataTypes.VARCHAR);
        columnBuilder.addStringColumn(columnName);
        return this.columnBuilder;
    }



    /**
     * @author Ernest Sakala
     * @return void
     * @param columnName
     * add column name to list of column names
     *
     ***/

    public TableBuilder columnName(String columnName) {
        checkColumnValidity(columnName);
        columnNames.add(columnName);
        return this;
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

    protected String renameTable() {
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



    // Inner class for column building
    public class ColumnBuilder {
        private Stack<String> columnOrder;

        public ColumnBuilder() {
            this.columnOrder = new Stack<>();
        }

        private void addDoubleColumn(String columnName) {
            checkColumnValidity(columnName);
            columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL);
            columnOrder.push(columnName);
        }

        private void addStringColumn(String columnName) {
            checkColumnValidity(columnName);
            columnDefinitions.put(columnName, "VARCHAR");
            columnOrder.push(columnName);
        }

        public ColumnBuilder nullable(boolean nullable) {
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

           String columnDefinition = nullable ?  columnDefinitions.get(columnName).concat(" NULL") :  columnDefinitions.get(columnName).concat(" NOT NULL ");
           columnDefinitions.put(columnName,columnDefinition);

           return this;
        }

        public ColumnBuilder defaultValue(String value) {

            // perfoming validation to accept other data types
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition = columnDefinitions.get(columnName).concat(" DEFAULT ").concat("'").concat(value).concat("'");

            columnDefinitions.put(columnName,columnDefinition);

            return this;
        }



        private void checkColumnValidity(String columnName) {
            if (columnName == null || columnName.isEmpty()) {
                throw new IllegalArgumentException("Column Name Cannot be null or empty");
            }
            if (!columnName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                throw new IllegalArgumentException("Column name should only contain letters, digits, or underscores and must start with a letter.");
            }
        }
    }



}
