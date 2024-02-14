package com.schemaforge.forge.schema;

import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public interface Schema {

    String createTable(String tableName, TableBuilder tableBuilder);
    SchemaBuilder renameTable(String oldTableName, String newTableName);
    SchemaBuilder dropTable(String tableName);
    SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);
    SchemaBuilder dropColumns(String tableName, Consumer<TableBuilder> columnDefinitions);
    SchemaBuilder addTableColumn(String tableName, Consumer<TableBuilder> columnDefinitions);
    String build();
}
