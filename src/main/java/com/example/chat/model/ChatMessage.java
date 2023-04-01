package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
