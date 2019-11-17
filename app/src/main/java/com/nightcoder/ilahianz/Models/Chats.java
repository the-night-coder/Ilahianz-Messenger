package com.nightcoder.ilahianz.Models;

public class Chats {

    private String sender;
    private String receiver;
    private String message;
    private boolean isSeen;
    private boolean isDelivered;
    private boolean isSent;
    private String timestamp;
    private String reference;
    private String link;

    public Chats() {
    }

    public Chats(String sender, String receiver, String message,
                 String timestamp, String reference, String link,
                 boolean isSeen, boolean isDelivered, boolean isSent) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
        this.link = link;
        this.isSent = isSent;
        this.isDelivered = isDelivered;
        this.timestamp = timestamp;
        this.reference = reference;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
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

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
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
