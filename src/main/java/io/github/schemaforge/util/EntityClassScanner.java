package io.github.schemaforge.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.HashSet;
import java.util.Set;

@Component
public class EntityClassScanner {

    private final EntityManager entityManager;

    @Autowired
    public EntityClassScanner(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Set<Class<?>> getEntityClasses() {
        Set<Class<?>> entityClasses = new HashSet<>();

        Metamodel metamodel = entityManager.getMetamodel();
        Set<EntityType<?>> entities = metamodel.getEntities();

        for (EntityType<?> entity : entities) {
            entityClasses.add(entity.getJavaType());
        }

        return entityClasses;
    }
}
