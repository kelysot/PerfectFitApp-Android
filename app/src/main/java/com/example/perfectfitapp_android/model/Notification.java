package com.example.perfectfitapp_android.model;

public class Notification {

    String profileId, notificationType, date;

    public Notification(String profileId, String notificationType, String date) {
        this.profileId = profileId;
        this.notificationType = notificationType;
        this.date = date;
    }

    public Notification() {
    }

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
