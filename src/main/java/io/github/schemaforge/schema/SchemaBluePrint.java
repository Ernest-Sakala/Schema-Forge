package io.github.schemaforge.schema;

import java.util.function.Consumer;

/**
 * SchemaBluePrint provides methods for schema migrations.
 *
 * @author Ernest Sakala
 *
 */
public interface SchemaBluePrint {

    /**
     * Creates a new table in the database.
     *
     * @param tableName The name of the table to be created.
     * @param columnDefinitions A Consumer that defines the columns of the table using a TableBuilder.
     * @return A string representing the SQL query for creating the table.
     */
    String createTable(String tableName, Consumer<TableBuilder> columnDefinitions);

    /**
     * Renames an existing table in the database.
     *
     * @param oldTableName The current name of the table.
     * @param newTableName The new name for the table.
     * @return A string representing the SQL query for renaming the table.
     */
    String renameTable(String oldTableName, String newTableName);

    /**
     * Drops an existing table from the database.
     *
     * @param tableName The name of the table to be dropped.
     * @return A string representing the SQL query for dropping the table.
     */
    String dropTable(String tableName);

    /**
     * Adds new columns to an existing table in the database.
     *
     * @param tableName The name of the table to which columns are to be added.
     * @param columnDefinitions A Consumer that defines the columns to be added using a TableBuilder.
     * @return A string representing the SQL query for adding columns to the table.
     */
    String addTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);

    /**
     * Drops existing columns from a table in the database.
     *
     * @param tableName The name of the table from which columns are to be dropped.
     * @param columnDefinitions A Consumer that defines the columns to be dropped using a TableBuilder.
     * @return A string representing the SQL query for dropping columns from the table.
     */
    String dropTableColumns(String tableName, Consumer<TableBuilder> columnDefinitions);

}
