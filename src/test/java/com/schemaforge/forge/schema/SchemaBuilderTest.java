package com.schemaforge.forge.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SchemaBuilderTest {

    private SchemaBuilder schemaBuilder;


    @BeforeEach
    void setUp() {
        schemaBuilder = new SchemaBuilder();
    }



    void testCreateTable() {
        Consumer<TableBuilder> tableBuilderConsumer = tableBuilder -> {
            tableBuilder.addStringColumn("column1");
        };
        String query = schemaBuilder.createTable("tableName", tableBuilderConsumer);
        assertTrue(query.contains("column1 VARCHAR"));
        assertTrue(query.contains("tableName"));
       assertEquals("CREATE TABLE IF NOT EXISTS tableName (column1 VARCHAR);", query);
    }




    @Test
    void testRenameTable() {
        String oldTableName = "old_table";
        String newTableName = "new_table";
        assertEquals(schemaBuilder.renameTable(oldTableName, newTableName), schemaBuilder);
        assertEquals("ALTER TABLE IF NOT EXISTS " + oldTableName + " RENAME TO " + newTableName, schemaBuilder.build());
    }

    @Test
    void testDropTable() {
        String tableName = "test_table";
        assertEquals(schemaBuilder.dropTable(tableName), schemaBuilder);
        assertEquals("DROP TABLE IF EXISTS test_table CASCADE;", schemaBuilder.build());
    }


}