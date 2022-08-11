package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CategoryStore;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryStore store;

    public CategoryService(CategoryStore store) {
        this.store = store;
    }

    public void add(Category category) {
        store.add(category);
    }

    public List<Category> getAll() {
        return store.getAll();
    }

    public boolean update(Category category, int id) {
        return store.update(category, id);
    }

    public boolean delete(int id) {
        return store.delete(id);
    }
}
