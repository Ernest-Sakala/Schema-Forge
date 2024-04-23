package io.github.schemaforge.seed;

public class SeedContainer<E> {

    private Seeder<E> seeder;
    private String seederName;

    public SeedContainer() {
    }

    public SeedContainer(Seeder<E> seeder, String seederName) {
        this.seeder = seeder;
        this.seederName = seederName;
    }

    public Seeder<E> getSeeder() {
        return seeder;
    }

    public void setSeeder(Seeder<E> seeder) {
        this.seeder = seeder;
    }

    public String getSeederName() {
        return seederName;
    }

    public void setSeederName(String seederName) {
        this.seederName = seederName;
    }
}
