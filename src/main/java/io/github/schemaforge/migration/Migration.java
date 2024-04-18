package io.github.schemaforge.migration;

public interface Migration {

    String forgeSchema();

    String revert();

}
