package com.example.chat.controller;

import com.example.chat.event.UserEvent;
import com.example.chat.event.UserEventType;
import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageType;
import com.example.chat.model.User;
import com.example.chat.persistence.MessagesRepository;
import com.example.chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessagesRepository messagesRepo;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.register")
    @SendTo("/chat/user-event")
    public UserEvent register(@Payload User user, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        simpMessageHeaderAccessor.getSessionAttributes().put("username", user.getUsername());
        userRepository.addUser(user.getUsername());
        return new UserEvent(user.getUsername(), UserEventType.JOINED);
    }

    @MessageMapping("/chat.send")
    @SendTo("/chat/public")
    public ChatMessage send(@Payload ChatMessage msg) {
        messagesRepo.addPublicMessage(msg);
        return msg;
    }

    @MessageMapping("/chat.send.private")
    public void sendTo(@Payload ChatMessage msg) {
        if (msg.getMessageType() == MessageType.GET_ALL) {
            var messages = messagesRepo.getPrivateMessages(msg.getSender(), msg.getSendTo());
            if (messages != null) {
                messagingTemplate.convertAndSendToUser(msg.getSender(), "/private", Map.of("messageType", MessageType.GET_ALL, "messages", messages));
            }
        } else if (msg.getMessageType() == MessageType.PRIVATE) {
            messagesRepo.addPrivateMessage(msg);
            var response = Map.of("messageType", MessageType.PRIVATE, "messages", List.of(msg));
            messagingTemplate.convertAndSendToUser(msg.getSendTo(), "/private", response);
            messagingTemplate.convertAndSendToUser(msg.getSender(), "/private", response);
        }
    }
}
