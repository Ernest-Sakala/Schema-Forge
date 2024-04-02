package com.schemaforge.forge.schema;

import java.util.function.Consumer;

public interface SchemaBluePrint {

    String createTable(String tableName, Consumer<TableBuilder> columnDefinitions);
    String renameTable(String oldTableName, String newTableName);
    String dropTable(String tableName);
    String addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);
    String dropTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);

}
