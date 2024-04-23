package io.github.schemaforge.seed;

import java.util.List;

public class DataContainer <E>{

    private String tableName;
    private List<E> data;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
