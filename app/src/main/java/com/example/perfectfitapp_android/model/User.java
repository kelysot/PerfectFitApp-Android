package com.example.perfectfitapp_android.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    String email, password, isConnected;
    ArrayList<String> profilesArray;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<String> getProfilesArray() {
        return profilesArray;
    }

    public void setProfilesArray(ArrayList<String> profilesArray) {
        this.profilesArray = profilesArray;
    }

    public String getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }

    public User(){
        this.email = "";
        this.password = "";
        this.isConnected = "";
        profilesArray = new ArrayList<>();
    }

    public User(String email, String password, String isConnected){
        this.email = email;
        this.password = password;
        this.isConnected = isConnected;
        profilesArray = new ArrayList<>();
    }

    public HashMap<String, String> toJson(){

        HashMap<String, String> map = new HashMap<>();
        map.put("email", this.getEmail());
        map.put("password", this.getPassword());
        map.put("isConnected", this.getIsConnected());

        return map;
        //TODO: list of profiles
    }

    public User fromJson(JsonObject json){
        User user = new User();

        user.setEmail(json.get("email").getAsString());
        user.setIsConnected(json.get("isConnected").getAsString());
//        user.setPassword(json.get("password").getAsString()); //TODO: need the password here?
        ArrayList<String> arr = new ArrayList<>();
        for (JsonElement j: json.get("profilesId").getAsJsonArray()) {
            arr.add(j.getAsString());
        }
        user.setProfilesArray(arr);

        // TODO: get the Tokens
        return user;
    }

}
