package com.schemaforge.forge.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
public class MySQLSchema implements Schema {

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    public MySQLSchema() {

    }

    @Override
    public String createTable(String tableName, TableBuilder tableBuilder) {
       return SchemaUtil.createTable(tableName,tableBuilder);
    }


    @Override
    public String renameTable(String oldTableName, String newTableName) {
        return SchemaUtil.renameTable(oldTableName, newTableName);
    }

    @Override
    public String dropTable(String tableName) {
        return SchemaUtil.dropTable(tableName);
    }

    @Override
    public String addTableColumns(String tableName, TableBuilder columnDefinitions) {
        return SchemaUtil.addTableColumns(tableName,columnDefinitions);
    }

    @Override
    public SchemaBuilder dropColumns(String tableName, Consumer<TableBuilder> columnDefinitions) {
        return null;
    }


    @Override
    public String build() {
        return null;
    }
}
