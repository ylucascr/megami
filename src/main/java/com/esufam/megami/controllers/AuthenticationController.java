package com.esufam.megami.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esufam.megami.dto.AuthDTO;
import com.esufam.megami.models.User;
import com.esufam.megami.repositories.UserRepository;
import com.esufam.megami.services.TokenService;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO userData) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userData.getUsername(), userData.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO userData) {
        if (userRepository.findByUsername(userData.getUsername()) != null) {
            return ResponseEntity.badRequest().build();
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.getPassword());
        User user = this.toEntity(userData);
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    private User toEntity(AuthDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
