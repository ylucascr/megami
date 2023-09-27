package com.esufam.megami.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esufam.megami.dto.PostDTO;
import com.esufam.megami.models.Like;
import com.esufam.megami.models.Post;
import com.esufam.megami.repositories.LikeRepository;
import com.esufam.megami.repositories.PostRepository;

@RestController
@RequestMapping(path = "/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<Post> addNewPost(@RequestBody PostDTO postData) {
        if (postData.isMissingData()) {
            return ResponseEntity.badRequest().build();
        }
        Post p = new Post();
        p.setTitle(postData.title());
        p.setFilename(postData.filename());
        p.setUserId(postData.userId());
        p.setStatus('A');
        return ResponseEntity.ok(postRepository.save(p));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Post>> getAllPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody PostDTO postData) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        if (postData.title() != null) {
            post.setTitle(postData.title());
        }
        if (postData.filename() != null) {
            post.setFilename(postData.filename());
        }
        if (postData.userId() != null) {
            post.setUserId(postData.userId());
        }
        if (postData.status() != 0) {
            post.setStatus(postData.status());
        }
        return ResponseEntity.ok(postRepository.save(post));
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<String> togglePostLike(@PathVariable Integer id, @RequestBody PostDTO postData) {
        if (postData.userId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Like like = likeRepository.findByPostIdAndUserId(id, postData.userId()).orElse(null);
        if (like == null) {
            like = new Like();
            like.setPostId(id);
            like.setUserId(postData.userId());
            likeRepository.save(like);
        } else {
            likeRepository.delete(like);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        postRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}