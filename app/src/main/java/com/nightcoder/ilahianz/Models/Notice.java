package com.nightcoder.ilahianz.Models;

public class Notice {

    public static int TYPE_IMAGE = 232;
    public static int TYPE_DOC = 212;
    public static int TYPE_AUDIO = 321;
    public static int TYPE_TEXT = 344;

    //

    public static String KEY_ID = "id";
    public static String KEY_CONTENT_TYPE = "contentType";
    public static String KEY_TIMESTAMP = "timestamp";
    public static String KEY_TARGET = "target";
    public static String KEY_CONTENT_PATH = "contentPath";
    public static String KEY_COMPOSER_ID = "composerId";
    public static String KEY_TEXT = "text";
    public static String KEY_SUBJECT = "subject";

    private String id;
    private int contentType;
    private double timestamp;
    private String target;
    private String contentPath;
    private String composerId;
    private String text;
    private String subject;

    public Notice() {
    }

    public Notice(String id, int contentType, double timestamp, String target,
                  String contentPath, String composerId, String text, String subject) {
        this.id = id;
        this.contentType = contentType;
        this.timestamp = timestamp;
        this.target = target;
        this.contentPath = contentPath;
        this.composerId = composerId;
        this.text = text;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getComposerId() {
        return composerId;
    }

    public void setComposerId(String composerId) {
        this.composerId = composerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
