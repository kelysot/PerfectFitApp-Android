package com.example.perfectfitapp_android.model;

import java.util.ArrayList;

public class Profile {

    String userId, firstName, lastName,gender, userName, birthday, userImageUrl;
    String shoulder, chest, basin, waist, foot, height, weight, bodyType, status;
    ArrayList<String> similarProfileId;
    ArrayList<Profile> followers, trackers;
    ArrayList<Notification> notifications;
    ArrayList<Post> wishlist;
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

    public ArrayList<Profile> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Profile> followers) {
        this.followers = followers;
    }

    /*------------------------------------------------------*/

    public ArrayList<Profile> getTrackers() {
        return trackers;
    }

    public void setTrackers(ArrayList<Profile> trackers) {
        this.trackers = trackers;
    }

    /*------------------------------------------------------*/

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    /*------------------------------------------------------*/

    public ArrayList<Post> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Post> wishlist) {
        this.wishlist = wishlist;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getMyPostsListId() {
        return myPostsListId;
    }

    public void setMyPostsListId(ArrayList<String> myPostsListId) {
        this.myPostsListId = myPostsListId;
    }
}
