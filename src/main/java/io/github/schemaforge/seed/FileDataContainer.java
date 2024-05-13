package io.github.schemaforge.seed;

import java.io.FileReader;

public class FileDataContainer<T> {

    private FileReader fileReader;
    private T entity;
    private String tableName;
    private String fileType;

    public FileDataContainer() {
    }



    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public FileReader getFileReader() {
        return fileReader;
    }

    public void setFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


}
