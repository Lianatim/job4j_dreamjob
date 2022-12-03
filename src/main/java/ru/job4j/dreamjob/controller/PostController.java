package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static ru.job4j.dreamjob.utils.HttpSetSession.setSession;

@Controller
@ThreadSafe
public class PostController {

    private final PostService postService;
    private final CityService cityService;

    public PostController(PostService postService, CityService cityService) {
        this.postService = postService;
        this.cityService = cityService;
    }

    @GetMapping("/posts")
    public String posts(Model model, HttpSession httpSession) {
        model.addAttribute("posts", postService.findAll());
        setSession(model, httpSession);
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model, HttpSession httpSession) {
        model.addAttribute("post", new Post(0, "Заполните название", "Заполните описание", new City(), true, LocalDateTime.now()));
        model.addAttribute("cities", cityService.getAllCities());
        setSession(model, httpSession);
        return "addPost";
    }


    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        postService.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id, HttpSession httpSession) {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        setSession(model, httpSession);
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        postService.update(post);
        return "redirect:/posts";
    }
}