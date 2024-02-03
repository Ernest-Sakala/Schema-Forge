package com.schemaforge.forge.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        {
            "com.schemaforge.forge.migration",
            "com.schemaforge.forge.util",
            "com.schemaforge.forge.service",
            "com.schemaforge.forge.util",
            "com.schemaforge.forge.database",
            "com.schemaforge.forge.repository",
            "com.schemaforge.forge.model"
        })
public class ConfigureDependencyInjection {
}