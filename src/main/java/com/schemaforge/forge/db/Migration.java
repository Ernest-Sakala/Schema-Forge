package com.schemaforge.forge.db;

import java.util.Map;

public interface Migration {

    String forgeSchema();

    String revert();

}
