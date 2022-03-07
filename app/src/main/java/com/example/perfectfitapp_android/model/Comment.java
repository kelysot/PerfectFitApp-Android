package com.example.perfectfitapp_android.model;

public class Comment {

    String profileId, date, text;

    public Comment(String profileId, String date, String text) {
        this.profileId = profileId;
        this.date = date;
        this.text = text;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
