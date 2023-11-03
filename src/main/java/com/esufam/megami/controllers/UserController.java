package com.esufam.megami.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esufam.megami.dto.UserDTO;
import com.esufam.megami.models.Response;
import com.esufam.megami.models.User;
import com.esufam.megami.repositories.UserRepository;
import com.esufam.megami.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @GetMapping(path = "/{username}")
    public ResponseEntity<Response> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", "User " + username + " not found");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(Response.success(this.toDTO(user)));
    }

    @PostMapping(value="/{username}/follow")
    public ResponseEntity<Response> followUser(Principal principal, @PathVariable String username) {
        User me = this.userService.getUserFromPrincipal(principal);
        Integer userId = this.userService.getUserIdFromUsername(username);

        if (userId == -1) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", "User " + username + " not found");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.NOT_FOUND);
        }

        if (me.getFollowedUserIds().contains(userId)) {
            me.getFollowedUserIds().remove(userId);
        } else {
            me.getFollowedUserIds().add(userId);
        }

        this.userRepository.save(me);

        return ResponseEntity.ok(Response.success(null));
    }
    

    @Transactional
    @DeleteMapping("/{username}")
    public ResponseEntity<Response> deleteUser(Principal principal, @PathVariable String username) {
        String me = this.userService.getUsernameFromPrincipal(principal);

        if (!username.equals(me)) {
            Map<String, Object> data = new HashMap<>();
            data.put("auth", "You cannot delete other users but yourself");
            return new ResponseEntity<>(Response.fail(data), HttpStatus.UNAUTHORIZED);
        }

        userRepository.deleteByUsername(username);
        return ResponseEntity.ok(Response.success(null));
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}