package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessageService {
    @Autowired
    public MessageRepository messageRepository;
    //    List<ChatMessage> publicMessages = new CopyOnWriteArrayList<>();
    Map<Map.Entry<String, String>, List<ChatMessage>> privateMessages = new ConcurrentHashMap<>();

    public List<ChatMessage> getPublicMessages() {
        return messageRepository.findByMessageTypeIsNullOrderByDateAsc();
    }

    public void addPublicMessage(ChatMessage msg) {
//        publicMessages.add(msg);
        messageRepository.save(msg);
    }

    public void addPrivateMessage(ChatMessage msg) {
        // Save the message to the database
        messageRepository.save(msg);

        // Store the message in the privateMessages map
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

    public List<ChatMessage> getPrivateMessages(String userA, String userB) {
        // Query the database for messages between userA and userB
        List<ChatMessage> messages = messageRepository.findBySenderAndSendToOrSenderAndSendToOrderByDateAsc(userA, userB, userB, userA);

        // Store the messages in the privateMessages map
        Map.Entry<String, String> entry = Map.entry(userA, userB);
        if (!privateMessages.containsKey(entry)) {
            entry = Map.entry(userB, userA);
        }
        privateMessages.put(entry, new CopyOnWriteArrayList<>(messages));

        // Return the messages
        return messages;
    }
}
