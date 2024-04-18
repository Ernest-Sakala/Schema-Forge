package io.github.schemaforge.schema;

import io.github.schemaforge.database.DatabaseDataTypes;

class ColumnSchemaBluePrintPostgresSQL extends ColumnSchemaBluePrint{


    @Override
    protected String idDefinition() {
        return  " BIGINT GENERATED ALWAYS AS IDENTITY ";
    }

    @Override
    protected String dateTimeDefinition() {
        return DatabaseDataTypes.TIMESTAMP;
    }

    @Override
    protected String binaryDefinition() {
        return DatabaseDataTypes.BYTEA;
    }
}
