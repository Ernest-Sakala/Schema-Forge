package io.github.schemaforge.database;

import io.github.schemaforge.schema.MySQLSchema;
import io.github.schemaforge.config.SchemaForgeClientProperties;
import io.github.schemaforge.schema.PostgresSQLSchema;
import io.github.schemaforge.schema.Schema;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class DatabaseConnection {

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    public DatabaseConnection(SchemaForgeClientProperties schemaForgeClientProperties) {
        this.schemaForgeClientProperties = schemaForgeClientProperties;
    }


    public DataSource getDataSource() {

        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();

        if(schemaForgeClientProperties.getUrl().contains("mysql")){
            singleConnectionDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }else if(schemaForgeClientProperties.getUrl().contains("postgresql")){
            singleConnectionDataSource.setDriverClassName("org.postgresql.Driver");
        }

        singleConnectionDataSource.setUrl(schemaForgeClientProperties.getUrl());
        singleConnectionDataSource.setUsername(schemaForgeClientProperties.getUsername());
        singleConnectionDataSource.setPassword(schemaForgeClientProperties.getPassword());

        return singleConnectionDataSource;
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

