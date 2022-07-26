package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.ItemHqlStore;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemHqlStore store;

    public ItemService(ItemHqlStore store) {
        this.store = store;
    }

    public void create(Item item) {
        store.add(item);
    }

    public boolean update(Item item) {
        return store.update(item);
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
}
