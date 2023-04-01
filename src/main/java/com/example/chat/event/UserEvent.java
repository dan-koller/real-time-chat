package com.example.chat.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEvent {
    private String username;
    private UserEventType userEventType;
}
