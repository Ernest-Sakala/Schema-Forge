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
}
