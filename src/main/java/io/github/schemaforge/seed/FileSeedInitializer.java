package io.github.schemaforge.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class FileSeedInitializer<T> {

    private static final Logger log = LoggerFactory.getLogger(FileSeedInitializer.class);

    private final FileSeederClassReader<T> jsonSeederClassReader;


    private final FileSeedManager<T> jsonSeedManager;

    public FileSeedInitializer(FileSeederClassReader<T> jsonSeederClassReader, FileSeedManager<T> jsonSeedManager) {
        this.jsonSeederClassReader = jsonSeederClassReader;
        this.jsonSeedManager = jsonSeedManager;
    }


    @PostConstruct
    public void migrate() {
        banner();

        List<FileDataSeeder<T>> dataSeeders = jsonSeederClassReader.getSeedClasses();

        jsonSeedManager.addSeed(dataSeeders);

        jsonSeedManager.runSeeds();

    }


    private void banner() {
        System.out.println(" ,---.        ,--.                                  ,------.                            ");
        System.out.println("'   .-'  ,---.|  ,---.  ,---. ,--,--,--. ,--,--.    |  .---',---. ,--.--. ,---.  ,---.  ");
        System.out.println("`.  `-. | .--'|  .-.  || .-. :|        |' ,-.  |    |  `--,| .-. ||  .--'| .-. || .-. : ");
        System.out.println(".-'    |\\ `--.|  | |  |\\   --.|  |  |  |\\ '-'  |    |  |`  ' '-' '|  |   ' '-' '\\   --. ");
        System.out.println("`-----'  `---'`--' `--' `----'`--`--`--' `--`--'    `--'    `---' `--'   .`-  /  `----' ");
        System.out.println("Developed By Ernest Sakala. Inspired By Laravel Migrations                                                                                       ");
        System.out.println(" ");
        System.out.println("Running Json Seeds ");
    }

}
