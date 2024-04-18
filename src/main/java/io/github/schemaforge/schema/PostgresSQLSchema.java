package io.github.schemaforge.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    /**
     * @param tableName name of the table
     * @param columnDefinitions columns
     * @return String
     */
    @Override
    public String dropTableColumns(String tableName, TableBuilder columnDefinitions) {
        return SchemaUtil.dropTableColumns(tableName,columnDefinitions);
    }

}
