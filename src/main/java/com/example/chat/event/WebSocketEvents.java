package com.example.chat.event;

import com.example.chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEvents {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to remove a user from the list of users when the user disconnects.
     *
     * @param event The event that is triggered when a user disconnects
     */
    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        String username = (String) StompHeaderAccessor.wrap(event.getMessage()).getSessionAttributes().get("username");
        userRepository.removeUser(username);
        messagingTemplate.convertAndSend("/chat/user-event", new UserEvent(username, UserEventType.LEFT));
    }
}
