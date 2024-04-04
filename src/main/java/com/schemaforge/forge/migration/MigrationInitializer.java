package com.schemaforge.forge.migration;

import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.config.SchemaForgeConstants;
import com.schemaforge.forge.util.EntityClassScanner;
import com.schemaforge.forge.util.FileGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
class MigrationInitializer {

    private static Logger log = LoggerFactory.getLogger(MigrationManager.class);


    private final MigrationManager migrationManager;

    private final MigrationClassReader migrationClassReader;

    private final EntityClassScanner entityClassScanner;

    private final MigrationClassGenerator migrationClassGenerator;


    private final FileGenerator fileGenerator;

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    private ApplicationContext context;

    @Autowired
    public MigrationInitializer(MigrationManager migrationManager, MigrationClassReader migrationClassReader, EntityClassScanner entityClassScanner, MigrationClassGenerator migrationClassGenerator, FileGenerator fileGenerator, SchemaForgeClientProperties schemaForgeClientProperties, ApplicationContext context) {
        this.migrationManager = migrationManager;
        this.migrationClassReader = migrationClassReader;
        this.entityClassScanner = entityClassScanner;
        this.migrationClassGenerator = migrationClassGenerator;
        this.fileGenerator = fileGenerator;
        this.schemaForgeClientProperties = schemaForgeClientProperties;
        this.context = context;
    }

    @PostConstruct
    public void migrate() {

        banner();

        try {
            fileGenerator.generateFile("{" +
                    "\n" +
                    "\t" + "\"" +"database" + "\"" + ":" + "\"" + schemaForgeClientProperties.getDatabase() +"\""+
                    "\n"+
                    "}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for(Class<?> entityClass : entityClassScanner.getEntityClasses()){
            migrationClassGenerator.generateMigrationClass(entityClass);
        }

        migrationManager.addMigration(migrationClassReader.getMigrationClasses());

        boolean doneRunning = migrationManager.runMigrations();


        shutdownApplicationAfterRunningMigrations(doneRunning);
    }

    private void shutdownApplicationAfterRunningMigrations(boolean doneRunning) {
        if(schemaForgeClientProperties.getCommand().equalsIgnoreCase(SchemaForgeConstants.MIGRATE) || schemaForgeClientProperties.getCommand().equalsIgnoreCase(SchemaForgeConstants.REVERT) ) {
            if (doneRunning) {

                log.info("Finished Running Migrations Schema Forge");

                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ((ConfigurableApplicationContext) context).close();
                }).start();
            }
        }
    }


    private void banner() {
        System.out.println(" ,---.        ,--.                                  ,------.                            ");
        System.out.println("'   .-'  ,---.|  ,---.  ,---. ,--,--,--. ,--,--.    |  .---',---. ,--.--. ,---.  ,---.  ");
        System.out.println("`.  `-. | .--'|  .-.  || .-. :|        |' ,-.  |    |  `--,| .-. ||  .--'| .-. || .-. : ");
        System.out.println(".-'    |\\ `--.|  | |  |\\   --.|  |  |  |\\ '-'  |    |  |`  ' '-' '|  |   ' '-' '\\   --. ");
        System.out.println("`-----'  `---'`--' `--' `----'`--`--`--' `--`--'    `--'    `---' `--'   .`-  /  `----' ");
        System.out.println("Developed By Ernest Sakala. Inspired By Laravel Migrations                                                                                       ");
        System.out.println(" ");
        System.out.println("Running Migrations ");
    }




}

