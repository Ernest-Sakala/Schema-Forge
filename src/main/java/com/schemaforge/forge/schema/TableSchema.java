package com.schemaforge.forge.schema;

public interface TableSchema {

    TableBuilder.ColumnBuilder addTextColumn(String columnName);

    TableBuilder.ColumnBuilder addFloatColumn(String columnName);

    TableBuilder.ColumnBuilder addBooleanColumn(String columnName);

    TableBuilder.ColumnBuilder addStringColumn(String columnName);

    TableBuilder.ColumnBuilder addEnumColumn(String columnName, String [] enumNames);

    TableBuilder.ColumnBuilder addJsonColumn(String columnName);

    TableBuilder.ColumnBuilder addJsonbColumn(String columnName);

    TableBuilder.ColumnBuilder addUuidbColumn(String columnName);
}
