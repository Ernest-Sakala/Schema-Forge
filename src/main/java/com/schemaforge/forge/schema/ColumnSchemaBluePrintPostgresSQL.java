package com.schemaforge.forge.schema;

class ColumnSchemaBluePrintPostgresSQL extends ColumnSchemaBluePrint{


    @Override
    protected String idDefinition() {
        return  " BIGINT GENERATED ALWAYS AS IDENTITY ";
    }
}
