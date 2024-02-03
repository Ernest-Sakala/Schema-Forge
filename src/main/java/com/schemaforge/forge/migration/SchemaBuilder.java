package com.schemaforge.forge.migration;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SchemaBuilder implements Schema{

    private String tableName;
    private Map<String, String> columns;


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


    @Override
    public String dropTable() {
        return null;
    }


}

