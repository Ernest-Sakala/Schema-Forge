package io.github.schemaforge.seed;

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
public class FileSeederClassReader<T> {

    private static final Logger log = LoggerFactory.getLogger(FileSeederClassReader.class);


    public List<FileDataSeeder<T>> getSeedClasses() {

        List<FileDataSeeder<T>> jsonDataSeeders = new ArrayList<>();

        try {

            String migrationPath = "forge/database/seeds";

            URL url = getClass().getClassLoader().getResource(migrationPath);

            if (url != null) {


                Path path = Paths.get(url.toURI());

                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(".java")) {
                            compileAndLoadMigration(file, jsonDataSeeders);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (URISyntaxException | IOException e) {
            // Handle exceptions as needed
            e.printStackTrace();
        }


        log.info("Json Seed Class size >>>>>>>>>>>>>>>>>>>>>>>>>>>{}", jsonDataSeeders.size());

        return jsonDataSeeders;
    }




    public void compileAndLoadMigration(Path javaFile,  List<FileDataSeeder<T>> jsonDataSeeders) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());

        if (compilationResult == 0) {
            String className = getClassName(javaFile);

            log.info("Seed Class >>>>>>>>>>>>>> {}", className);


            try {


                Class<?> seederClass = Class.forName("forge.database.seeds."+className.trim());


                if(FileDataSeeder.class.isAssignableFrom(seederClass)) {

                    FileDataSeeder<T> jsonDataSeeder = (FileDataSeeder<T>) seederClass.newInstance();
                    jsonDataSeeders.add(jsonDataSeeder);

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
