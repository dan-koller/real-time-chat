package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is used to represent a user in the chat.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "users")
public class User {
    private String username;
}
