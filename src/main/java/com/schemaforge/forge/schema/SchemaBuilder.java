package com.schemaforge.forge.schema;


import com.schemaforge.forge.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class SchemaBuilder{

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    private StringBuilder schema;

    @Autowired
    private  DatabaseConnection databaseType;

    public SchemaBuilder() {
    }


    public SchemaBuilder schemaBuilder(){
        return this;
    }


    public String createTable(String tableName, Consumer<TableBuilder> columnDefinitions) {
        TableBuilder tableBuilder = new TableBuilder();
        columnDefinitions.accept(tableBuilder);
        String query = new PostgresSQLSchema().createTable(tableName,tableBuilder);
        log.info("CREATE TABLE SCHEMA FORGE >>>>" + query);
        return query;
    }



    public SchemaBuilder renameTable(String oldTableName, String newTableName) {
        schema = new StringBuilder();
        schema.append("ALTER TABLE IF NOT EXISTS ").append(oldTableName).append(" RENAME TO ").append(newTableName);
        log.info("CREATE TABLE SCHEMA FORGE >>>>" + schema);
        return this;
    }



    public String dropTable(String tableName) {
        schema = new StringBuilder();
        schema.append("DROP TABLE IF EXISTS ").append(tableName).append(" CASCADE;");
        log.info("DROP TABLE SCHEMA FORGE >>>>" + schema);
        return schema.toString();
    }



    public SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        schema = new StringBuilder();
        schema.append("ALTER TABLE IF EXITS ").append(tableName).append(" ADD COLUMN").append(" ");
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

