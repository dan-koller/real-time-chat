package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * This class is used to represent a message in the chat.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "messages")
public class ChatMessage {
    @MongoId
    private String id;
    private String sender;
    private String message;
    private String date;
    private String sendTo;
    private MessageType messageType;
}
