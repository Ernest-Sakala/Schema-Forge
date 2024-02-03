package com.schemaforge.forge.repository;

import com.schemaforge.forge.model.SchemaForgeMigrationHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemaForeMigrationHistoryRepository extends JpaRepository<SchemaForgeMigrationHistoryModel, Long> {

}
