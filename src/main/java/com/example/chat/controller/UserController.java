package com.example.chat.controller;

import com.example.chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<String> getOnlineUsers() {
        return userRepository.getUsers();
    }
}
