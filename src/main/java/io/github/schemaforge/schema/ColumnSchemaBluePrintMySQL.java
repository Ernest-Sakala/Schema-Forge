package io.github.schemaforge.schema;

import io.github.schemaforge.database.DatabaseDataTypes;

class ColumnSchemaBluePrintMySQL extends ColumnSchemaBluePrint{

    @Override
    protected String idDefinition() {
        return " INT UNSIGNED AUTO_INCREMENT PRIMARY KEY ";
    }

    @Override
    protected String dateTimeDefinition() {
        return DatabaseDataTypes.DATETIME;
    }

    @Override
    protected String binaryDefinition() {
        return DatabaseDataTypes.VARBINARY;
    }
}
