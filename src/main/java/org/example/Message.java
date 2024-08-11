package org.example;

public class Message {

    private final User sender;
    private final User receiver;
    private final String content;

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
