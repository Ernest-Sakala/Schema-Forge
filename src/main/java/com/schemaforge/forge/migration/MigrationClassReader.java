package com.schemaforge.forge.migration;



import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.config.SchemaForgeCommandValue;
import com.schemaforge.forge.config.SchemaForgeConstants;
import com.schemaforge.forge.exception.MigrationDoesNotExistException;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.service.SchemaForeMigrationHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MigrationClassReader {

    private static Logger log = LoggerFactory.getLogger(MigrationClassReader.class);



    private final SchemaForgeClientProperties schemaForgeClientProperties;


    private final SchemaForeMigrationHistoryService schemaForeMigrationHistoryService;


    @Autowired
    private  ApplicationContext applicationContext;



    public MigrationClassReader(SchemaForgeClientProperties schemaForgeClientProperties, SchemaForeMigrationHistoryService schemaForeMigrationHistoryService) {
        this.schemaForgeClientProperties = schemaForgeClientProperties;
        this.schemaForeMigrationHistoryService = schemaForeMigrationHistoryService;
    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }





    public List<MigrationContainer> getMigrationClasses() {

        List<MigrationContainer> migrationClasses = new ArrayList<>();


        try {

            String migrationPath = "forge/database/migrations";

            URL url = getClass().getClassLoader().getResource(migrationPath);

            if (url != null) {


                Path path = Paths.get(url.toURI());

                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(".java")) {
                            compileAndLoadMigration(file, migrationClasses);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (URISyntaxException | IOException e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }

        log.info("Migration Class size >>>>>>>>>>>>>>>>>>>>>>>>>>>"  + migrationClasses.size());

        return migrationClasses;
    }



    @Transactional
    public void compileAndLoadMigration(Path javaFile, List<MigrationContainer> migrationClasses) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);

            log.info("Migration Class >>>>>>>>>>>>>> " + className);

            log.info("Migration Command " + schemaForgeClientProperties.getCommand() + " " + schemaForgeClientProperties.getValue() );

            try {


                Class<?> migrationClass = Class.forName("forge.database.migrations."+className.trim());


                if (Migration.class.isAssignableFrom(migrationClass)) {
                    Migration migrationInstance = (Migration) migrationClass.newInstance();

                    String migration = className.trim();

                    MigrationContainer migrationContainer = new MigrationContainer(migrationInstance,migration);

                    String migrationClassName = migration + SchemaForgeConstants.JAVA_EXTENSION.trim();

                    SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = schemaForeMigrationHistoryService.checkMigrationExists(migrationClassName);


                    boolean checkMigrationAlreadyMigrated = false;
                    if(schemaForgeMigrationHistoryModel != null) {
                        log.info("Migration " + schemaForgeMigrationHistoryModel.getMigration() + " Already Migrated ");
                        if(!schemaForgeClientProperties.getValue().trim().equalsIgnoreCase(SchemaForgeConstants.ALL.trim())) {
                            String migrationName = schemaForgeClientProperties.getValue().trim() + SchemaForgeConstants.JAVA_EXTENSION.trim();
                            String migrationFromDatabase = schemaForgeMigrationHistoryModel.getMigration().trim();
                            log.info("Migration Name " + migrationName);
                            log.info("Migration from Database " + migrationFromDatabase);
                            boolean alreadyMigrated = migrationName.equalsIgnoreCase(migrationFromDatabase);

                            log.info("Already Migrated " + alreadyMigrated);
                            checkMigrationAlreadyMigrated = alreadyMigrated;
                        }else {
                            String migrationFromDatabase = schemaForgeMigrationHistoryModel.getMigration().trim();
                            checkMigrationAlreadyMigrated = migrationClassName.equalsIgnoreCase(migrationFromDatabase);
                        }

                    }


                    log.info("Is revert migration " + schemaForgeClientProperties.getCommand().trim().equalsIgnoreCase(SchemaForgeConstants.REVERT));
                    log.info("IS migrate command " + schemaForgeClientProperties.getCommand().trim().equalsIgnoreCase(SchemaForgeConstants.MIGRATE));

                    log.info("Migration Class name " + migrationClassName);



                    if(schemaForgeClientProperties.getCommand().trim().equalsIgnoreCase(SchemaForgeConstants.REVERT)){

                            if(schemaForgeClientProperties.getValue().trim().equals(SchemaForgeCommandValue.ALL)){
                                if(schemaForgeMigrationHistoryModel != null) {
                                    if(schemaForgeMigrationHistoryModel.getMigration().trim().equalsIgnoreCase(migrationClassName)) {
                                        migrationClasses.add(migrationContainer);
                                    }
                                }
                            } else if((schemaForgeClientProperties.getValue().trim()+ SchemaForgeConstants.JAVA_EXTENSION.trim()).equalsIgnoreCase(migrationClassName.trim())){

                                if(schemaForgeMigrationHistoryModel != null) {
                                    if (schemaForgeMigrationHistoryModel.getMigration().equals(schemaForgeClientProperties.getValue().trim()+ SchemaForgeConstants.JAVA_EXTENSION.trim())) {
                                        migrationClasses.add(migrationContainer);
                                    }
                                }
                            }


                    }else if(schemaForgeClientProperties.getCommand().trim().equals(SchemaForgeConstants.MIGRATE)){

                        if(schemaForgeClientProperties.getValue().trim().equalsIgnoreCase(SchemaForgeCommandValue.ALL)) {
                            log.info("All already migrated "  + checkMigrationAlreadyMigrated);
                            if(!checkMigrationAlreadyMigrated) {
                                migrationClasses.add(migrationContainer);
                            }

                        }else {

                            boolean checkMigrationFileToMigrate = (schemaForgeClientProperties.getValue().trim() + SchemaForgeConstants.JAVA_EXTENSION.trim()).equalsIgnoreCase(migrationClassName.trim());

                            log.info("Migration match  " + checkMigrationFileToMigrate);

                            log.info("Migration migrated " + checkMigrationAlreadyMigrated);

                            if(checkMigrationFileToMigrate && !checkMigrationAlreadyMigrated){
                                migrationClasses.add(migrationContainer);
                            }


                        }
                    }


                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // Handle exceptions as needed
                e.printStackTrace();
            }
        } else {
            // Compilation failed
            System.err.println("Compilation failed for: " + javaFile);
        }

    }

    public static void registerBean(ConfigurableApplicationContext context, String beanName, Class<?> beanClass) {
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) context.getBeanFactory();
        BeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    private String getClassName(Path javaFile) {
        String fullPath = javaFile.toString().replace(File.separator, ".");
        return fullPath.substring(fullPath.indexOf("forge.database.migrations") + 26, fullPath.lastIndexOf('.'));
    }
}

