package com.example.perfectfitapp_android.model;

import java.util.ArrayList;

public class User {

    String email, password;
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

    public User(){
        this.email = "";
        this.password = "";
        profilesArray = new ArrayList<>();
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
        profilesArray = new ArrayList<>();
    }

}
