package com.esufam.megami.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esufam.megami.models.User;
import com.esufam.megami.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return this.userRepository.findByUsername(username);
    }
}
