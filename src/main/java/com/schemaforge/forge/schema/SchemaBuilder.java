package com.schemaforge.forge.schema;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class SchemaBuilder implements Schema {

    private static Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    private String tableName;
    private Map<String, String> columns;

    private StringBuilder schema;


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


    public SchemaBuilder createTable(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        schema.append(tableBuilder.createTable()).append(");");
        log.info("CREATE TABLE SCHEMA FORGE >>>>" + schema);
        return this;
    }

    @Override
    public SchemaBuilder dropTable(String tableName) {
        schema = new StringBuilder();
        schema.append("DROP TABLE IF EXISTS ").append(tableName).append(" CASCADE;");
        log.info("DROP TABLE SCHEMA FORGE >>>>" + schema);
        return this;
    }



    public SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN").append(" ");
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

    public String build() {
        return schema.toString();
    }




}

