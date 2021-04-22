package com.nmpdev.chat.model;

import java.util.Date;

public class Message {

    private String text;
    private String imageUrl;
    private long timestamp;
    private User user;

    public Message() {

    }

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
        this.timestamp = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
