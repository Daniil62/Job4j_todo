package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
public class AccountStore {

    private final QueryExecutor executor;

    public AccountStore(SessionFactory factory) {
        executor = new QueryExecutor(factory);
    }

    public Optional<User> add(User user) {
        try {
            executor.execute(session -> session.save(user));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return user.getId() == 0 ? Optional.empty() : Optional.of(user);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result;
        try {
            result = Optional.of((User) executor.execute(session ->
                    session.createQuery(
                            "from User where login = :l and password = :p")
                            .setParameter("l", login)
                            .setParameter("p", password).getSingleResult()));
        } catch (final Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    public boolean update(User user) {
        return executor.execute(session -> session.createQuery(
               "update User acc set acc.name = :n, acc.login = :l, "
                       + "acc.password = :p where id = :i")
               .setParameter("n", user.getName())
               .setParameter("l", user.getLogin())
               .setParameter("p", user.getPassword())
               .setParameter("i", user.getId())
               .executeUpdate() > 0);
    }

    public boolean delete(String login, String password) {
        return executor.execute(session -> session.createQuery("delete from User "
                + "where login = :l and password = :p")
                .setParameter("l", login)
                .setParameter("p", password)
                .executeUpdate() > 0);
    }
}