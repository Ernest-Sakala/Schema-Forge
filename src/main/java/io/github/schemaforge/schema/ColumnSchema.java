package io.github.schemaforge.schema;

/**
 * The ColumnSchema class provides methods for defining columns in a database table.
 * This class serves as a blueprint for defining various types of columns.
 * @author Ernest Sakala
 */
abstract class ColumnSchema {

     /**
      * Adds a text column to the database table.
      *
      * @param columnName The name of the text column to be added.
      */
     protected abstract void addTextColumn(String columnName);

     /**
      * Adds a float column to the database table.
      *
      * @param columnName The name of the float column to be added.
      */
     protected abstract void addFloatColumn(String columnName);

     /**
      * Adds an enum column to the database table.
      *
      * @param columnName The name of the enum column to be added.
      * @param enumNames  An array of enum names for the column.
      */
     protected abstract void addEnumColumn(String columnName, String[] enumNames);

     /**
      * Adds a JSON column to the database table.
      *
      * @param columnName The name of the JSON column to be added.
      */
     protected abstract void addJsonColumn(String columnName);

     /**
      * Adds a JSONB column to the database table.
      *
      * @param columnName The name of the JSONB column to be added.
      */
     protected abstract void addJsonbColumn(String columnName);

     /**
      * Adds a UUID column to the database table.
      *
      * @param columnName The name of the UUID column to be added.
      */
     protected abstract void addUuidColumn(String columnName);


     /**
      * Adds a date column to the database table.
      *
      * @param columnName The name of the date column to be added.
      */
     protected abstract void addDateColumn(String columnName);

     /**
      * Adds a time column to the database table.
      *
      * @param columnName The name of the time column to be added.
      */
     protected abstract void addTimeColumn(String columnName);

     /**
      * Adds a date and time column to the database table.
      *
      * @param columnName The name of the date and time column to be added.
      */
     protected abstract void addDateTimeColumn(String columnName);

     /**
      * Adds a boolean column to the database table.
      *
      * @param columnName The name of the boolean column to be added.
      */
     protected abstract void addBooleanColumn(String columnName);

     /**
      * Adds a binary large object (BLOB) column to the database table.
      *
      * @param columnName The name of the BLOB column to be added.
      */
     protected abstract void addBlobColumn(String columnName);

     /**
      * Adds an integer column to the database table.
      *
      * @param columnName The name of the integer column to be added.
      */
     protected abstract void addIntegerColumn(String columnName);

     /**
      * Adds a big integer column to the database table.
      *
      * @param columnName The name of the big integer column to be added.
      */
     protected abstract void addBigIntegerColumn(String columnName);



     /**
      * Adds a double column to the database table.
      *
      * @param columnName The name of the double column to be added.
      */
     protected abstract void addDoubleColumn(String columnName);

     /**
      * Adds a string column to the database table.
      *
      * @param columnName The name of the string column to be added.
      */
     protected abstract void addStringColumn(String columnName);

     /**
      * Adds a primary key column to the database table.
      */
     protected abstract void id();

     /**
      * Adds a custom-named primary key column to the database table.
      *
      * @param columnName The custom name for the primary key column.
      */
     protected abstract void id(String columnName);

     /**
      * Adds a foreign key column to the database table.
      *
      * @param columnName The name of the foreign key column to be added.
      * @return A ColumnBuilder instance for further column configuration.
      */
     protected abstract TableBuilder.ColumnBuilder addForeignId(String columnName);

     /**
      * Sets the default value for the column.
      *
      * @param value The default value for the column.
      * @return A ColumnBuilder instance for further column configuration.
      */
     public abstract TableBuilder.ColumnBuilder defaultValue(String value);

     /**
      * Sets whether the column can contain null values.
      *
      * @param nullable True if the column can contain null values, false otherwise.
      * @return A ColumnBuilder instance for further column configuration.
      */
     public abstract TableBuilder.ColumnBuilder nullable(boolean nullable);


     public abstract TableBuilder.ColumnBuilder unique(boolean unique);

}
