package com.example.perfectfitapp_android.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    String email;
    String password, isConnected;
    ArrayList<String> profilesId;

    public User(){
        this.email = "";
        this.password = "";
        this.isConnected = "";
        profilesId = new ArrayList<>();
    }

    public User(@NonNull String email, String password, String isConnected){
        this.email = email;
        this.password = password;
        this.isConnected = isConnected;
        profilesId = new ArrayList<>();
    }

    public User(@NonNull String email, String isConnected, ArrayList<String> profilesId){
        this.email = email;
        this.isConnected = isConnected;
        this.profilesId = profilesId;
    }

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
        return profilesId;
    }

    public void setProfilesArray(ArrayList<String> profilesId) {
        this.profilesId = profilesId;
    }

    public String getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
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

    public static User jsonObjectToUser(JsonObject userJson){
        String email = userJson.get("email").getAsString();
        String isConnected = userJson.get("isConnected").getAsString();

        JsonElement profilesIdJson = userJson.get("profilesId");
        ArrayList<String> profilesId = new ArrayList<>();
        if(!profilesIdJson.toString().equals("null") || !profilesIdJson.isJsonNull()){
            for (JsonElement profilesId1 : profilesIdJson.getAsJsonArray()) {
                profilesId.add(profilesId1.getAsString());
            }
        }

        User user = new User(email, isConnected, profilesId);

        return user;
    }

}
