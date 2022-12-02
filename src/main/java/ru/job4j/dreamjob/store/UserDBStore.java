package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private static final String ADD = "INSERT INTO users(email, password) VALUES (?, ?)";
    private static final String FIND_BY_PASSWORD_EMAIL = "SELECT * FROM users WHERE email = ? AND password = ?";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> userAdd = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    userAdd = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
        return userAdd;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> user = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_PASSWORD_EMAIL)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    user = Optional.of(getUser(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when find:", e);
        }
        return user;
    }

    public Optional<User> findById(int id) {
        Optional<User> user = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    user = Optional.of(getUser(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return user;
    }

    public User getUser(ResultSet it) throws SQLException {
        return new User(it.getInt("id"), it.getString("email"),
                it.getString("password"));
    }
}
