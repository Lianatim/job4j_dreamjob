package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.exception.EntityNotFoundException;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDBStore;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class PostService {

    private final PostDBStore postDBStore;
    private final CityService cityService;

    public PostService(PostDBStore postDBStore, CityService cityService) {
        this.postDBStore = postDBStore;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        Collection<Post> posts = postDBStore.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId()).orElseThrow(() -> new EntityNotFoundException(post.getCity().getId()))
                )
        );
        return posts;
    }

    public void add(Post post) {
        postDBStore.add(post);
    }

    public Optional<Post> findById(int id) {
        return postDBStore.findById(id);
    }

    public boolean update(Post post) {
        return postDBStore.update(post);
    }
}
