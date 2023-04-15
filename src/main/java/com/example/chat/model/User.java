package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent a user in the chat.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String username;
}
