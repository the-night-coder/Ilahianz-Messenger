package com.nightcoder.ilahianz.Models;

public class Comment {

    private String id;
    private String message;
    private double timestamp;

    public Comment(String id, String message, double timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
