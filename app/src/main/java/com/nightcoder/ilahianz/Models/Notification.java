package com.nightcoder.ilahianz.Models;

public class Notification {

    public static final int TYPE_THANKS = 323;
    public static final int TYPE_COMMENT = 322;
    public static final int TYPE_BLOOD_REQUEST = 321;

    private String key;
    private String message;
    private String id;
    private int type;
    private String ref;
    private double timestamp;
    private boolean seen;
    private String username;

    public Notification(){

    }

    public Notification(String key, String message, String id,
                        int type, String ref, double timestamp, boolean seen, String username) {
        this.key = key;
        this.message = message;
        this.id = id;
        this.type = type;
        this.ref = ref;
        this.timestamp = timestamp;
        this.seen = seen;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
