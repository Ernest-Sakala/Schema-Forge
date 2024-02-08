package com.schemaforge.forge.schema;

import com.schemaforge.forge.database.DatabaseDataTypes;
import org.springframework.stereotype.Component;
import java.util.AbstractMap;
import java.util.Map;

@Component
public class SchemaBluePrint {

    public Map.Entry<String, String> addDoubleColumn(String columnName){
        return new AbstractMap.SimpleEntry<>(columnName, DatabaseDataTypes.DECIMAL);
    }
}
