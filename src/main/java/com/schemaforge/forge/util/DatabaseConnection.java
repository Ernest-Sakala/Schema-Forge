package com.schemaforge.forge.util;

import com.schemaforge.forge.db.ConnectionProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class DatabaseConnection {

    private String url;
    private String username;
    private String password;


    public DatabaseConnection() {
    }

    public void setConnection(Map<String , String> databaseDetails){
        url = databaseDetails.get("URL");
        username = databaseDetails.get("USERNAME");
        password = databaseDetails.get("PASSWORD");
    }

    public void setConnection(ConnectionProperties databaseDetails){
        url = databaseDetails.getUrl();
        username = databaseDetails.getUsername();
        password = databaseDetails.getPassword();
    }


    public DataSource getDataSource() {

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        if(url.contains("mysql")){
            dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        }else if(url.contains("postgresql")){
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
        }

        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

}

