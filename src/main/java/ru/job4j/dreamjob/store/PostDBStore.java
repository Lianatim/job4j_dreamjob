package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.exception.EntityNotFoundException;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PostDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPost(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Failed connection when find all:", e);
        }
        return posts;
    }


    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name, description, city_id, visible, created) VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setStatement(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
        return post;
    }

    public boolean update(Post post) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = ?, description = ?, city_id = ?, visible = ?, created = ?")) {
            setStatement(ps, post);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Failed connection when update:", e);
        }
        return rsl;
    }

    public Optional<Post> findById(int id) {
        Optional<Post> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(createPost(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return rsl;
    }

    private static Post createPost(ResultSet resultSet) throws SQLException {
        int id =  resultSet.getInt("city_id");
        City city = new CityService().findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return new Post(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                city,
                resultSet.getBoolean("visible"),
                resultSet.getTimestamp("created").toLocalDateTime());
    }

    private static void setStatement(PreparedStatement ps, Post post) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setInt(3, post.getCity().getId());
        ps.setBoolean(4, post.isVisible());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }
}