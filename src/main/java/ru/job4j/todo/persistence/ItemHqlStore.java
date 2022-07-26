package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemHqlStore {

    private final SessionFactory factory;

    public ItemHqlStore(SessionFactory factory) {
        this.factory = factory;
    }

    public void add(Item item) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    public boolean update(Item item) {
        Session session = factory.openSession();
        session.beginTransaction();
        boolean result = session.createQuery(
                "update Item i set i.header = :newHeader,"
                        + " i.description = :newDescription, "
                        + "i.created = :newCreated where id = :fId")
                .setParameter("newHeader", item.getHeader())
                .setParameter("newDescription", item.getDescription())
                .setParameter("newCreated", LocalDate.now())
                .setParameter("fId", item.getId())
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Optional<Object> findById(long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        Optional<Object> result = Optional.of(session.get(Item.class, id));
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> getAll() {
        Session session = factory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> getPerformed() {
        return findItemsByStatus(true);
    }

    public List<Item> getUnperformed() {
        return findItemsByStatus(false);
    }

    private List<Item> findItemsByStatus(boolean status) {
        Session session = factory.openSession();
        session.beginTransaction();
        List result = session
                .createQuery("from Item where done = :d")
                .setParameter("d", status)
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public void changeItemStatus(long id, boolean status) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.createQuery(
                "update Item i set i.done = :newStatus "
                        + "where id = :fId")
                .setParameter("fId", id)
                .setParameter("newStatus", status)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public boolean delete(long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        boolean result = session.createQuery("delete from Item where id = :fId")
                .setParameter("fId", id)
                .executeUpdate() > 0;
        session.close();
        return result;
    }
}
