package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Chat {

    private final User u1;
    private final User u2;
    private final String chatId;
    private final Queue<Message> messages;
    private static final ArrayList<Chat> chats = new ArrayList<>();

    public Chat(User u1, User u2) {
        this.u1 = u1;
        this.u2 = u2;
        chats.add(this);
        messages = new LinkedList<>();
        chatId = u1.getUserId() + u2.getUserId();
    }

    public LinkedList<Message> getMessagesList() {
        return new LinkedList<>(messages);
    }

    public static Chat getChat(User u1, User u2) {
        String id1 = u1.getUserId() + u2.getUserId();
        String id2 = u2.getUserId() + u1.getUserId();

        var c = getChatById(id1);

        if (c == null) {
            c = getChatById(id2);
        }

        if (c == null) {
            c = new Chat(u1, u2);
        }

        chats.add(c);
        return c;
    }

    private static Chat getChatById(String chatId) {
        for (Chat c : chats) {
            if (c.getChatId().equals(chatId)) {
                return c;
            }
        }
        return null;
    }

    public String getChatId() {
        return chatId;
    }

    public Queue<Message> getMessages() {
        return messages;
    }
}
