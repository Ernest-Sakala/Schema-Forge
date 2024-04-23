package io.github.schemaforge.seed;

import java.util.ArrayList;
import java.util.List;

public class DataBuilder<E> {

    private final List<E> objects;

    private String tableName;

    public DataBuilder() {
        this.objects = new ArrayList<>();
    }

    public DataBuilder<E> add(E object) {
        objects.add(object);
        return this;
    }

    public void addTable(String tableName) {
        this.tableName = tableName;
    }

    protected DataContainer<E> getObjectMap() {

        DataContainer<E> container = new DataContainer<>();
        container.setTableName(this.tableName);
        container.setData(this.objects);
        return container;
    }
}
