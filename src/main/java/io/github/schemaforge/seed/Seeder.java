package io.github.schemaforge.seed;

import org.springframework.stereotype.Component;

@Component
public interface Seeder<E> {

    DataContainer<E> seed();

}
