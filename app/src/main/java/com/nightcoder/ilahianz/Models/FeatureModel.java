package com.nightcoder.ilahianz.Models;

public class FeatureModel {

    private String heading;
    private String content;
    private int imageId;
    private String buttonName;

    public FeatureModel(String heading, String content, int imageId, String buttonName) {
        this.heading = heading;
        this.content = content;
        this.imageId = imageId;
        this.buttonName = buttonName;
    }

    public FeatureModel() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
}
