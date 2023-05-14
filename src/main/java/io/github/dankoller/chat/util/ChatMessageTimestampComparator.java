package io.github.dankoller.chat.util;

import io.github.dankoller.chat.model.ChatMessage;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ChatMessageTimestampComparator implements Comparator<ChatMessage> {
    /**
     * This method compares the date string of two ChatMessage objects. The date string is first converted to a
     * LocalDateTime object and then compared.
     *
     * @param message1 The first object to be compared
     * @param message2 The second object to be compared
     * @return Default comparator return value
     */
    @Override
    public int compare(ChatMessage message1, ChatMessage message2) {
        LocalDateTime dateTime1 = DateTimeUtils.fromString(message1.getDate());
        LocalDateTime dateTime2 = DateTimeUtils.fromString(message2.getDate());
        return dateTime1.compareTo(dateTime2);
    }
}
