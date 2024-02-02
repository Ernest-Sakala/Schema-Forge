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

    public MigrationManager(){
        databaseConnection = null;
    }

    public MigrationManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        initializeJDBCConnection(databaseConnection);
    }


    public JdbcTemplate initializeJDBCConnection(DatabaseConnection databaseConnection){
        return new JdbcTemplate(databaseConnection.getDataSource());
    }

    public void addMigration(Migration migration) {
        migrations.add(migration);
    }

    public void runMigrations() {
        for (Migration migration : migrations) {
            migration.forgeSchema();
        }
    }

    public void rollbackMigrations() {
        for (Migration migration : migrations) {
            migration.revert();
        }
    }


}
