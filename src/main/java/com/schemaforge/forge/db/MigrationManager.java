package com.schemaforge.forge.db;

import com.schemaforge.forge.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MigrationManager {


    private JdbcTemplate jdbcTemplate;

    private final List<Migration> migrations = new ArrayList<>();

    private final DatabaseConnection databaseConnection;

    @Autowired
    public MigrationManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        initializeJDBCConnection(databaseConnection);
    }



    public void initializeJDBCConnection(DatabaseConnection databaseConnection){
        this.jdbcTemplate = new JdbcTemplate(databaseConnection.getDataSource());
    }

    public void addMigration(List<Migration> migration) {
        migrations.addAll(migration);
    }

    public void runMigrations() {

        for (Migration migration : migrations) {
          String query = migration.forgeSchema();

          if(query != null) {
              System.out.println("Schema Forge Query" + query);
              jdbcTemplate.execute(query);
          }
        }
    }

    public void rollbackMigrations() {
        for (Migration migration : migrations) {
            String query = migration.forgeSchema();
            System.out.println("Schema Forge Query" + query);
            jdbcTemplate.execute(query);
        }
    }


}
