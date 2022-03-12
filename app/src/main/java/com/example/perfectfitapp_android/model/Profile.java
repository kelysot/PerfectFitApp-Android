package com.example.perfectfitapp_android.model;

import android.util.Log;

import com.example.perfectfitapp_android.profile.ProfileFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile {

    //TODO:Add profileId in profile model in server side.
    String userId, profileId, firstName, lastName,gender, userName, birthday, userImageUrl;
    String shoulder, chest, basin, waist, foot, height, weight, bodyType, status;
    ArrayList<String> similarProfileId;
    ArrayList<String> followers, trackers;
    ArrayList<String> notifications;
    ArrayList<String> wishlist;
    ArrayList<String> myPostsListId;

    public Profile() {
        this.userId = "";
        this.profileId = "";
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.userName = "";
        this.birthday = "";
        this.userImageUrl = "";
        this.shoulder = "";
        this.chest = "";
        this.basin = "";
        this.waist = "";
        this.foot = "";
        this.height = "";
        this.weight = "";
        this.bodyType = "";

        this.status = "active";

        this.similarProfileId = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.trackers = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.wishlist = new ArrayList<>();
        this.myPostsListId = new ArrayList<>();
    }

    public Profile(String userId, String profileId, String firstName, String lastName, String gender, String userName,
                   String birthday, String userImageUrl, String shoulder, String chest, String basin,
                   String waist, String foot, String height, String weight, String bodyType) {

        this.userId = userId;
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.userName = userName;
        this.birthday = birthday;
        this.userImageUrl = userImageUrl;
        this.shoulder = shoulder;
        this.chest = chest;
        this.basin = basin;
        this.waist = waist;
        this.foot = foot;
        this.height = height;
        this.weight = weight;
        this.bodyType = bodyType;

        this.status = "active";

        this.similarProfileId = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.trackers = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.wishlist = new ArrayList<>();
        this.myPostsListId = new ArrayList<>();
    }
    public Profile(String userId, String profileId, String firstName, String lastName, String gender, String userName,
                   String birthday, String userImageUrl, String shoulder, String chest, String basin,
                   String waist, String foot, String height, String weight, String bodyType, String status, ArrayList<String> similarProfileId,
                   ArrayList<String> followers, ArrayList<String> trackers, ArrayList<String> notifications,
                   ArrayList<String> wishlist, ArrayList<String> myPostsListId) {

        this.userId = userId;
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.userName = userName;
        this.birthday = birthday;
        this.userImageUrl = userImageUrl;
        this.shoulder = shoulder;
        this.chest = chest;
        this.basin = basin;
        this.waist = waist;
        this.foot = foot;
        this.height = height;
        this.weight = weight;
        this.bodyType = bodyType;

        this.status = status;

        this.similarProfileId = similarProfileId;
        this.followers = followers;
        this.trackers = trackers;
        this.notifications = notifications;
        this.wishlist = wishlist;
        this.myPostsListId =myPostsListId;
    }

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*------------------------------------------------------*/

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /*------------------------------------------------------*/

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /*------------------------------------------------------*/

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /*------------------------------------------------------*/

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /*------------------------------------------------------*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*------------------------------------------------------*/

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /*------------------------------------------------------*/

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    /*------------------------------------------------------*/

    public String getShoulder() {
        return shoulder;
    }

    public void setShoulder(String shoulder) {
        this.shoulder = shoulder;
    }

    /*------------------------------------------------------*/

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    /*------------------------------------------------------*/

    public String getBasin() {
        return basin;
    }

    public void setBasin(String basin) {
        this.basin = basin;
    }

    /*------------------------------------------------------*/

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    /*------------------------------------------------------*/

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    /*------------------------------------------------------*/

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    /*------------------------------------------------------*/

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    /*------------------------------------------------------*/

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    /*------------------------------------------------------*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getSimilarProfileId() {
        return similarProfileId;
    }

    public void setSimilarProfileId(ArrayList<String> similarProfileId) {
        this.similarProfileId = similarProfileId;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getTrackers() {
        return trackers;
    }

    public void setTrackers(ArrayList<String> trackers) {
        this.trackers = trackers;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getMyPostsListId() {
        return myPostsListId;
    }

    public void setMyPostsListId(ArrayList<String> myPostsListId) {
        this.myPostsListId = myPostsListId;
    }

    public static Profile jsonElementToProfile(JsonElement profilesJson){
        String userId = profilesJson.getAsJsonObject().get("_id").getAsString();
        String profileId = profilesJson.getAsJsonObject().get("profileId").getAsString();
        String firstName = profilesJson.getAsJsonObject().get("firstName").getAsString();
        String lastName = profilesJson.getAsJsonObject().get("lastName").getAsString();
        String gender = profilesJson.getAsJsonObject().get("gender").getAsString();
        String userName = profilesJson.getAsJsonObject().get("userName").getAsString();
        String birthday = profilesJson.getAsJsonObject().get("birthday").getAsString();
        String userImageUrl = profilesJson.getAsJsonObject().get("userImageUrl").getAsString();
        String shoulder = profilesJson.getAsJsonObject().get("shoulder").getAsString();
        String chest = profilesJson.getAsJsonObject().get("chest").getAsString();
        String basin = profilesJson.getAsJsonObject().get("basin").getAsString();
        String waist = profilesJson.getAsJsonObject().get("waist").getAsString();
        String foot = profilesJson.getAsJsonObject().get("foot").getAsString();
        String height = profilesJson.getAsJsonObject().get("height").getAsString();
        String weight = profilesJson.getAsJsonObject().get("weight").getAsString();
        String bodyType = profilesJson.getAsJsonObject().get("bodyType").getAsString();
        String status = profilesJson.getAsJsonObject().get("status").getAsString();

        JsonElement similarProfileIdJson = profilesJson.getAsJsonObject().get("similarProfileId");
        ArrayList<String> similarProfileId = new ArrayList<>();
        if(!similarProfileIdJson.toString().equals("null") || !similarProfileIdJson.isJsonNull()){
            for (JsonElement similarProfile : similarProfileIdJson.getAsJsonArray()) {
                similarProfileId.add(similarProfile.getAsString());
            }
        }

        JsonElement followersJson = profilesJson.getAsJsonObject().get("followers");
        ArrayList<String> followers = new ArrayList<>();
        if(!followersJson.toString().equals("null") || !followersJson.isJsonNull()){
            for (JsonElement follower : followersJson.getAsJsonArray()) {
                followers.add(follower.getAsString());
            }
        }

        JsonElement trackersJson = profilesJson.getAsJsonObject().get("trackers");
        ArrayList<String> trackers = new ArrayList<>();
        if(!trackersJson.toString().equals("null") || !trackersJson.isJsonNull()){
            for (JsonElement tracker : trackersJson.getAsJsonArray()) {
                trackers.add(tracker.getAsString());
            }
        }

        JsonElement notificationsJson = profilesJson.getAsJsonObject().get("notifications");
        ArrayList<String> notifications = new ArrayList<>();
        if(!notificationsJson.toString().equals("null") || !notificationsJson.isJsonNull()){
            for (JsonElement notification : notificationsJson.getAsJsonArray()) {
                notifications.add(notification.getAsString());
            }
        }

        JsonElement wishlistJson = profilesJson.getAsJsonObject().get("wishlist");
        ArrayList<String> wishlist = new ArrayList<>();
        if(!wishlistJson.toString().equals("null") || !wishlistJson.isJsonNull()){
            for (JsonElement wishlist1 : wishlistJson.getAsJsonArray()) {
                wishlist.add(wishlist1.getAsString());
            }
        }

        JsonElement myPostsListIdJson = profilesJson.getAsJsonObject().get("myPostsListId");
        ArrayList<String> myPostsListId = new ArrayList<>();
        if(!myPostsListIdJson.toString().equals("null") || !myPostsListIdJson.isJsonNull()){
            for (JsonElement myPost : myPostsListIdJson.getAsJsonArray()) {
                myPostsListId.add(myPost.getAsString());
            }
        }

        Profile profile = new Profile(userId, profileId, firstName, lastName, gender, userName,
                birthday, userImageUrl, shoulder, chest, basin, waist, foot, height,
                weight, bodyType, status, similarProfileId, followers, trackers, notifications,
                wishlist, myPostsListId);
        return profile;
    }

    public static Profile jsonObjectToProfile(JsonObject profilesJson){
        String userId = profilesJson.get("_id").getAsString();
        String firstName = profilesJson.get("firstName").getAsString();
        String lastName = profilesJson.get("lastName").getAsString();
        String gender = profilesJson.get("gender").getAsString();
        String userName = profilesJson.get("userName").getAsString();
        String birthday = profilesJson.get("birthday").getAsString();
        String userImageUrl = profilesJson.get("pictureUrl").getAsString();
        String shoulder = profilesJson.get("shoulder").getAsString();
        String chest = profilesJson.get("chest").getAsString();
        String basin = profilesJson.get("basin").getAsString();
        String waist = profilesJson.get("waist").getAsString();
        String foot = profilesJson.get("foot").getAsString();
        String height = profilesJson.get("height").getAsString();
        String weight = profilesJson.get("weight").getAsString();
        String bodyType = profilesJson.get("bodyType").getAsString();
        String status = profilesJson.get("status").getAsString();

        JsonElement similarProfileIdJson = profilesJson.get("similarProfileId");
        ArrayList<String> similarProfileId = new ArrayList<>();
        if(!similarProfileIdJson.toString().equals("null") || !similarProfileIdJson.isJsonNull()){
            for (JsonElement similarProfile : similarProfileIdJson.getAsJsonArray()) {
                similarProfileId.add(similarProfile.getAsString());
            }
        }

        JsonElement followersJson = profilesJson.get("followers");
        ArrayList<String> followers = new ArrayList<>();
        if(!followersJson.toString().equals("null") || !followersJson.isJsonNull()){
            for (JsonElement follower : followersJson.getAsJsonArray()) {
                followers.add(follower.getAsString());
            }
        }

        JsonElement trackersJson = profilesJson.get("trackers");
        ArrayList<String> trackers = new ArrayList<>();
        if(!trackersJson.toString().equals("null") || !trackersJson.isJsonNull()){
            for (JsonElement tracker : trackersJson.getAsJsonArray()) {
                trackers.add(tracker.getAsString());
            }
        }

        JsonElement notificationsJson = profilesJson.get("notifications");
        ArrayList<String> notifications = new ArrayList<>();
        if(!notificationsJson.toString().equals("null") || !notificationsJson.isJsonNull()){
            for (JsonElement notification : notificationsJson.getAsJsonArray()) {
                notifications.add(notification.getAsString());
            }
        }

        JsonElement wishlistJson = profilesJson.get("wishlist");
        ArrayList<String> wishlist = new ArrayList<>();
        if(!wishlistJson.toString().equals("null") || !wishlistJson.isJsonNull()){
            for (JsonElement wishlist1 : wishlistJson.getAsJsonArray()) {
                wishlist.add(wishlist1.getAsString());
            }
        }

        JsonElement myPostsListIdJson = profilesJson.get("myPostsListId");
        ArrayList<String> myPostsListId = new ArrayList<>();
        if(!myPostsListIdJson.toString().equals("null") || !myPostsListIdJson.isJsonNull()){
            for (JsonElement myPost : myPostsListIdJson.getAsJsonArray()) {
                myPostsListId.add(myPost.getAsString());
            }
        }

        Profile profile = new Profile(userId, null, firstName, lastName, gender, userName,
                birthday, userImageUrl, shoulder, chest, basin, waist, foot, height,
                weight, bodyType, status, similarProfileId, followers, trackers, notifications,
                wishlist, myPostsListId);
        return profile;
    }

    public HashMap<String, String> toJson(){

        HashMap<String, String> map = new HashMap<>();

        map.put("firstName", this.getFirstName());
        map.put("lastName", this.getLastName());
        map.put("gender", this.getGender());
        map.put("userName", this.getUserName());
        map.put("birthday", this.getBirthday());
        map.put("pictureUrl", this.getUserImageUrl());
        map.put("shoulder", this.getShoulder());
        map.put("chest", this.getChest());
        map.put("basin", this.getBasin());
        map.put("waist", this.getWaist());
        map.put("foot", this.getFoot());
        map.put("height", this.getHeight());
        map.put("weight", this.getWeight());
        map.put("bodyType", this.getBodyType());
        map.put("status", this.getStatus());

        //TODO: all the arrays




        return map;
    }

//    public static List<Profile> jsonArrayToProfile(JsonArray profilesJson){
//        List<Profile> profilesList = new ArrayList<>();
//        for (int i = 0; i < profilesJson.size(); i++) {
//            profilesList.add(Profile.jsonObjectToProfile(profilesJson.get(i)));
//        }
//        return profilesList;
//    }

}
