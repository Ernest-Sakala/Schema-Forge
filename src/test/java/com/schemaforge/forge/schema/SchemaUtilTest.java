package com.schemaforge.forge.schema;

import io.github.schemaforge.schema.SchemaUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SchemaUtilTest {
    private final String tableName = "test_table";

    @Test
    void dropTableTest(){
       String query = SchemaUtil.dropTable(tableName);
       Assertions.assertEquals("DROP TABLE IF EXISTS test_table CASCADE;", query);
    }

    @Test
    void testRenameTable() {
        String newTableName = "new_table";
        Assertions.assertEquals("ALTER TABLE IF EXISTS " + tableName + " RENAME TO " + newTableName, SchemaUtil.renameTable(tableName, newTableName));
    }
}
