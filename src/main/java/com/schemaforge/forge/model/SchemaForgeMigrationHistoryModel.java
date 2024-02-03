package com.schemaforge.forge.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEMA_FORGE_MIGRATION_HISTORY")
public class SchemaForgeMigrationHistoryModel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer version;

    private String description;

    private String type;

    private String script;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SchemaForgeMigrationHistoryModel() {
    }

    public SchemaForgeMigrationHistoryModel(Long id, Integer version, String description, String type, String script, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.version = version;
        this.description = description;
        this.type = type;
        this.script = script;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
