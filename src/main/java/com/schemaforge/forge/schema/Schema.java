package com.schemaforge.forge.schema;

import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public interface Schema {

    String createTable(String tableName, TableBuilder tableBuilder);
    String renameTable(String oldTableName, String newTableName);
    String dropTable(String tableName);
    SchemaBuilder addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);
    SchemaBuilder dropColumns(String tableName, Consumer<TableBuilder> columnDefinitions);
    SchemaBuilder addTableColumn(String tableName, Consumer<TableBuilder> columnDefinitions);
    String build();
}
