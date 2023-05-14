package io.github.dankoller.chat.controller;

import io.github.dankoller.chat.event.UserEvent;
import io.github.dankoller.chat.event.UserEventType;
import io.github.dankoller.chat.model.ChatMessage;
import io.github.dankoller.chat.model.MessageType;
import io.github.dankoller.chat.model.User;
import io.github.dankoller.chat.service.MessageService;
import io.github.dankoller.chat.service.UserService;
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
    private MessageService messageService;
    @Autowired
    private UserService userService;

    /**
     * This method is used to register a user in the chat and to inform all subscribers that a new user has joined the
     * chat. The session attributes are used to store the username of the user.
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
        userService.addUser(user.getUsername());
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
        messageService.addPublicMessage(msg);
        return msg;
    }

    /**
     * This method is used to exchange private messages between two users.
     * GET_ALL is used to get all the messages between the sender and the receiver (for chat history).
     * PRIVATE is used to send a private message to the receiver.
     *
     * @param msg The message that is sent
     */
    @MessageMapping("/chat.send.private")
    public void sendTo(@Payload ChatMessage msg) {
        if (msg.getMessageType() == MessageType.GET_ALL) {
            List<ChatMessage> messages = messageService.getPrivateMessages(msg.getSender(), msg.getSendTo());
            if (messages != null) {
                messagingTemplate.convertAndSendToUser(msg.getSender(), "/private",
                        Map.of("messageType", MessageType.GET_ALL, "messages", messages));
            }
        } else if (msg.getMessageType() == MessageType.PRIVATE) {
            messageService.addPrivateMessage(msg);
            Map<String, Object> response = Map.of("messageType", MessageType.PRIVATE, "messages", List.of(msg));
            messagingTemplate.convertAndSendToUser(msg.getSendTo(), "/private", response);
            messagingTemplate.convertAndSendToUser(msg.getSender(), "/private", response);
        }
    }
}
