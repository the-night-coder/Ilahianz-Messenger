package com.nightcoder.ilahianz.Models;

public class ChatListModel {
    private String id;
    private String lastMessage;
    private double time;

    public ChatListModel(String id, String lastMessage, double time) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public ChatListModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
