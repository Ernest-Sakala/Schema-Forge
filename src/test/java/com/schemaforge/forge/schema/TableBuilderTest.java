package com.schemaforge.forge.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableBuilderTest {


    private TableBuilder tableBuilder;

    @BeforeEach
    void setUp() {
        tableBuilder = new TableBuilder();
    }

    @Test
    void testAddDoubleColumn() {
        tableBuilder.addDoubleColumn("test_column");
        assertTrue(tableBuilder.createTable().contains("test_column DECIMAL"));
    }

    @Test
    void testAddStringColumn() {
        tableBuilder.addStringColumn("test_column");
        assertTrue(tableBuilder.createTable().contains("test_column VARCHAR"));
    }

    @Test
    void testAddBigIntColumn() {
        tableBuilder.addBigIntColumn("test_column");
        assertTrue(tableBuilder.createTable().contains("test_column BIGINT"));
    }

    @Test
    void testAddBooleanColumn() {
        tableBuilder.addBooleanColumn("test_column");
        assertTrue(tableBuilder.createTable().contains("test_column BOOLEAN"));
    }

    @Test
    void testAddBigTimeStampColumn() {
        tableBuilder.addBigTimeStampColumn("test_column");
        assertTrue(tableBuilder.createTable().contains("test_column TIMESTAMP"));
    }


    @Test
    void testAddColumn_NullColumnName() {
        assertThrows(NullPointerException.class, () -> tableBuilder.addDoubleColumn(null));
    }

    @Test
    void testAddColumn_EmptyColumnName() {
        assertThrows(IllegalArgumentException.class, () -> tableBuilder.addDoubleColumn(""));
    }

    @Test
    void testAddColumn_InvalidColumnName() {
        assertThrows(IllegalArgumentException.class, () -> tableBuilder.addDoubleColumn("123_invalid"));
    }




    @Test
    void testNullable() {
        tableBuilder.addStringColumn("test_column").nullable(true);
        assertTrue(tableBuilder.createTable().contains("test_column VARCHAR NULL"));
    }

    @Test
    void testDefaultValue() {
        tableBuilder.addStringColumn("test_column").defaultValue("default_value");
        assertTrue(tableBuilder.createTable().contains("test_column VARCHAR DEFAULT 'default_value'"));
    }



}