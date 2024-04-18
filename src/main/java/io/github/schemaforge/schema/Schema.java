package io.github.schemaforge.schema;

import org.springframework.stereotype.Component;

@Component
public interface Schema {

    String createTable(String tableName, TableBuilder tableBuilder);
    String renameTable(String oldTableName, String newTableName);
    String dropTable(String tableName);
    String addTableColumns(String tableName, TableBuilder columnDefinitions);
    String dropTableColumns(String tableName, TableBuilder columnDefinitions);

}
