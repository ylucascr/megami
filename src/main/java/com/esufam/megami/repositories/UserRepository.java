package com.esufam.megami.repositories;

import org.springframework.data.repository.CrudRepository;

import com.esufam.megami.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    Integer deleteByUsername(String username);
}