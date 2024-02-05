package com.schemaforge.forge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "schemaforge")
public class SchemaForgeClientProperties {

    private boolean rollbackMigrations;

    public boolean isRollbackMigrations() {
        return rollbackMigrations;
    }

    public void setRollbackMigrations(boolean rollbackMigrations) {
        this.rollbackMigrations = rollbackMigrations;
    }
}
