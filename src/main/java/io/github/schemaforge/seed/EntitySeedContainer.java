package io.github.schemaforge.seed;

public class EntitySeedContainer<E> {

    private EntityDataSeeder<E> seeder;
    private String seederName;

    public EntitySeedContainer() {
    }

    public EntitySeedContainer(EntityDataSeeder<E> seeder, String seederName) {
        this.seeder = seeder;
        this.seederName = seederName;
    }

    public EntityDataSeeder<E> getSeeder() {
        return seeder;
    }

    public void setSeeder(EntityDataSeeder<E> seeder) {
        this.seeder = seeder;
    }

    public String getSeederName() {
        return seederName;
    }

    public void setSeederName(String seederName) {
        this.seederName = seederName;
    }
}
