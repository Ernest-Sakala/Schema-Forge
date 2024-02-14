package com.schemaforge.forge.schema;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PostgresSQLSchema implements Schema {

    private StringBuilder schema;

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
