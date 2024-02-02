package com.schemaforge.forge.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.schemaforge.forge.db", "com.schemaforge.forge.util"})
public class ConfigureDependencyInjection {
}