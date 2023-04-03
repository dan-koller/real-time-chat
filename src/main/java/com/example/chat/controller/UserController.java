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

    /**
     * This method is used to get all the users that are currently online.
     *
     * @return A list of all the users that are currently online
     */
    @GetMapping("/users")
    public List<String> getOnlineUsers() {
        return userRepository.getUsers();
    }
}
