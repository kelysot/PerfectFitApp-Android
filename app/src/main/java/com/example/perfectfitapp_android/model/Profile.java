package com.example.perfectfitapp_android.model;

import com.example.perfectfitapp_android.profile.ProfileFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    String userId, firstName, lastName,gender, userName, birthday, userImageUrl;
    String shoulder, chest, basin, waist, foot, height, weight, bodyType, status;
    ArrayList<String> similarProfileId;
    ArrayList<String> followers, trackers;
    ArrayList<String> notifications;
    ArrayList<String> wishlist;
    ArrayList<String> myPostsListId;

    public Profile(String userId, String firstName, String lastName, String gender, String userName,
                   String birthday, String userImageUrl, String shoulder, String chest, String basin,
                   String waist, String foot, String height, String weight, String bodyType) {

        this.userId = userId;
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
    public Profile(String userId, String firstName, String lastName, String gender, String userName,
                   String birthday, String userImageUrl, String shoulder, String chest, String basin,
                   String waist, String foot, String height, String weight, String bodyType, String status, ArrayList<String> similarProfileId,
                   ArrayList<String> followers, ArrayList<String> trackers, ArrayList<String> notifications,
                   ArrayList<String> wishlist, ArrayList<String> myPostsListId) {

        this.userId = userId;
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

    public static Profile jsonObjectToProfile(JsonElement profilesJson){
        String userId = profilesJson.getAsJsonObject().get("_id").toString();
        String firstName = profilesJson.getAsJsonObject().get("firstName").toString();
        String lastName = profilesJson.getAsJsonObject().get("lastName").toString();
        String gender = profilesJson.getAsJsonObject().get("gender").toString();
        String userName = profilesJson.getAsJsonObject().get("userName").toString();
        String birthday = profilesJson.getAsJsonObject().get("birthday").toString();
        String userImageUrl = profilesJson.getAsJsonObject().get("userImageUrl").toString();
        String shoulder = profilesJson.getAsJsonObject().get("shoulder").toString();
        String chest = profilesJson.getAsJsonObject().get("chest").toString();
        String basin = profilesJson.getAsJsonObject().get("basin").toString();
        String waist = profilesJson.getAsJsonObject().get("waist").toString();
        String foot = profilesJson.getAsJsonObject().get("foot").toString();
        String height = profilesJson.getAsJsonObject().get("height").toString();
        String weight = profilesJson.getAsJsonObject().get("weight").toString();
        String bodyType = profilesJson.getAsJsonObject().get("bodyType").toString();
        String status = profilesJson.getAsJsonObject().get("status").toString();

        JsonElement similarProfileIdJson = profilesJson.getAsJsonObject().get("similarProfileId");
        ArrayList<String> similarProfileId = new ArrayList<>();
        if(!similarProfileIdJson.toString().equals("null") || !similarProfileIdJson.isJsonNull()){
            for (JsonElement similarProfile : similarProfileIdJson.getAsJsonArray()) {
                similarProfileId.add(similarProfile.toString());
            }
        }

        JsonElement followersJson = profilesJson.getAsJsonObject().get("followers");
        ArrayList<String> followers = new ArrayList<>();
        if(!followersJson.toString().equals("null") || !followersJson.isJsonNull()){
            for (JsonElement follower : followersJson.getAsJsonArray()) {
                followers.add(follower.toString());
            }
        }

        JsonElement trackersJson = profilesJson.getAsJsonObject().get("trackers");
        ArrayList<String> trackers = new ArrayList<>();
        if(!trackersJson.toString().equals("null") || !trackersJson.isJsonNull()){
            for (JsonElement tracker : trackersJson.getAsJsonArray()) {
                trackers.add(tracker.toString());
            }
        }

        JsonElement notificationsJson = profilesJson.getAsJsonObject().get("notifications");
        ArrayList<String> notifications = new ArrayList<>();
        if(!notificationsJson.toString().equals("null") || !notificationsJson.isJsonNull()){
            for (JsonElement notification : notificationsJson.getAsJsonArray()) {
                notifications.add(notification.toString());
            }
        }

        JsonElement wishlistJson = profilesJson.getAsJsonObject().get("wishlist");
        ArrayList<String> wishlist = new ArrayList<>();
        if(!wishlistJson.toString().equals("null") || !wishlistJson.isJsonNull()){
            for (JsonElement wishlist1 : wishlistJson.getAsJsonArray()) {
                wishlist.add(wishlist1.toString());
            }
        }

        JsonElement myPostsListIdJson = profilesJson.getAsJsonObject().get("myPostsListId");
        ArrayList<String> myPostsListId = new ArrayList<>();
        if(!myPostsListIdJson.toString().equals("null") || !myPostsListIdJson.isJsonNull()){
            for (JsonElement myPost : myPostsListIdJson.getAsJsonArray()) {
                myPostsListId.add(myPost.toString());
            }
        }

        Profile profile = new Profile(userId, firstName, lastName, gender, userName,
                birthday, userImageUrl, shoulder, chest, basin, waist, foot, height,
                weight, bodyType, status, similarProfileId, followers, trackers, notifications,
                wishlist, myPostsListId);
        return profile;
    }

    public static List<Profile> jsonArrayToProfile(JsonArray profilesJson){
        List<Profile> profilesList = new ArrayList<>();
        for (int i = 0; i < profilesJson.size(); i++) {
            profilesList.add(Profile.jsonObjectToProfile(profilesJson.get(i)));
        }
        return profilesList;
    }

}
