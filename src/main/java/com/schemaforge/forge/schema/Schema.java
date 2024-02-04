package com.schemaforge.forge.schema;

import java.util.Map;

public interface Schema {

    SchemaBuilder tableName(String tableName);

    SchemaBuilder columns(Map<String, String> columns);

    SchemaBuilder schemaBuilder();

    String createTable();

    String dropTable();


    String tableAddColumns();
}
