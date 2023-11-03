package com.esufam.megami.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esufam.megami.dto.PostGetDTO;
import com.esufam.megami.dto.PostPostDTO;
import com.esufam.megami.models.Post;
import com.esufam.megami.models.Response;
import com.esufam.megami.models.User;
import com.esufam.megami.repositories.PostRepository;
import com.esufam.megami.services.UserService;
import com.esufam.megami.storage.StorageService;

@Controller
@RequestMapping(path = "/posts")
public class PostController {
    @Autowired
    private PostRepository repository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    private Sort sortByLastModified() {
        return Sort.by(Sort.Direction.DESC, "updatedAt");
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Response> all() {
        List<PostGetDTO> posts = this.repository.findAll(this.sortByLastModified())
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(Response.success(posts));
    }

    @GetMapping(path = "/feed")
    public @ResponseBody ResponseEntity<Response> feed(Principal principal) {
        User me = this.userService.getUserFromPrincipal(principal);
        List<PostGetDTO> posts = this.repository.findAllByUserIdIn(me.getFollowedUserIds(), this.sortByLastModified())
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(Response.success(posts));
    }

    @GetMapping(path = "/from/{username}")
    public @ResponseBody ResponseEntity<Response> byUser(@PathVariable String username) {
        Integer userId = this.userService.getUserIdFromUsername(username);
        if (userId == -1) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", "User " + username + " not found");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.NOT_FOUND);
        }

        List<PostGetDTO> posts = this.repository.findAllByUserId(userId, this.sortByLastModified())
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(Response.success(posts));
    }

    @GetMapping(path = "/{filename}")
    public @ResponseBody ResponseEntity<Response> one(@PathVariable String filename) {
        Post post = this.repository.findByFilename(filename).orElse(null);
        if (post == null) {
            Map<String, Object> data = new HashMap<>();
            data.put("filename", "File " + filename + " not found");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(Response.success(this.toDTO(post)));
    }

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Response> add(@ModelAttribute PostPostDTO post) {
        String filename = this.storageService.store(post.getFile());

        Post newPost = new Post();
        newPost.setTitle(post.getTitle());
        newPost.setDescription(post.getDescription());
        newPost.setFilename(filename);
        
        this.repository.save(newPost);

        return ResponseEntity.ok(Response.success(null));
    }

    @PatchMapping(path = "/{filename}")
    public @ResponseBody ResponseEntity<Response> edit(@PathVariable String filename, @ModelAttribute PostPostDTO newPost) {
        Post post = this.repository.findByFilename(filename).orElse(null);
        if (post == null) {
            Map<String, Object> data = new HashMap<>();
            data.put("filename", "File " + filename + " not found");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.NOT_FOUND);
        }
        this.patchPostFromDTO(newPost, post);
        return ResponseEntity.ok(
            Response.success(this.toDTO(this.repository.save(post)))
        );
    }

    @DeleteMapping(path = "/{filename}")
    public @ResponseBody ResponseEntity<Response> delete(@PathVariable String filename) {
        Optional<Post> post = this.repository.findByFilename(filename);
        if (post.isPresent()) {
            String filenameToDelete = post.get().getFilename();
            this.storageService.delete(filenameToDelete);
            this.repository.delete(post.get());
        }

        return ResponseEntity.ok(Response.success(null));
    }

    private void patchPostFromDTO(PostPostDTO dto, Post post) {
        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            post.setDescription(dto.getDescription());
        }
        if (dto.getFile() != null) {
            this.storageService.delete(post.getFilename());
            String filename = this.storageService.store(dto.getFile());
            post.setFilename(filename);
        }
    }

    private PostGetDTO toDTO(Post post) {
        PostGetDTO dto = new PostGetDTO();
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setFilename(post.getFilename());
        dto.setUploader(this.userService.getUsernameFromUserId(post.getUserId()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}
