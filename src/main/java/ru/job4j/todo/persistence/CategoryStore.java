package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CategoryStore {

    private final QueryExecutor executor;

    public CategoryStore(SessionFactory factory) {
        executor = new QueryExecutor(factory);
    }

    public void add(Category category) {
        executor.execute(session -> session.save(category));
    }

    public List<Category> getAll() {
        return executor.execute(session ->
                session.createQuery("from Category ").list());
    }

    public List<Category> findByIds(final List<Integer> ids) {
        return executor.execute(session -> ids.stream()
                .map(id -> session.get(Category.class, id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public boolean update(Category category, int id) {
        return executor.execute(session -> session.createQuery(
                "update Category c set c.name = :newName where id = :fId")
                .setParameter("newName", category.getName())
                .setParameter("fId", id)
                .executeUpdate() > 0);
    }

    public boolean delete(int id) {
        return executor.execute(session ->
                session.createQuery("delete from Category where id = :fId")
                .setParameter("fId", id)
                .executeUpdate() > 0);
    }
}
