package com.schemaforge.forge.migration;

import com.schemaforge.forge.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MigrationManager {


    private JdbcTemplate jdbcTemplate;

    private final List<MigrationContainer> migrations = new ArrayList<>();

    private final DatabaseConnection databaseConnection;

    private ExecuteMigration executeMigration;

    @Autowired
    public MigrationManager(DatabaseConnection databaseConnection, ExecuteMigration executeMigration) {
        this.databaseConnection = databaseConnection;
        this.executeMigration = executeMigration;
        initializeJDBCConnection(databaseConnection);
    }

    public void initializeJDBCConnection(DatabaseConnection databaseConnection){
        this.jdbcTemplate = new JdbcTemplate(databaseConnection.getDataSource());
    }

    public void addMigration(List<MigrationContainer> migration) {
        migrations.addAll(migration);
    }

    public void runMigrations() {

        for (MigrationContainer migration : migrations) {
          String query = migration.getMigration().forgeSchema();

          if(query != null) {
              try {
                  System.out.println("Schema Forge Migration Query >>> " + query);
                  executeMigration.execute(migration.getMigrationName().trim(),query);
              }catch (Exception exception){
                  exception.printStackTrace();
              }
          }
        }
    }

    public void rollbackMigrations() {
        for (MigrationContainer migration : migrations) {
            String query = migration.getMigration().forgeSchema();
            System.out.println("Schema Forge Query" + query);
            jdbcTemplate.execute(query);
        }
    }


}
