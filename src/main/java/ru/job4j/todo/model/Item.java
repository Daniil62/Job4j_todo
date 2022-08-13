package ru.job4j.todo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String header = "";
    @Column(columnDefinition = "TEXT")
    private String description = "";
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created = new Date(System.currentTimeMillis());
    private boolean done;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final Set<Category> categories = new HashSet<>();

    public Item() {
    }

    public Item(long id, String header, String description, Date created, boolean done, User user) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.created = created;
        this.done = done;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addCategoryCollection(Category[] array) {
        Collections.addAll(categories, array);
    }

    public Set<Category> getCategories() {
        return Set.copyOf(categories);
    }

    public void setCategories(Collection<Category> collection) {
        categories.clear();
        categories.addAll(collection);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) {
            return false;
        }
        return id == ((Item) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String n = System.lineSeparator();
        return String.format(
                " id: %d%s header: %s%s description: %s%s created: %s"
                        + "%s done: %b%s author: %s%s categories: %s%s$s",
                id, n, header, n, description, n, created.toString(),
                n, done, n, user, n, categories.toString(), n);
    }
}