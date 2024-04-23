package io.github.schemaforge.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        {
            "io.github.schemaforge.database",
            "io.github.schemaforge.seed",
            "io.github.schemaforge.migration",
            "io.github.schemaforge.util",
            "io.github.schemaforge.service",
            "io.github.schemaforge.util",
            "io.github.schemaforge.repository",
            "io.github.schemaforge.model",
            "io.github.schemaforge.schema",
            "io.github.schemaforge.config",
            "forge.database.migrations",
            "forge.database.seeds",
            "classpath:forge.database.migrations"
        })
public class ConfigureDependencyInjection {
}