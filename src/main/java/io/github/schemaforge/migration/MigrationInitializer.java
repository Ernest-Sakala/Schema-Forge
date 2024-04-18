package io.github.schemaforge.migration;

import io.github.schemaforge.config.SchemaForgeClientProperties;
import io.github.schemaforge.config.SchemaForgeConstants;
import io.github.schemaforge.util.EntityClassScanner;
import io.github.schemaforge.util.FileGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
class MigrationInitializer {

    private static final Logger log = LoggerFactory.getLogger(MigrationManager.class);


    private final MigrationManager migrationManager;

    private final MigrationClassReader migrationClassReader;

    private final EntityClassScanner entityClassScanner;

    private final MigrationClassGenerator migrationClassGenerator;

    private final FileGenerator fileGenerator;

    private final boolean shutdownCompleted = false;

    private final SchemaForgeClientProperties schemaForgeClientProperties;

    private ConfigurableApplicationContext context;

    private boolean doneRunning = false;

    private Thread shutdownThread;

    @Autowired
    public MigrationInitializer(MigrationManager migrationManager, MigrationClassReader migrationClassReader, EntityClassScanner entityClassScanner, MigrationClassGenerator migrationClassGenerator, FileGenerator fileGenerator, SchemaForgeClientProperties schemaForgeClientProperties,  ConfigurableApplicationContext context) {
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

        doneRunning = migrationManager.runMigrations();


        shutdownApplicationAfterRunningMigrations(doneRunning);
    }



    public void preDestroyClose(){
        shutdownApplicationAfterRunningMigrations(doneRunning);
    }

    private void shutdownApplicationAfterRunningMigrations(boolean doneRunning) {

        if(schemaForgeClientProperties.getCommand().equalsIgnoreCase(SchemaForgeConstants.MIGRATE) || schemaForgeClientProperties.getCommand().equalsIgnoreCase(SchemaForgeConstants.REVERT) ) {
            if (doneRunning) {
                // Shutdown the application

//                try {
//                    Thread.sleep(5000); // Adjust the time delay as needed
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//                SpringApplication.exit(context, () -> 0);
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(10000); // Adjust the time delay as needed
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//
//                        log.info("Migrations completed. Shutting down application gracefully.");
//                       // SpringApplication.exit(context, () -> 0);
//
//                        //System.exit(0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }).start();
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

