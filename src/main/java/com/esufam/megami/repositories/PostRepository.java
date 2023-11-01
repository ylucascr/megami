package com.esufam.megami.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esufam.megami.models.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAll();
    Optional<Post> findByFilename(String Filename);
}
