package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.persistence.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessagesRepository messagesRepository;

    @GetMapping("/public-messages")
    public List<ChatMessage> getPublicMessages() {
        return messagesRepository.getPublicMessages();
    }
}