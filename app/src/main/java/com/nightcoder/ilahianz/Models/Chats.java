package com.nightcoder.ilahianz.Models;

public class Chats {

    private String sender;
    private String receiver;
    private String message;
    private double isSeen;
    private double isDelivered;
    private double isSent;
    private String timestamp;
    private String reference;
    private String link;
    private int type;
    private String url;

    public Chats(String sender, String receiver, String message, double isSeen,
                 double isDelivered, double isSent, String timestamp, String reference, String link, int type, String url) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
        this.isDelivered = isDelivered;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.reference = reference;
        this.link = link;
        this.type = type;
        this.url = url;
    }

    public Chats() {
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

    public double getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(double isSeen) {
        this.isSeen = isSeen;
    }

    public double getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(double isDelivered) {
        this.isDelivered = isDelivered;
    }

    public double getIsSent() {
        return isSent;
    }

    public void setIsSent(double isSent) {
        this.isSent = isSent;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
