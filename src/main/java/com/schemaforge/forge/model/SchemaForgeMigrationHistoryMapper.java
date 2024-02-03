package com.schemaforge.forge.model;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SchemaForgeMigrationHistoryMapper implements RowMapper<SchemaForgeMigrationHistoryModel> {

    @Override
    public SchemaForgeMigrationHistoryModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        SchemaForgeMigrationHistoryModel schemaForgeMigrationHistoryModel = new SchemaForgeMigrationHistoryModel();

        schemaForgeMigrationHistoryModel.setId(resultSet.getLong("id"));
        schemaForgeMigrationHistoryModel.setMigration(resultSet.getString("migration"));
        schemaForgeMigrationHistoryModel.setCreatedDate(resultSet.getTimestamp("createddate").toLocalDateTime());

        return schemaForgeMigrationHistoryModel;

    }
}
