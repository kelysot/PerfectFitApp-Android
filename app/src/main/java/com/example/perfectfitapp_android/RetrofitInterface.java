package com.example.perfectfitapp_android;

import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/auth/login")
    Call<User> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/register")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @GET("/post")
    Call<JsonArray> getAllPosts();

}
