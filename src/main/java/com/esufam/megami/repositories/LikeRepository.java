package com.esufam.megami.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esufam.megami.models.Like;


public interface LikeRepository extends CrudRepository<Like, Integer> {
    Optional<Like> findByPostIdAndUserId(Integer postId, Integer userId);
}