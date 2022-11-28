package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Наша команда разрабатывает программное решение для финансового сектора.", new City(2, "СПб"), true, LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Ищем Android-программиста в молодую развивающуюся команду.", new City(1, "Москва"), true, LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Мы IT-компания из Санкт-Петербурга, которая занимается разработкой мобильных приложений, веб-порталов.", new City(3, "Екб"), true, LocalDateTime.now()));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        int id = count.incrementAndGet();
        post.setId(id);
        post.setCreated(LocalDateTime.now());
        posts.putIfAbsent(id, post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public boolean update(Post post) {
        post.setCreated(LocalDateTime.now());
        return posts.replace(post.getId(), posts.get(post.getId()), post);
    }
}