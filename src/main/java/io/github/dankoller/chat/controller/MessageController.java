package io.github.dankoller.chat.controller;

import io.github.dankoller.chat.model.ChatMessage;
import io.github.dankoller.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * This method is used to get all the public messages from the database.
     *
     * @return A list of all the public messages
     */
    @GetMapping("/public-messages")
    public List<ChatMessage> getPublicMessages() {
        return messageService.getPublicMessages();
    }
}