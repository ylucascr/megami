package com.esufam.megami.repositories;

import org.springframework.data.repository.CrudRepository;

import com.esufam.megami.models.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {

}