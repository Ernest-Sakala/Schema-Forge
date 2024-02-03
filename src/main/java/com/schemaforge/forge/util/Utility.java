package com.schemaforge.forge.util;

public class Utility {

    public static String getColumnType(Class<?> fieldType) {
        // Implement your logic to map Java types to database types
        // This is a simplified example
        if (fieldType == String.class) {
            return "VARCHAR(255)";
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return "INT";
        } else if (fieldType == Long.class || fieldType == long.class) {
            return "BIGINT";
        } else {
            // Add more type mappings as needed
            return "VARCHAR(255)";
        }
    }
}
