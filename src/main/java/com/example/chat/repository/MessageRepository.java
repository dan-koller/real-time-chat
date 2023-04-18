package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndSendToOrSenderAndSendToOrderByDateAsc(String sender, String sendTo, String sendTo2, String sender2);

    List<ChatMessage> findByMessageTypeEqualsOrderByDateAsc(MessageType messageType);
}
