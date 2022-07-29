package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.AccountStore;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountStore store;
    private static final int MIM_LOGIN_LENGTH = 5;
    private static final int MIM_PASSWORD_LENGTH = 5;

    public AccountService(AccountStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        return store.add(user);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return store.findByLoginAndPassword(login, password);
    }

    public boolean update(User user) {
        return store.update(user);
    }

    public boolean delete(String login, String password) {
        return store.delete(login, password);
    }

    public boolean validate(User user) {
        return user != null && !user.getName().isEmpty()
                && user.getLogin().length() >= MIM_LOGIN_LENGTH
                && user.getPassword().length() >= MIM_PASSWORD_LENGTH;
    }
}
