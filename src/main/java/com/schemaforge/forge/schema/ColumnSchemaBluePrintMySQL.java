package com.schemaforge.forge.schema;

class ColumnSchemaBluePrintMySQL extends ColumnSchemaBluePrint{

    @Override
    protected String idDefinition() {
        return " INT UNSIGNED AUTO_INCREMENT PRIMARY KEY ";
    }
}
