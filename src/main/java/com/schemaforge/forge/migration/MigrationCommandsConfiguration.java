package com.schemaforge.forge.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MigrationCommandsConfiguration implements EnvironmentAware {

    private static Logger log = LoggerFactory.getLogger(MigrationCommandsConfiguration.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void printArguments() {
        // Access command-line arguments using the Environment bean
        String rollbackCommand = environment.getProperty("forge-rollback");
        String activeProfile = environment.getProperty("migration");

        log.info("Schema Forge Rollback Command : " + rollbackCommand);
        log.info("Schema for Rollback Arguments  : " + activeProfile);
    }
}