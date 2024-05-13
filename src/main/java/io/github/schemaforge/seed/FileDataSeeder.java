package io.github.schemaforge.seed;

public interface FileDataSeeder<T> {

    FileDataContainer<T> seed();
}
