package com.esufam.megami.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.esufam.megami.models.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAll();
    List<Post> findAllByUserId(Integer userId);
    List<Post> findAllByUserIdIn(Set<Integer> userIds);
    Optional<Post> findByFilename(String Filename);
}
