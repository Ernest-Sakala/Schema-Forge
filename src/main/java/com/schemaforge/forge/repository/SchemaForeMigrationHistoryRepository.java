package com.schemaforge.forge.repository;

import com.schemaforge.forge.database.DatabaseConnection;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryMapper;
import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Repository
public class SchemaForeMigrationHistoryRepository {

    private final DatabaseConnection databaseConnection;

    private final SchemaForgeMigrationHistoryMapper schemaForgeMigrationHistoryMapper;


    @Autowired
    public SchemaForeMigrationHistoryRepository(DatabaseConnection databaseConnection, SchemaForgeMigrationHistoryMapper schemaForgeMigrationHistoryMapper) {
        this.databaseConnection = databaseConnection;
        this.schemaForgeMigrationHistoryMapper = schemaForgeMigrationHistoryMapper;
    }


    @Transactional
    public int insertMigrationHistory(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel) {


        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        String query = "INSERT INTO SCHEMA_FORGE_MIGRATION_HISTORY (version, description, type, migration, createddate, modifieddate) VALUES (?, ?, ?, ?, ?, ?)";


        int rowsAffected = databaseConnection.database().update(conn -> {

            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, schemaForgeMigrationHistoryModel.getVersion());
            preparedStatement.setString(2, schemaForgeMigrationHistoryModel.getDescription());
            preparedStatement.setString(3, schemaForgeMigrationHistoryModel.getType());
            preparedStatement.setString(4, schemaForgeMigrationHistoryModel.getMigration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            return preparedStatement;

        }, generatedKeyHolder);

        return rowsAffected;

    }



    public SchemaForgeMigrationHistoryModel findByMigration(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel) {

        System.out.println("Query Migration >>>>>>>>>>>>>>>>>>" + schemaForgeMigrationHistoryModel.getMigration().trim());

        String sql = "SELECT * FROM SCHEMA_FORGE_MIGRATION_HISTORY WHERE migration = ?";

        try{

          return databaseConnection.database().queryForObject(sql, schemaForgeMigrationHistoryMapper, schemaForgeMigrationHistoryModel.getMigration().trim());

        }catch (Exception exception){

            if(exception instanceof EmptyResultDataAccessException) {
                return null;
            }

            exception.printStackTrace();
        }
        return null;
    }


    public int deleteByMigration(SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel) {

        String sql = "DELETE FROM SCHEMA_FORGE_MIGRATION_HISTORY WHERE migration = ?";

        try{
            return databaseConnection.database().update(sql,schemaForgeMigrationHistoryModel.getMigration());
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return 0;
    }



    public SchemaForgeMigrationHistoryModel findByMigration(String migration) {

        System.out.println("Filtering Migrations to Execute >>>>>>>>>>>>>>>>>>" + migration);

        String sql = "SELECT * FROM SCHEMA_FORGE_MIGRATION_HISTORY WHERE migration = ?";

        try{

            return databaseConnection.database().queryForObject(sql, schemaForgeMigrationHistoryMapper, migration.trim());

        }catch (Exception exception){

            if(exception instanceof EmptyResultDataAccessException) {
                return null;
            }

            exception.printStackTrace();
        }
        return null;
    }




}
