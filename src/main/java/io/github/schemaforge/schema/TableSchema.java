package io.github.schemaforge.schema;

/**
 * The TableSchema interface provides methods for defining a database table schema.
 * This interface allows adding various types of columns to the table.
 * @author Ernest Sakala
 */
public interface TableSchema {

    /**
     * Adds a primary key column to the database table.
     */
    void id();

    /**
     * Adds a custom-named primary key column to the database table.
     *
     * @param columnName The custom name for the primary key column.
     */
    void id(String columnName);

    /**
     * Adds a foreign key column to the database table.
     *
     * @param columnName The name of the foreign key column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addForeignId(String columnName);

    /**
     * Adds a text column to the database table.
     *
     * @param columnName The name of the text column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addTextColumn(String columnName);

    /**
     * Adds a float column to the database table.
     *
     * @param columnName The name of the float column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addFloatColumn(String columnName);

    /**
     * Adds a boolean column to the database table.
     *
     * @param columnName The name of the boolean column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addBooleanColumn(String columnName);

    /**
     * Adds a VARCHAR column to the database table.
     *
     * @param columnName The name of the string column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addStringColumn(String columnName);

    /**
     * Adds a VARCHAR column to the database table with supplied length.
     *
     * @param columnName The name of the string column to be added.
     * @param length The length of the VARCHAR column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addStringColumn(String columnName, int length);


    /**
     * Adds an enum column to the database table.
     *
     * @param columnName The name of the enum column to be added.
     * @param enumNames  An array of enum names for the column.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addEnumColumn(String columnName, String[] enumNames);

    /**
     * Adds a JSON column to the database table.
     *
     * @param columnName The name of the JSON column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addJsonColumn(String columnName);


    /**
     * Adds an Integer column to the database table.
     *
     * @param columnName The name of the Integer column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addIntegerColumn(String columnName);

    /**
     * Adds a BigInteger column to the database table.
     *
     * @param columnName The name of the BigInteger column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addBigIntegerColumn(String columnName);

    /**
     * Adds a Date column to the database table.
     *
     * @param columnName The name of the date column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addDateColumn(String columnName);

    /**
     * Adds a Time column to the database table.
     *
     * @param columnName The name of the Time column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addTimeColumn(String columnName);


    /**
     * Adds a DateTime column to the database table.
     *
     * @param columnName The name of the DateTime column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addDateTimeColumn(String columnName);

    /**
     * Adds a TimeStamp column to the database table.
     *
     * @param columnName The name of the TimeStamp column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addTimestampColumn(String columnName);


    /**
     * Adds a Binary column to the database table to store BLOB.
     *
     * @param columnName The name of the Binary column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addBinaryColumn(String columnName);


    /**
     * Adds a JSONB column to the database table.
     *
     * @param columnName The name of the JSONB column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addJsonbColumn(String columnName);

    /**
     * Adds a UUID column to the database table.
     *
     * @param columnName The name of the UUID column to be added.
     * @return A ColumnBuilder instance for further column configuration.
     */
    TableBuilder.ColumnBuilder addUuidColumn(String columnName);


}
