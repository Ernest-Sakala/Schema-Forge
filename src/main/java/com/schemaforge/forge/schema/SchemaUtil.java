package com.schemaforge.forge.schema;

import java.util.Map;

public class SchemaUtil {

    private static StringBuilder schema;

    public static String createTable(String tableName, TableBuilder tableBuilder) {
        schema = new StringBuilder();
        schema.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        for (Map.Entry<String, String> entry : tableBuilder.columnDefinitions.entrySet()) {
            schema.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        if (schema.length() > 2) {
            schema.setLength(schema.length() - 2);
        }

        schema.append(");");

        return schema.toString();
    }

    public static String dropTable(String  tableName){
        schema = new StringBuilder();
        schema.append("DROP TABLE IF EXISTS ").append(tableName).append(" CASCADE;");
        return schema.toString();
    }

    public static String renameTable(String oldTableName, String newTableName){
        schema = new StringBuilder();
        schema.append("ALTER TABLE IF EXISTS ").append(oldTableName).append(" RENAME TO ").append(newTableName);
        return schema.toString();
    }
}
