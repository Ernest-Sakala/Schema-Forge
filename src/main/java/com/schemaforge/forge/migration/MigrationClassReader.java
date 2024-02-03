package com.schemaforge.forge.migration;


import org.springframework.stereotype.Component;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
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

        return migrationClasses;
    }

    private void compileAndLoadMigration(Path javaFile, List<MigrationContainer> migrationClasses) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);
            try {

                Class<?> migrationClass = Class.forName("forge.database.migrations."+className.trim());

                if (Migration.class.isAssignableFrom(migrationClass)) {
                    Migration migrationInstance = (Migration) migrationClass.newInstance();

                    MigrationContainer migrationContainer = new MigrationContainer(migrationInstance,migrationClass.getName());

                    migrationClasses.add(migrationContainer);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // Handle exceptions as needed
                e.printStackTrace();
            }
        } else {
            // Compilation failed
            System.err.println("Compilation failed for: " + javaFile.toString());
        }
    }

    private String getClassName(Path javaFile) {
        String fullPath = javaFile.toString().replace(File.separator, ".");
        return fullPath.substring(fullPath.indexOf("forge.database.migrations") + 26, fullPath.lastIndexOf('.'));
    }
}

