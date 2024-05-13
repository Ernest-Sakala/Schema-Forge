package io.github.schemaforge.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

@Component
class EntitySeedInitializer<E> {

    private static final Logger log = LoggerFactory.getLogger(EntitySeedInitializer.class);

    private final EntitySeedClassReader<E> seedClassReader;


    private final EntitySeedManager<E> seedManager;


    @Autowired
    public EntitySeedInitializer(EntitySeedClassReader<E> seedClassReader, EntitySeedManager<E> seedManager) {
        this.seedClassReader = seedClassReader;
        this.seedManager = seedManager;
    }



    @PostConstruct
    public void migrate() {
        banner();

       List<EntitySeedContainer<E>> seedContainerList = seedClassReader.getSeedClasses();

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
        System.out.println("Running Entity Seeds ");
    }




}

