package com.schemaforge.forge.schema;


import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class TableBuilder {
    private Map<String, String> columns;

    public TableBuilder() {
        this.columns = new HashMap<>();
    }

    public Map.Entry<String, String> addDoubleColumn(String columnName) {
        String dataType = DatabaseDataTypes.DECIMAL;
        columns.put(columnName, dataType);
        return new AbstractMap.SimpleEntry<>(columnName, dataType);
    }


    public Map.Entry<String, String> addStringColumn(String columnName) {
        String dataType = DatabaseDataTypes.VARCHAR;
        columns.put(columnName, dataType);
        return new AbstractMap.SimpleEntry<>(columnName, dataType);
    }


    public String build() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        // Remove the trailing comma and space
        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }
}
