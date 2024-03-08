package com.schemaforge.forge.schema;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SchemaFactory {

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    private final MySQLSchema mySQLSchema;

    private final PostgresSQLSchema postgresSQLSchema;


    private final SchemaForgeClientProperties schemaForgeClientProperties;

    public SchemaFactory(MySQLSchema mySQLSchema, PostgresSQLSchema postgresSQLSchema, SchemaForgeClientProperties schemaForgeClientProperties) {
        this.mySQLSchema = mySQLSchema;
        this.postgresSQLSchema = postgresSQLSchema;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }


    public Schema getDatabaseType(){

        log.info(schemaForgeClientProperties.toString());

        String database = schemaForgeClientProperties.getDatabase();
        if((database == null) || (database.isEmpty())){
            throw new IllegalArgumentException("Schema forge database name should not be empty");
        }

        Schema schema = null;

        if(database.equalsIgnoreCase("MYSQL")){
            schema = this.mySQLSchema;
        }else if(database.equalsIgnoreCase("POSTGRESQL")){
            schema = this.postgresSQLSchema;
        }

        return schema;
    }
}
