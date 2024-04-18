package io.github.schemaforge.schema;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

@Component
public class SchemaBuilder implements SchemaBluePrint{

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    private StringBuilder schema;
    private final Schema schemaType;

    public SchemaBuilder() {
        schemaType = this.getDatabaseType();
    }


    public SchemaBuilder schemaBuilder(){
        return this;
    }

    @Override
    public String createTable(String tableName, Consumer<TableBuilder> columnDefinitions) {
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        return schemaType.createTable(tableName,tableBuilder);
    }


    @Override
    public String renameTable(String oldTableName, String newTableName) {
        return schemaType.renameTable(newTableName, oldTableName);
    }


    @Override
    public String dropTable(String tableName) {
        return schemaType.dropTable(tableName);
    }


    @Override
    public String addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        return schemaType.addTableColumns(tableName, tableBuilder);
    }

    @Override
    public String dropTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions){
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        return schemaType.dropTableColumns(tableName,tableBuilder);
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

