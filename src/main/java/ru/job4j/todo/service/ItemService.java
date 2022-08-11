package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.CategoryStore;
import ru.job4j.todo.persistence.ItemHqlStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemHqlStore store;
    private final CategoryStore categoryStore;

    public ItemService(ItemHqlStore store, CategoryStore categoryStore) {
        this.store = store;
        this.categoryStore = categoryStore;
    }

    public void create(Item item) {
        store.add(item);
    }

    public void update(Item item) {
        store.update(item);
    }

    public List<Item> getAll() {
        return store.getAll();
    }

    public Optional<Object> findById(long id) {
        return store.findById(id);
    }

    public List<Item> getPerformed() {
        return store.getPerformed();
    }

    public List<Item> getUnperformed() {
        return store.getUnperformed();
    }

    public void changeStatus(long id, boolean status) {
        store.changeItemStatus(id, status);
    }

    public boolean delete(long id) {
        return store.delete(id);
    }

    public void setCategories(Item item) {
        if (item != null && !item.getCategories().isEmpty()) {
            final List<Integer> ids = new ArrayList<>();
            for (Category category : item.getCategories()) {
                ids.add(category.getId());
            }
            item.setCategories(categoryStore.findByIds(ids));
        }
    }
}
