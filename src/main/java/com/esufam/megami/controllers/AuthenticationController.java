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

import com.esufam.megami.dto.UserDTO;
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
    public ResponseEntity<String> login(@RequestBody UserDTO userData) {
        if (userData.missingLoginInfo()) {
            return ResponseEntity.badRequest().build();
        }
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userData.username(), userData.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userData) {
        if (
            userData.missingRegisterInfo() ||
            userRepository.findByUsername(userData.username()) != null
        ) {
            return ResponseEntity.badRequest().build();
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.password());
        User user = new User();
        user.setUsername(userData.username());
        user.setEmail(userData.email());
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
