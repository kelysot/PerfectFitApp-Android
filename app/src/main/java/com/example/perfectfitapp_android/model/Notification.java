package com.example.perfectfitapp_android.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification {

    String notificationId, profileIdFrom, profileIdMine, notificationType, date;

    public Notification(String notificationId, String profileIdFrom, String profileIdMine, String notificationType, String date) {
        this.notificationId =  notificationId;
        this.profileIdMine = profileIdMine;
        this.profileIdFrom = profileIdFrom;
        this.notificationType = notificationType;
        this.date = date;
    }

    public Notification() {}

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getProfileIdFrom() {
        return profileIdFrom;
    }

    public void setProfileIdFrom(String profileIdFrom) {
        this.profileIdFrom = profileIdFrom;
    }

    /*------------------------------------------------------*/

    public String getProfileIdMine() {
        return profileIdMine;
    }

    public void setProfileIdMine(String profileIdMine) {
        this.profileIdMine = profileIdMine;
    }

    /*------------------------------------------------------*/

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    /*------------------------------------------------------*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*------------------------------------------------------*/

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /*------------------------------------------------------*/

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", profileIdFrom='" + profileIdFrom + '\'' +
                ", profileIdMine='" + profileIdMine + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    /*------------------------------------------------------*/

    public HashMap<String, Object> toJson(){

        HashMap<String, Object> map = new HashMap<>();

        map.put("_id", this.getNotificationId());
        map.put("profileIdMine", this.getProfileIdMine());
        map.put("profileIdFrom", this.getProfileIdFrom());
        map.put("notificationType", this.getNotificationType());
        map.put("date", this.getDate());

        return map;
    }


    public static Notification jsonObjectToNotification(JsonObject notificationJson){
        String notificationId = notificationJson.get("_id").getAsString();
        String profileIdMine = notificationJson.get("profileIdMine").getAsString();
        String profileIdFrom = notificationJson.get("profileIdFrom").getAsString();
        String notificationType = notificationJson.get("notificationType").getAsString();
        String date = notificationJson.get("date").getAsString();

        Notification notification =  new Notification(notificationId, profileIdFrom, profileIdMine, notificationType, date);
        return notification;
    }
    /*------------------------------------------------------*/

    public static Notification jsonElementToNotification(JsonElement notificationJson){
        String notificationId = notificationJson.getAsJsonObject().get("_id").getAsString();
        String profileIdMine = notificationJson.getAsJsonObject().get("profileIdMine").getAsString();
        String profileIdFrom = notificationJson.getAsJsonObject().get("profileIdFrom").getAsString();
        String notificationType = notificationJson.getAsJsonObject().get("notificationType").getAsString();
        String date = notificationJson.getAsJsonObject().get("date").getAsString();

        Notification notification =  new Notification(notificationId, profileIdFrom, profileIdMine, notificationType, date);
        return notification;
    }

    /*------------------------------------------------------*/

    public static List<Notification> jsonArrayToNotification(JsonArray notificationJson){
        List<Notification> notificationList = new ArrayList<>();
        for (int i = 0; i < notificationJson.size(); i++) {
            notificationList.add(Notification.jsonElementToNotification(notificationJson.get(i)));
        }
        return notificationList;
    }
}
