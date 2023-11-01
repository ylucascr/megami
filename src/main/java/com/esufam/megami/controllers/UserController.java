package com.esufam.megami.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esufam.megami.dto.UserPatchDTO;
import com.esufam.megami.models.User;
import com.esufam.megami.repositories.UserRepository;
import com.esufam.megami.services.TokenService;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PatchMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserPatchDTO userData) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        this.patchFromDTO(user, userData);

        userRepository.save(user);

        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(token);
    }

    @Transactional
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userRepository.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    private void patchFromDTO(User user, UserPatchDTO dto) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getQuestion() != null) {
            user.setQuestion(dto.getQuestion());
        }
        if (dto.getAnswer() != null) {
            user.setAnswer(dto.getAnswer());
        }
    }
}