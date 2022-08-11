package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemHqlStore {

    private final QueryExecutor executor;

    public ItemHqlStore(SessionFactory factory) {
        executor = new QueryExecutor(factory);
    }

    public void add(Item item) {
        executor.execute(session -> session.save(item));
    }

    public void update(Item item) {
        executor.execute(session -> {
            session.update(item);
            return item;
        });
    }

    public Optional<Object> findById(long id) {
        return Optional.of(executor.execute(session ->
                session.createQuery("from Item i join fetch i.categories where i.id = :id")
                        .setParameter("id", id)
                        .uniqueResult()));
    }

    public List<Item> getAll() {
        return executor.execute(session -> session.createQuery("from Item").list());
    }

    public List<Item> getPerformed() {
        return findItemsByStatus(true);
    }

    public List<Item> getUnperformed() {
        return findItemsByStatus(false);
    }

    private List<Item> findItemsByStatus(boolean status) {
        return executor.execute(session -> session
                .createQuery("from Item where done = :d")
                .setParameter("d", status)
                .list());
    }

    public void changeItemStatus(long id, boolean status) {
        executor.execute(session -> session.createQuery(
                "update Item i set i.done = :newStatus "
                        + "where id = :fId")
                .setParameter("fId", id)
                .setParameter("newStatus", status)
                .executeUpdate());
    }

    public boolean delete(long id) {
        return executor.execute(session -> session.createQuery("delete from Item where id = :fId")
                .setParameter("fId", id)
                .executeUpdate() > 0);
    }
}