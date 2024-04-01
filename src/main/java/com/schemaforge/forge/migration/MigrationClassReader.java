package com.schemaforge.forge.migration;


import com.schemaforge.forge.config.SchemaForgeAnonymous;
import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.config.SchemaForgeCommandValue;
import com.schemaforge.forge.config.SchemaForgeCommands;
import com.schemaforge.forge.exception.MigrationDoesNotExistException;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.service.SchemaForeMigrationHistoryService;
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

@Component
public class MigrationClassReader {

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

                        boolean continueWalking = true;
                        if (file.toString().endsWith(".java")) {
                            continueWalking =  compileAndLoadMigration(file, migrationClasses);
                        }

                        if(continueWalking) {
                            return FileVisitResult.CONTINUE;
                        }else {
                            return FileVisitResult.TERMINATE;
                        }
                    }
                });
            }
        } catch (URISyntaxException | IOException e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }

        System.out.println("Migration Class size >>>>>>>>>>>>>>>>>>>>>>>>>>>"  + migrationClasses.size());

        return migrationClasses;
    }


    @Transactional
    public boolean compileAndLoadMigration(Path javaFile, List<MigrationContainer> migrationClasses) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);

            System.out.println("Migration Command " + schemaForgeClientProperties.getCommand() + " " + schemaForgeClientProperties.getValue() );

            try {

                // Get the current application context
                ApplicationContext context = getApplicationContext();

                Class<?> migrationClass = Class.forName("forge.database.migrations."+className.trim());

                // Register the bean with the current application context
              //  registerBean((ConfigurableApplicationContext) context, className, migrationClass);

                if (Migration.class.isAssignableFrom(migrationClass)) {
                    Migration migrationInstance = (Migration) migrationClass.newInstance();

                    String migration = className.trim();

                    MigrationContainer migrationContainer = new MigrationContainer(migrationInstance,migration);

                    String migrationClassName = migration + SchemaForgeAnonymous.JAVA_EXTENSION.trim();

                    SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = schemaForeMigrationHistoryService.checkMigrationExists(migrationClassName);

                    System.out.println("IS revert migration" + schemaForgeClientProperties.getCommand().trim().equalsIgnoreCase(SchemaForgeCommands.REVERT));


                    System.out.println("IS migrating command >>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + schemaForgeClientProperties.getValue().trim().equalsIgnoreCase(SchemaForgeCommandValue.ALL));


                    if(schemaForgeClientProperties.getCommand().trim().equalsIgnoreCase(SchemaForgeCommands.REVERT)){
                        if(schemaForgeMigrationHistoryModel != null){

                            if(schemaForgeClientProperties.getValue().trim().equals(SchemaForgeCommandValue.ALL)){
                                migrationClasses.add(migrationContainer);
                                return true;
                            } else if(schemaForgeClientProperties.getValue().trim().equals(migrationClassName)){
                                if (schemaForgeMigrationHistoryModel.getMigration().equals(migrationClassName)) {
                                    migrationClasses.add(migrationContainer);
                                    return false;
                                }
                            }

                        }else {
                            try {
                                throw new MigrationDoesNotExistException();
                            } catch (MigrationDoesNotExistException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else if(schemaForgeClientProperties.getCommand().trim().equals(SchemaForgeCommands.MIGRATE)){

                        if(schemaForgeClientProperties.getValue().trim().equalsIgnoreCase(SchemaForgeCommandValue.ALL)) {
                            migrationClasses.add(migrationContainer);
                            return true;
                        }else if(schemaForgeClientProperties.getValue().trim().equals(migrationClassName)){
                            migrationClasses.add(migrationContainer);
                            return false;
                        }
                    }


                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // Handle exceptions as needed
                e.printStackTrace();
            }
        } else {
            // Compilation failed
            System.err.println("Compilation failed for: " + javaFile.toString());
        }

        return false;
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

