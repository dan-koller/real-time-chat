package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent a message in the chat.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage {
    private String sender;
    private String message;
    private String date;
    private String sendTo;
    private MessageType messageType;
}
