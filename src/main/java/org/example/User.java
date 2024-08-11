package org.example;

import java.util.UUID;

public class User {

    private final String userId;
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        userId = UUID.randomUUID().toString();
    }

    public void sendMessage(User receiver, Message message) {
        var chat = Chat.getChat(this, receiver);
        chat.getMessages().add(message);
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
