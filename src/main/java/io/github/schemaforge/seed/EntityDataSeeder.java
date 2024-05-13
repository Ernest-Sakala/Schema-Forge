package io.github.schemaforge.seed;

import org.springframework.stereotype.Component;

@Component
public interface EntityDataSeeder<E> {

    DataContainer<E> seed();

}
