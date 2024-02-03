package com.schemaforge.forge.migration;

public class MigrationContainer {

    private Migration migration;
    private String migrationName;

    public MigrationContainer(Migration migration, String migrationName) {
        this.migration = migration;
        this.migrationName = migrationName;
    }

    public Migration getMigration() {
        return migration;
    }

    public void setMigration(Migration migration) {
        this.migration = migration;
    }

    public String getMigrationName() {
        return migrationName;
    }

    public void setMigrationName(String migrationName) {
        this.migrationName = migrationName;
    }
}
