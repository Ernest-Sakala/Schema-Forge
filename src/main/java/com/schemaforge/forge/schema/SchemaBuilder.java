package com.schemaforge.forge.schema;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

@Component
public class SchemaBuilder {

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    private StringBuilder schema;
    private final Schema schemaType;

    public SchemaBuilder() {
        schemaType = this.getDatabaseType();
    }


    public SchemaBuilder schemaBuilder(){
        return this;
    }


    public String createTable(String tableName, Consumer<TableBuilder> columnDefinitions) {
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        return schemaType.createTable(tableName,tableBuilder);
    }



    public String renameTable(String oldTableName, String newTableName) {
        return schemaType.renameTable(newTableName, oldTableName);
    }



    public String dropTable(String tableName) {
        return schemaType.dropTable(tableName);
    }



    public SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("ALTER TABLE IF EXIST ").append(tableName).append(" ADD COLUMN").append(" ");
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        schema.append(tableBuilder.addColumns()).append(";");
        log.info("ALTER TABLE SCHEMA FORGE >>>>" + schema);
        return this;
    }


    public SchemaBuilder dropColumns(String tableName, Consumer<TableBuilder> columnDefinitions){
        schema = new StringBuilder();
        schema.append("ALTER TABLE ").append(tableName).append(" ");
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        schema.append(tableBuilder.dropColumns()).append(";");
        log.info("ALTER TABLE SCHEMA FORGE >>>>" + schema);
        return this;
    }



    public SchemaBuilder addTableColumn(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("ALTER TABLE ADD COLUMN ").append(tableName).append(" ");
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        schema.append(tableBuilder.addColumns()).append(";");
        return this;
    }


    private Schema getDatabaseType() {
        try {
            StringBuilder jsonContent = new StringBuilder();

            FileReader reader = new FileReader("src/main/resources/forge.json");

            int character;
            while ((character = reader.read()) != -1) {
                jsonContent.append((char) character);
            }
            reader.close();

            String jsonString = jsonContent.toString();

            String database = extractValue(jsonString, "database");

            if(database.isEmpty()){
                throw new IllegalArgumentException("Schema forge database type should not be empty");
            }

            Schema schema = null;

            if(database.equalsIgnoreCase("MYSQL")){
                log.info("MYSQL SCHEMA TO BE USED");
                schema = new MySQLSchema();
            }else if(database.equalsIgnoreCase("POSTGRESQL")){
                log.info("POSTGRESQL SCHEMA TO BE USED");
                schema = new PostgresSQLSchema();
            }

            return schema;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractValue(String jsonString, String key) {
        int startIndex = jsonString.indexOf("\"" + key + "\"") + key.length() + 4;
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }


    public String build() {
        return schema.toString();
    }




}

