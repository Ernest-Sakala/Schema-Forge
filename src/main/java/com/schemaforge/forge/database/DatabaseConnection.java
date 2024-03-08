package com.schemaforge.forge.database;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.schema.Schema;
import com.schemaforge.forge.schema.MySQLSchema;
import com.schemaforge.forge.schema.PostgresSQLSchema;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.Map;

@Component
public class DatabaseConnection {

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    public DatabaseConnection(SchemaForgeClientProperties schemaForgeClientProperties) {
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }


    public DataSource getDataSource() {

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        if(schemaForgeClientProperties.getUrl().contains("mysql")){
            dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        }else if(schemaForgeClientProperties.getUrl().contains("postgresql")){
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
        }

        dataSourceBuilder.url(schemaForgeClientProperties.getUrl());
        dataSourceBuilder.username(schemaForgeClientProperties.getUsername());
        dataSourceBuilder.password(schemaForgeClientProperties.getPassword());
        return dataSourceBuilder.build();
    }



    public Schema getDatabaseType(){

        Schema database = null;
        if(schemaForgeClientProperties.getUrl().contains("mysql")){
            database = new MySQLSchema();
        }else if(schemaForgeClientProperties.getUrl().contains("postgresql")){
            database = new PostgresSQLSchema();
        }

        return database;
    }


    public JdbcTemplate database(){
         return new JdbcTemplate(getDataSource());
    }

}

