package com.esufam.megami.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esufam.megami.dto.AuthDTO;
import com.esufam.megami.models.Response;
import com.esufam.megami.models.User;
import com.esufam.megami.repositories.UserRepository;
import com.esufam.megami.services.TokenService;
import com.esufam.megami.services.UserService;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/login")
    public ResponseEntity<Response> isLoggedIn(Principal principal) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", this.userService.getUserFromPrincipal(principal).getUsername());
        return ResponseEntity.ok(Response.success(data));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Response> login(@RequestBody AuthDTO userData) {
        if (isInvalid(userData)) {
            return ResponseEntity.badRequest().build();
        }
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userData.getUsername(), userData.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);

        return ResponseEntity.ok(Response.success(data));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Response> register(@RequestBody AuthDTO userData) {
        if (isInvalid(userData) || userRepository.findByUsername(userData.getUsername()) != null) {
            return ResponseEntity.badRequest().build();
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.getPassword());
        User user = this.toEntity(userData);
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return ResponseEntity.ok(Response.success(null));
    }

    private User toEntity(AuthDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    private boolean isInvalid(AuthDTO dto) {
        if (
            dto.getUsername() == null ||
            dto.getUsername().length() < 1 ||
            dto.getPassword() == null ||
            dto.getPassword().length() < 8 ||
            dto.getPassword().length() > 14
        ) {
            return true;
        }

        return false;
    }
}
