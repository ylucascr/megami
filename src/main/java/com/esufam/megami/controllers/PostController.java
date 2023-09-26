package com.esufam.megami.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.esufam.megami.models.Post;
import com.esufam.megami.repositories.PostRepository;

@Controller
@RequestMapping(path = "/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewPost(@RequestParam String title, @RequestParam String filename, @RequestParam Integer userId) {
        Post p = new Post();
        p.setTitle(title);
        p.setFilename(filename);
        p.setUserId(userId);
        p.setStatus('A');
        postRepository.save(p);
        return "Saved";
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Post getPostById(@PathVariable Integer id) {
        return postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PutMapping("/{id}")
    public @ResponseBody Post updatePost(@PathVariable Integer id, @RequestParam String title, @RequestParam String filename, @RequestParam char status) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(title);
            post.setFilename(filename);
            post.setStatus(status);
            return postRepository.save(post);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deletePost(@PathVariable Integer id) {
        postRepository.deleteById(id);
        return "Deleted";
    }
}