package com.esufam.megami.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.esufam.megami.repositories.PostRepository;
import com.esufam.megami.storage.StorageService;
import com.esufam.megami.storage.exceptions.FileNotFoundInDatabaseException;

@Controller
@RequestMapping(path = "/posts")
public class PostController {
    @Autowired
    private PostRepository repository;

    @Autowired
    private StorageService storageService;

    @GetMapping(path = "/all")
    public @ResponseBody List<Post> all() {
        return this.repository.findAll()
            .stream()
            .collect(Collectors.toList());
    }

    @GetMapping(path = "/{filename}")
    public @ResponseBody PostGetDTO one(@PathVariable String filename) {
        Post post = this.repository.findByFilename(filename).orElseThrow(() -> new FileNotFoundInDatabaseException(filename));
        return this.toDTO(post);
    }

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody String add(@ModelAttribute PostPostDTO post) {
        String filename = this.storageService.store(post.getFile());

        Post newPost = new Post();
        newPost.setTitle(post.getTitle());
        newPost.setDescription(post.getDescription());
        newPost.setFilename(filename);
        
        this.repository.save(newPost);

        return "Saved!";
    }

    @PatchMapping(path = "/{filename}")
    public @ResponseBody PostGetDTO edit(@PathVariable String filename, @ModelAttribute PostPostDTO newPost) {
        return this.repository.findByFilename(filename)
            .map(post -> {
                this.patchPostFromDTO(newPost, post);
                return this.repository.save(post);
            })
            .map(this::toDTO)
            .orElseThrow(() -> new FileNotFoundInDatabaseException(filename));
    }

    @DeleteMapping(path = "/{filename}")
    public @ResponseBody void delete(@PathVariable String filename) {
        Optional<Post> post = this.repository.findByFilename(filename);
        if (post.isPresent()) {
            String filenameToDelete = post.get().getFilename();
            this.storageService.delete(filenameToDelete);
            this.repository.delete(post.get());
        }
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
        dto.setUserId(post.getUserId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}
