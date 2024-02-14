package com.schemaforge.forge.migration;


import org.springframework.stereotype.Component;
import javax.persistence.Table;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static com.schemaforge.forge.util.Utility.getColumnType;

@Component
public class MigrationClassGenerator {


    public void generateMigrationClass(Class<?> entityClass) {
        String packageName = "forge.database.migrations";
        String className = "Create" + entityClass.getSimpleName() + "Migration";
        String fileName = className + ".java";

        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import com.schemaforge.forge.migration.Migration;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import java.util.HashMap;\n");
        code.append("import java.util.Map;\n");
        code.append("import com.schemaforge.forge.schema.SchemaBuilder;\n\n");

        code.append("@Component\n");
        code.append("public class ").append(className).append(" implements Migration {\n\n");
        code.append("\t@Override\n");
        code.append("\tpublic String forgeSchema() {\n");

        String tableName = "";

        if (entityClass.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            tableName = tableAnnotation.name().toUpperCase();
            System.out.println("Table Name: " + tableName);
        } else {
            System.out.println("Class is not annotated with @Table");
        }

        if(tableName.equals("")) {
            code.append("\t\treturn new SchemaBuilder().createTable(\"ENTER TABLE NAME\", table -> {\n");
            addColumMethods(entityClass, code);
        }else {
            code.append("\t\treturn new SchemaBuilder().createTable(\"");
            code.append(tableName);
            code.append("\", table -> {\n");
            addColumMethods(entityClass, code);
        }

        code.append("\t\t});\n");
        code.append("\t}\n\n");

        code.append("\t@Override\n");
        code.append("\tpublic String revert() {\n");

        if(tableName.equals("")) {
            code.append("\t\treturn new SchemaBuilder().dropTable(\"").append("ENTER TABLE NAME").append("\").build();\n");
        }else {
            code.append("\t\treturn new SchemaBuilder().dropTable(\"").append(tableName).append("\");\n");
        }
        code.append("\t}\n");
        code.append("}\n");


        writeToFile("src/main/resources/forge/database/migrations",fileName, code.toString());
    }

    private void addColumMethods(Class<?> entityClass, StringBuilder code) {
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {


            String fieldName = field.getName();
            String columnName = fieldName.toUpperCase();
            String columnType = getColumnType(field.getType());

            String methodName = "";

            if(field.getType() == String.class){
                methodName = "addStringColumn";
            }else if(field.getType() == Integer.class) {
                methodName = "addIntegerColumn";
            }else {
                continue;
            }

            code.append("\t\t\t"+"table."+methodName+"(\""+columnName+"\");\n");
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
