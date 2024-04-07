package com.schemaforge.forge.schema;

abstract class ColumnSchema {
     protected abstract void addTextColumn(String columnName);

     protected abstract void addFloatColumn(String columnName);

     protected abstract void addEnumColumn(String columnName,String [] enumNames);

     protected abstract void addJsonColumn(String columnName);

     protected abstract void addJsonbColumn(String columnName);

     protected abstract void addUuidColumn(String columnName);

     protected abstract void addDoubleColumn(String columnName);

     protected abstract void addStringColumn(String columnName);

     protected abstract void id();

     protected abstract void id(String columnName);

     protected abstract TableBuilder.ColumnBuilder addForeignId(String columnName);


     public abstract TableBuilder.ColumnBuilder defaultValue(String value);

     public abstract TableBuilder.ColumnBuilder nullable(boolean nullable);


}
