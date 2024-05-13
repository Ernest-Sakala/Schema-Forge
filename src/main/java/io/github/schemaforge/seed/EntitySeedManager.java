package io.github.schemaforge.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntitySeedManager<E> {


    private static final Logger log = LoggerFactory.getLogger(EntitySeedManager.class);

    private final List<EntitySeedContainer<E>> seedContainerList = new ArrayList<>();

    private final SeedExecutor seedExecutor;

    @Autowired
    public EntitySeedManager(SeedExecutor seedExecutor) {
        this.seedExecutor = seedExecutor;
    }

    public void addSeed(List<EntitySeedContainer<E>> seedContainerListData) {
        seedContainerList.addAll(seedContainerListData);
    }

    public void runSeeds() {


        for(EntitySeedContainer<E> seedContainer : seedContainerList) {
            DataContainer<E> data = seedContainer.getSeeder().seed();


            for(E current : data.getData()){
                Class<?> currentClass =  current.getClass();
                log.info("Class {}" , currentClass.getName());

                String query = buildInsertQuery(current, data.getTableName());

                log.info("Seeding Query {}" , query);

                seedExecutor.execute(seedContainer.getSeederName(),query);

            }

        }
    }

    public String buildInsertQuery(Object object, String tableName) {
        Class<?> currentClass = object.getClass();
        Field[] fields = currentClass.getDeclaredFields();

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName).append(" (");

        // Append column names excluding fields with @Id annotation
        boolean isFirst = true;
        for (Field field : fields) {
            if (!isIdField(field)) {
                if (!isFirst) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(field.getName());
                isFirst = false;
            }
        }

        queryBuilder.append(") VALUES (");

        // Append values
        isFirst = true;
        for (Field field : fields) {
            if (!isIdField(field)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    if (!isFirst) {
                        queryBuilder.append(", ");
                    }
                    if (value != null) {
                        queryBuilder.append("'").append(value).append("'");
                    } else {
                        queryBuilder.append("null");
                    }
                    isFirst = false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        queryBuilder.append(");");

        return queryBuilder.toString();
    }

    private boolean isIdField(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Id")) {
                return true;
            }
        }
        return false;
    }


}
