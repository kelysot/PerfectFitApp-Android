package com.example.perfectfitapp_android.model;

public class User {

    String email, password;

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

    public User(){
        this.email = "";
        this.password = "";
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

}
