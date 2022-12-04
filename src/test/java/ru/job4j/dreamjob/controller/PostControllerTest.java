package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "New description", new City(1, "Spb"), true, LocalDateTime.now()),
                new Post(2, "New post", "New description", new City(2, "Spb"), true, LocalDateTime.now())
        );
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, httpSession);
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenAddPost() {
        List<City> cities = Arrays.asList(new City(1, "Spb"), new City(2, "Msc"));
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        Post verifyPost = new Post(0, "Заполните название", "Заполните описание", new City(), true, LocalDateTime.now());
        String page = postController.addPost(model, httpSession);
        verify(model).addAttribute("post", verifyPost);
        verify(model).addAttribute("cities", cities);
        assertThat(page).isEqualTo("addPost");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post", "New description", new City(1, "Spb"), true, LocalDateTime.now());
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenFormUpdatePost() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "New description", new City(1, "Spb"), true, LocalDateTime.now()),
                new Post(2, "New post", "New description", new City(2, "Msc"), true, LocalDateTime.now())
        );
        List<City> cities = Arrays.asList(new City(1, "Spb"), new City(2, "Msc"));
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        int id = posts.get(1).getId();
        when(postService.findById(id)).thenReturn(posts.get(1));
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, id, httpSession);
        verify(model).addAttribute("post", posts.get(1));
        verify(model).addAttribute("cities", cities);
        assertThat(page).isEqualTo("updatePost");
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "New post", "New description", new City(1, "Spb"), true, LocalDateTime.now());
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(input);
        verify(postService).update(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }
}