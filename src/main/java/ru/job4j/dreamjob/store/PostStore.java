package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    AtomicInteger count = new AtomicInteger();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Наша команда разрабатывает программное решение для финансового сектора.", LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Ищем Android-программиста в молодую развивающуюся команду.", LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Мы IT-компания из Санкт-Петербурга, которая занимается разработкой мобильных приложений, веб-порталов.", LocalDateTime.now()));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        posts.put(count.incrementAndGet(), post);
    }
}