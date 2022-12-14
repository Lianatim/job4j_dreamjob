package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDBStore;

import java.util.Optional;

@Service
@ThreadSafe
public class UserService {

    public UserService(UserDBStore userDBStore) {
        this.userDBStore = userDBStore;
    }

    private final UserDBStore userDBStore;

    public Optional<User> add(User user) {
        return userDBStore.add(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return userDBStore.findUserByEmailAndPassword(email, password);
    }

    public Optional<User> findById(int id) {
        return userDBStore.findById(id);
    }
}
