package ru.job4j.dreamjob.store;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

class PostDBStoreTest {

    private final BasicDataSource pool = new Main().loadPool();
    PostDBStore store = new PostDBStore(pool);

    @AfterEach
    public void wipeTable() throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from post")) {
            statement.execute();
        }
    }

    @Test
    public void whenCreatePost() {
        Post post = new Post(0, "Java Job", "description", new City(0, "Moscow"), true, LocalDateTime.now());
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(post.getName());
        assertThat(postInDb.getDescription()).isEqualTo(post.getDescription());
    }

    @Test
    public void whenCreateThenUpdatePost() {
        Post post = new Post(1, "Java Job1", "description1", new City(1, "Spb"), true, LocalDateTime.now());
        Post postUpdate = new Post(1, "Java NewJob1", "newDescription1", new City(1, "Spb"), true, LocalDateTime.now());
        store.add(post);
        store.update(postUpdate);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(postUpdate.getName());
    }

    @Test
    public void whenFindAllPost() {
        Post post = new Post(0, "Java Job", "description", new City(0, "Moscow"), true, LocalDateTime.now());
        Post post1 = new Post(1, "Java Job1", "description1", new City(0, "Moscow"), true, LocalDateTime.now());
        Post post2 = new Post(2, "Java Job2", "description2", new City(0, "Moscow"), true, LocalDateTime.now());
        Collection<Post>  posts = List.of(post, post1, post2);
        store.add(post);
        store.add(post1);
        store.add(post2);
        assertThat(posts).isEqualTo(store.findAll());
    }

    @Test
    public void whenFindIdPost() {
        Post post = new Post(0, "Java Job", "description", new City(0, "Moscow"), true, LocalDateTime.now());
        Post post1 = new Post(1, "Java Job1", "description1", new City(0, "Moscow"), true, LocalDateTime.now());
        Post post2 = new Post(2, "Java Job2", "description2", new City(0, "Moscow"), true, LocalDateTime.now());
        store.add(post);
        store.add(post1);
        store.add(post2);
        assertThat(post1).isEqualTo(store.findById(post1.getId()));
    }
}