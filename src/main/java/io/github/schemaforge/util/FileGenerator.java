package io.github.schemaforge.util;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileGenerator {

    public void generateFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/forge.json"))) {
            writer.write(content);
        }
    }
}
