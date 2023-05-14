package io.github.dankoller.chat.repository;

import io.github.dankoller.chat.model.ChatMessage;
import io.github.dankoller.chat.model.MessageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndSendToOrSenderAndSendTo(String sender, String sendTo, String sendTo2, String sender2);

    List<ChatMessage> findByMessageTypeEquals(MessageType messageType);
}
