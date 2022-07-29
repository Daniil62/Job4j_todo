package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
public class AccountStore {

    private final SessionFactory factory;

    public AccountStore(SessionFactory factory) {
        this.factory = factory;
    }

    public Optional<User> add(User user) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return user.getId() == 0 ? Optional.empty() : Optional.of(user);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result = Optional.empty();
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            result = Optional.of((User) session.createQuery(
                    "from User where login = :l and password = :p")
                    .setParameter("l", login)
                    .setParameter("p", password).getSingleResult());
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public boolean update(User user) {
        Session session = factory.openSession();
        session.beginTransaction();
        boolean result = session.createQuery(
                "update User acc set acc.name = :n, acc.login = :l, "
                        + "acc.password = :p where id = :i")
                .setParameter("n", user.getName())
                .setParameter("l", user.getLogin())
                .setParameter("p", user.getPassword())
                .setParameter("i", user.getId())
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public boolean delete(String login, String password) {
        Session session = factory.openSession();
        session.beginTransaction();
        boolean result = session.createQuery("delete from User "
                + "where login = :l and password = :p")
                .setParameter("l", login)
                .setParameter("p", password)
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
