package io.github.schemaforge.seed;

import io.github.schemaforge.config.SchemaForgeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EntitySeedClassReader<E> {

    private static final Logger log = LoggerFactory.getLogger(EntitySeedClassReader.class);


    public List<EntitySeedContainer<E>> getSeedClasses() {

        List<EntitySeedContainer<E>> seedContainer = new ArrayList<>();

        try {

            String migrationPath = "forge/database/seeds";

            URL url = getClass().getClassLoader().getResource(migrationPath);

            if (url != null) {


                Path path = Paths.get(url.toURI());

                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(".java")) {
                            compileAndLoadMigration(file, seedContainer);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (URISyntaxException | IOException e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }

        log.info("Seed Class size >>>>>>>>>>>>>>>>>>>>>>>>>>>{}", seedContainer.size());


        return seedContainer;
    }




    public void compileAndLoadMigration(Path javaFile, List<EntitySeedContainer<E>> migrationClasses) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);

            log.info("Seed Class >>>>>>>>>>>>>> {}", className);


            try {


                Class<?> seederClass = Class.forName("forge.database.seeds."+className.trim());


                if (EntityDataSeeder.class.isAssignableFrom(seederClass)) {

                    EntityDataSeeder<E> migrationInstance = (EntityDataSeeder<E>) seederClass.newInstance();
                    String migration = className.trim();
                    EntitySeedContainer<E> migrationContainer = new EntitySeedContainer<>(migrationInstance,migration);
                    String migrationClassName = migration + SchemaForgeConstants.JAVA_EXTENSION.trim();
                    migrationClasses.add(migrationContainer);
                }



            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                // Handle exceptions as needed
                log.error(e.getMessage());
            }
        } else {
            // Compilation failed
            System.err.println("Compilation failed for: " + javaFile);
        }

    }



    private String getClassName(Path javaFile) {
        String fullPath = javaFile.toString().replace(File.separator, ".");
        return fullPath.substring(fullPath.indexOf("forge.database.seeds") + 21, fullPath.lastIndexOf('.'));
    }
}
