package com.schemaforge.forge.database;

import org.springframework.stereotype.Component;

@Component
public class ConnectionProperties {

    private String url;


    private String password;


    private String username;



    public ConnectionProperties() {
    }

    public void setConnectionProperties(String url, String password, String username) {
        this.url = url;
        this.password = password;
        this.username = username;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
