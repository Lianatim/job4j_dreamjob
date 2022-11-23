package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.time.LocalDateTime;

@Controller
//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Order")
public class PostController {

    private final PostStore postStore = PostStore.instOf();

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postStore.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("post", new Post(0, "Заполните название", "Заполните описание", LocalDateTime.now()));
        return "addPost";
    }


    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        postStore.add(post);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postStore.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        if (postStore.update(post)) {
            throw new PostNotFoundException(post.getId());
        }
        return "redirect:/posts";
    }
}