package com.schemaforge.forge.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public class PostgresSQLSchema implements Schema {

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);


    private StringBuilder schema;

    public PostgresSQLSchema() {
    }

    @Override
    public String createTable(String tableName, TableBuilder tableBuilder) {
        return SchemaUtil.createTable(tableName,tableBuilder);
    }

    @Override
    public SchemaBuilder renameTable(String oldTableName, String newTableName) {
        return null;
    }

    @Override
    public SchemaBuilder dropTable(String tableName) {
        return null;
    }

    @Override
    public SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        return null;
    }

    @Override
    public SchemaBuilder dropColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        return null;
    }

    @Override
    public SchemaBuilder addTableColumn(String tableName, Consumer<TableBuilder> columnDefinitions) {
        return null;
    }

    @Override
    public String build() {
        return null;
    }
}
