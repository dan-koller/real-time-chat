package com.example.chat.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent a user event. This is used to inform all subscribers that a user has joined or left
 * the chat.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEvent {
    private String username;
    private UserEventType userEventType;
}
