package com.nightcoder.ilahianz.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatModel {

    private String sender;
    private String receiver;
    private String message;
    private boolean isSeen;
    private boolean isDelivered;
    private String timestamp;
    private String reference;

    public ChatModel(String sender, String receiver, String message,
                     boolean isSeen, boolean isDelivered,
                     String timestamp, String reference) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
        this.isDelivered = isDelivered;
        this.timestamp = timestamp;
        this.reference = reference;
    }

    public ChatModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
