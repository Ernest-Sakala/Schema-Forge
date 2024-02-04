package com.schemaforge.forge.schema;

import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class SchemaBuilder implements Schema {

    private String tableName;
    private Map<String, String> columns;

    private StringBuilder schema;


    @Override
    public SchemaBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public SchemaBuilder schemaBuilder(){
        return this;
    }

    @Override
    public SchemaBuilder columns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public String createTable() {

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

        for (Map.Entry<String, String> column : columns.entrySet()) {
            createTableQuery += column.getKey() + " " + column.getValue() + ", ";
        }

        createTableQuery = createTableQuery.substring(0, createTableQuery.length() - 2);
        createTableQuery += ")";

        System.out.println("Table created: " + createTableQuery);

        return createTableQuery;
    }


    public SchemaBuilder createTable(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("CREATE TABLE ").append(tableName).append(" (");
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        schema.append(tableBuilder.build()).append(");");
        return this;
    }



    @Override
    public String tableAddColumns() {

        String alterTableQuery = "ALTER TABLE  " + tableName ;

        for (Map.Entry<String, String> column : columns.entrySet()) {
            alterTableQuery +=  " ADD " + column.getKey() + " " + column.getValue() + ", ";
        }

        alterTableQuery = alterTableQuery.substring(0, alterTableQuery.length() - 2);

        System.out.println("Table alter query forge : " + alterTableQuery);

        return alterTableQuery;
    }


    public String build() {
        return schema.toString();
    }


    @Override
    public String dropTable() {
        return null;
    }



//    public static class TableBuilder {
//        private Map<String, String> columns;
//
//        public TableBuilder() {
//            this.columns = new HashMap<>();
//        }
//
//        public Map.Entry<String, String> addDoubleColumn(String columnName) {
//            String dataType = DatabaseDataTypes.DECIMAL;
//            columns.put(columnName, dataType);
//            return new AbstractMap.SimpleEntry<>(columnName, dataType);
//        }
//
//
//        public String build() {
//            StringBuilder columnDefinitions = new StringBuilder();
//            for (Map.Entry<String, String> entry : columns.entrySet()) {
//                columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
//            }
//            // Remove the trailing comma and space
//            if (columnDefinitions.length() > 2) {
//                columnDefinitions.setLength(columnDefinitions.length() - 2);
//            }
//            return columnDefinitions.toString();
//        }
//    }
//

}

