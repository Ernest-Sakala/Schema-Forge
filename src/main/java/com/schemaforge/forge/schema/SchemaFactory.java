package com.schemaforge.forge.schema;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SchemaFactory {

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    public SchemaFactory(SchemaForgeClientProperties schemaForgeClientProperties) {
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }

    @Bean
    public Schema getDatabaseType(){

        String database = schemaForgeClientProperties.getDatabase();
        if((database == null) || (database.isEmpty())){
            throw new IllegalArgumentException("Schema forge database name should not be empty");
        }

        Schema schema = null;

        if(database.equalsIgnoreCase("MYSQL")){
            schema = new MySQLSchema();
        }else if(database.equalsIgnoreCase("POSTGRESQL")){
            schema = new PostgresSQLSchema();
        }

        return schema;
    }
}
