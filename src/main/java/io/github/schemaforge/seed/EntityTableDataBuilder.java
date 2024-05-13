package io.github.schemaforge.seed;

import java.util.function.Consumer;

public class EntityTableDataBuilder<E> {

    public DataContainer<E> insertData(String tableName, Consumer<DataBuilder<E>> dataBuilderConsumer) {

        DataBuilder<E> dataBuilder = new DataBuilder<>();

        dataBuilderConsumer.accept(dataBuilder);

        dataBuilder.addTable(tableName);

        return dataBuilder.getObjectMap();

    }
}
