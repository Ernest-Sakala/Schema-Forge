package com.schemaforge.forge.util;

public class Utility {

    // Map Java types to database types

    public static String getColumnType(Class<?> fieldType) {

        if (fieldType == String.class) {
            return "VARCHAR(255)";
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return "INT";
        } else if (fieldType == Long.class || fieldType == long.class) {
            return "BIGINT";
        } else {
            return "VARCHAR(255)";
        }
    }
}
