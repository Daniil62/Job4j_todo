package ru.job4j.todo.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name = "";

    public Category() {
    }

    public Category(int id) {
        this.id = id;
    }

    public Category(String id) {
        this.id = Integer.parseInt(id);
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category)) {
            return false;
        }
        Category category = (Category) o;
        return id == category.id && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id) * (31 + name.hashCode());
    }

    @Override
    public String toString() {
        String n = System.lineSeparator();
        return String.format(" Category: %s%s id: %d%s", name, n, id, n);
    }
}
