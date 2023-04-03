package com.example.chat.persistence;

import com.example.chat.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesRepository {
    List<ChatMessage> publicMessages = new CopyOnWriteArrayList<>();
    Map<Map.Entry<String, String>, List<ChatMessage>> privateMessages = new ConcurrentHashMap<>();

    /**
     * This method is used to get all the public messages from the database.
     *
     * @return A list of all the public messages
     */
    public List<ChatMessage> getPublicMessages() {
        return publicMessages;
    }

    /**
     * This method is used to add a public message to the database.
     *
     * @param msg The message that is added
     */
    public void addPublicMessage(ChatMessage msg) {
        publicMessages.add(msg);
    }

    /**
     * This method is used to add a private message to the database.
     *
     * @param msg The message that is added
     */
    public void addPrivateMessage(ChatMessage msg) {
        Map.Entry<String, String> entry = Map.entry(msg.getSender(), msg.getSendTo());
        if (!privateMessages.containsKey(entry)) {
            entry = Map.entry(msg.getSendTo(), msg.getSender());
        }
        if (privateMessages.containsKey(entry)) {
            privateMessages.get(entry).add(msg);
        } else {
            privateMessages.put(entry, new CopyOnWriteArrayList<>(List.of(msg)));
        }
    }

    /**
     * This method is used to get all the private messages between two users.
     *
     * @param userA The first user
     * @param userB The second user
     * @return A list of all the private messages between the two users
     */
    public List<ChatMessage> getPrivateMessages(String userA, String userB) {
        Map.Entry<String, String> entry = Map.entry(userA, userB);
        if (!privateMessages.containsKey(entry)) {
            entry = Map.entry(userB, userA);
        }
        return privateMessages.get(entry);
    }
}
