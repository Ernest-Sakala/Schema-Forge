package com.schemaforge.forge.db;

public interface Migration {

    void forgeSchema();

    void revert();
}
