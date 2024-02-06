package com.schemaforge.forge.migration;


import com.schemaforge.forge.config.SchemaForgeAnonymous;
import com.schemaforge.forge.config.SchemaForgeClientProperties;
import com.schemaforge.forge.config.SchemaForgeCommandValue;
import com.schemaforge.forge.config.SchemaForgeCommands;
import com.schemaforge.forge.exception.MigrationDoesNotExistException;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import com.schemaforge.forge.service.SchemaForeMigrationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private SchemaForgeClientProperties schemaForgeClientProperties;


    private final SchemaForeMigrationHistoryService schemaForeMigrationHistoryService;

    @Autowired
    public MigrationClassReader(SchemaForgeClientProperties schemaForgeClientProperties, SchemaForeMigrationHistoryService schemaForeMigrationHistoryService) {
        this.schemaForgeClientProperties = schemaForgeClientProperties;
        this.schemaForeMigrationHistoryService = schemaForeMigrationHistoryService;
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

        return migrationClasses;
    }


    @Transactional
    public boolean compileAndLoadMigration(Path javaFile, List<MigrationContainer> migrationClasses) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);
            try {

                Class<?> migrationClass = Class.forName("forge.database.migrations."+className.trim());

                if (Migration.class.isAssignableFrom(migrationClass)) {
                    Migration migrationInstance = (Migration) migrationClass.newInstance();

                    String migration = className.trim();
                    MigrationContainer migrationContainer = new MigrationContainer(migrationInstance,migration);

                    String migrationClassName = migration + SchemaForgeAnonymous.JAVA_EXTENSION.trim();

                    SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = schemaForeMigrationHistoryService.checkMigrationExists(migrationClassName);

                    if(schemaForgeClientProperties.getCommand().trim().equals(SchemaForgeCommands.REVERT)){
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
                        if(schemaForgeClientProperties.getValue().trim().equals(SchemaForgeCommandValue.ALL)) {
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

    private String getClassName(Path javaFile) {
        String fullPath = javaFile.toString().replace(File.separator, ".");
        return fullPath.substring(fullPath.indexOf("forge.database.migrations") + 26, fullPath.lastIndexOf('.'));
    }
}

