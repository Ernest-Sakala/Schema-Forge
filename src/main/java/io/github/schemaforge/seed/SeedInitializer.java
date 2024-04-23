package io.github.schemaforge.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

@Component
class SeedInitializer<E> {

    private static final Logger log = LoggerFactory.getLogger(SeedInitializer.class);

    private final SeedClassReader<E> seedClassReader;


    private final SeedManager<E> seedManager;


    @Autowired
    public SeedInitializer(SeedClassReader<E> seedClassReader, SeedManager<E> seedManager) {
        this.seedClassReader = seedClassReader;
        this.seedManager = seedManager;
    }



    @PostConstruct
    public void migrate() {
        banner();

       List<SeedContainer<E>> seedContainerList = seedClassReader.getSeedClasses();

       seedManager.addSeed(seedContainerList);

       seedManager.runSeeds();

    }





    private void banner() {
        System.out.println(" ,---.        ,--.                                  ,------.                            ");
        System.out.println("'   .-'  ,---.|  ,---.  ,---. ,--,--,--. ,--,--.    |  .---',---. ,--.--. ,---.  ,---.  ");
        System.out.println("`.  `-. | .--'|  .-.  || .-. :|        |' ,-.  |    |  `--,| .-. ||  .--'| .-. || .-. : ");
        System.out.println(".-'    |\\ `--.|  | |  |\\   --.|  |  |  |\\ '-'  |    |  |`  ' '-' '|  |   ' '-' '\\   --. ");
        System.out.println("`-----'  `---'`--' `--' `----'`--`--`--' `--`--'    `--'    `---' `--'   .`-  /  `----' ");
        System.out.println("Developed By Ernest Sakala. Inspired By Laravel Migrations                                                                                       ");
        System.out.println(" ");
        System.out.println("Running Seeds ");
    }




}

