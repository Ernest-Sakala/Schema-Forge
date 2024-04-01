package com.schemaforge.forge.schema;

public interface ColumnSchema {

    void addTextColumn(String columnName);

    void addFloatColumn(String columnName);

    void addEnumColumn(String columnName,String [] enumNames);

    void addJsonColumn(String columnName);

    void addJsonbColumn(String columnName);

    void addUuidColumn(String columnName);

}
