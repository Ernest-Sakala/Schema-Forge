package io.github.schemaforge.seed;

import io.github.schemaforge.database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
public class SeedExecutor {

    private static final Logger log = LoggerFactory.getLogger(SeedExecutor.class);

    private final DatabaseConnection databaseConnection;

    @Autowired
    public SeedExecutor(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Transactional
    public void execute(String seed, String query){

        try{

            databaseConnection.database().execute(query);

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }




}
