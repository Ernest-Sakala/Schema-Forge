package com.schemaforge.forge.util;


import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MigrationClassGenerator {


    public void generateMigrationClass(Class<?> entityClass) {
        String packageName = "forge.database.migrations";
        String className = "Create" + entityClass.getSimpleName() + "Migration";
        String fileName = className + ".java";

        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import com.schemaforge.forge.db.Migration;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import java.util.HashMap;\n");
        code.append("import java.util.Map;\n");
        code.append("import com.schemaforge.forge.db.SchemaBuilder;\n\n");

        code.append("@Component\n");
        code.append("public class ").append(className).append(" implements Migration {\n\n");
        code.append("\t@Override\n");
        code.append("\tpublic String forgeSchema() {\n");
        code.append("\t\tMap<String, String> columnMap = new HashMap<>();\n");

        // Iterate over the fields of the entity class and add them to the columnMap
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String columnName = fieldName.toUpperCase();
            String columnType = getColumnType(field.getType());

            code.append("\t\tcolumnMap.put(\"").append(columnName).append("\", \"").append(columnType).append("\");\n");
        }

        code.append("\n\t\tString cre = new SchemaBuilder().tableName(\"").append(entityClass.getSimpleName().toUpperCase()).append("\").columns(columnMap).build();\n");
        code.append("\t\treturn cre;\n");
        code.append("\t}\n\n");

        code.append("\t@Override\n");
        code.append("\tpublic String revert() {\n");
        code.append("\t\treturn null;\n");
        code.append("\t}\n");
        code.append("}\n");

        // Write the code to a file
        writeToFile("src/main/resources/forge/database/migrations",fileName, code.toString());
    }

    private static String getColumnType(Class<?> fieldType) {
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

    private static void writeToFile(String fileName, String content) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
            System.out.println("File " + fileName + " created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String directoryPath, String fileName, String content) {
        try {
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Directory " + directoryPath + " created successfully.");
                } else {
                    System.err.println("Failed to create directory: " + directoryPath);
                    return;
                }
            }

            String filePath = directoryPath + File.separator + fileName;
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(content);
                System.out.println("File " + filePath + " created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
