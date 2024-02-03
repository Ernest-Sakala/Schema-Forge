package com.schemaforge.forge.migration;

public interface Migration {

    String forgeSchema();

    String revert();

}
