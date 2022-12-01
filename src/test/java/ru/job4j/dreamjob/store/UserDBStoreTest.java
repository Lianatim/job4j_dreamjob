package ru.job4j.dreamjob.store;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

class UserDBStoreTest {

    private final BasicDataSource pool = new Main().loadPool();
    UserDBStore store = new UserDBStore(pool);

    @AfterEach
    public void wipeTable() throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from users")) {
            statement.execute();
        }
    }
    @Test
    void whenAddUser() {
        User user = new User(0, "email", "password");
        store.add(user);
        User userInDb = store.findById(user.getId()).orElseThrow(NoSuchElementException::new);
        assertThat(userInDb.getEmail()).isEqualTo(user.getEmail());

    }

    @Test
    void whenFindUser() {
        User user = new User(0, "email", "password");
        store.add(user);
        User userInDb = store.findUserByEmailAndPassword(user.getEmail(), user.getPassword()).orElseThrow();
        assertThat(userInDb.getEmail()).isEqualTo(user.getEmail());
    }
}