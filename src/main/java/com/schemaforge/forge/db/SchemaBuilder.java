package com.schemaforge.forge.db;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchemaBuilder {

    private String tableName;
    private Map<String, String> columns;

    public SchemaBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SchemaBuilder columns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }

    public String build() {

        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

        for (Map.Entry<String, String> column : columns.entrySet()) {
            createTableQuery += column.getKey() + " " + column.getValue() + ", ";
        }

        createTableQuery = createTableQuery.substring(0, createTableQuery.length() - 2);
        createTableQuery += ")";

        System.out.println("Table created: " + createTableQuery);

        return createTableQuery;
    }
}

