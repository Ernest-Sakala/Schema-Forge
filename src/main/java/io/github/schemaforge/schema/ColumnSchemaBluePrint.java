package io.github.schemaforge.schema;

abstract class ColumnSchemaBluePrint {
  protected abstract String idDefinition();

  protected abstract String dateTimeDefinition();

  protected abstract String binaryDefinition();
}
