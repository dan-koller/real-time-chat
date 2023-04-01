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

    public List<ChatMessage> getPublicMessages() {
        return publicMessages;
    }

    public void addPublicMessage(ChatMessage msg) {
        publicMessages.add(msg);
    }

    public void addPrivateMessage(ChatMessage msg) {
        var entry = Map.entry(msg.getSender(), msg.getSendTo());

        if (!privateMessages.containsKey(entry)) {
            entry = Map.entry(msg.getSendTo(), msg.getSender());
        }

        if (privateMessages.containsKey(entry)) {
            privateMessages.get(entry).add(msg);
        } else {
            privateMessages.put(entry, new CopyOnWriteArrayList<>(List.of(msg)));
        }
    }

    public List<ChatMessage> getPrivateMessages(String userA, String userB) {
        var entry = Map.entry(userA, userB);

        if (!privateMessages.containsKey(entry)) {
            entry = Map.entry(userB, userA);
        }

        return privateMessages.get(entry);
    }
}
