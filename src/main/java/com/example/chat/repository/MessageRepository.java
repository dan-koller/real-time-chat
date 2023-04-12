package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndSendToOrSenderAndSendToOrderByDateAsc(String sender, String sendTo, String sendTo2, String sender2);

    List<ChatMessage> findByMessageTypeIsNullOrderByDateAsc(); // public messages don't have a message type set
}
