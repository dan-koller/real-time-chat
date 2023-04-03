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
import java.util.Objects;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to register a user in the chat. It will add the user to the list of users.
     * It will also inform all subscribers that a new user has joined the chat.
     * The session attributes are used to store the username of the user.
     * The null check is to improve the code quality. It is not necessary.
     *
     * @param user                      The user that is joining the chat
     * @param simpMessageHeaderAccessor The accessor that is used to access the session attributes
     * @return The user event that is sent to all subscribers
     */
    @MessageMapping("/chat.register")
    @SendTo("/chat/user-event")
    public UserEvent register(@Payload User user, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Map<String, Object> sessionAttributes = simpMessageHeaderAccessor.getSessionAttributes();
        if (Objects.isNull(sessionAttributes)) {
            return null;
        }
        sessionAttributes.put("username", user.getUsername());
//        simpMessageHeaderAccessor.getSessionAttributes().put("username", user.getUsername());
        userRepository.addUser(user.getUsername());
        return new UserEvent(user.getUsername(), UserEventType.JOINED);
    }

    /**
     * This method is used to add a message to the public chat for chat history.
     * It will also send the message to all subscribers.
     *
     * @param msg The message that is sent
     * @return The message that is sent
     */
    @MessageMapping("/chat.send")
    @SendTo("/chat/public")
    public ChatMessage send(@Payload ChatMessage msg) {
        messagesRepository.addPublicMessage(msg);
        return msg;
    }

    /**
     * This method is used to send a private message to a user.
     * It will also send the message to the sender and the receiver.
     * There are two message types: GET_ALL and PRIVATE.
     * GET_ALL is used to get all the messages between the sender and the receiver.
     * PRIVATE is used to send a private message to the receiver.
     *
     * @param msg The message that is sent
     */
    @MessageMapping("/chat.send.private")
    public void sendTo(@Payload ChatMessage msg) {
        if (msg.getMessageType() == MessageType.GET_ALL) {
            List<ChatMessage> messages = messagesRepository.getPrivateMessages(msg.getSender(), msg.getSendTo());
            if (messages != null) {
                messagingTemplate.convertAndSendToUser(msg.getSender(), "/private",
                        Map.of("messageType", MessageType.GET_ALL, "messages", messages));
            }
        } else if (msg.getMessageType() == MessageType.PRIVATE) {
            messagesRepository.addPrivateMessage(msg);
            Map<String, Object> response = Map.of("messageType", MessageType.PRIVATE, "messages", List.of(msg));
            messagingTemplate.convertAndSendToUser(msg.getSendTo(), "/private", response);
            messagingTemplate.convertAndSendToUser(msg.getSender(), "/private", response);
        }
    }
}
