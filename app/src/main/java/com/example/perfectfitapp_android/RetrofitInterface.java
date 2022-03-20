package com.example.perfectfitapp_android;

import android.util.Log;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/

    @POST("/auth/login")
    Call<User> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/register")
    Call<Void> executeRegister (@Body HashMap<String,String> map);

    @POST("/auth/logout")
    Call<Void> executeLogout(@Header("authorization") String token);

    @GET("/auth/getUser/{email}")
    Call<JsonObject> executeGetUser (@Path("email") String email);

    @GET("/auth/checkIfEmailExist/{email}")
    Call<Void> checkIfEmailExist(@Path("email") String email);

    @GET("/profile/:{id}")
    Call<JsonObject> getUser(@Path("id") String id);
    // http://10.0.2.2:4000/profile/1
    // Call<User> service.getUser(1);

    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/

    @GET("/profile/getProfile/{email}/{userName}")
    Call<JsonObject> executeGetProfile (@Header("authorization") String token,@Path("email") String email, @Path("userName") String userName);

    @POST("/profile/")
    Call<Void> executeCreateProfile (@Header("authorization") String token, @Body HashMap<String, Object>  map);

    @GET("/profile/checkIfUserNameExist/{userName}")
    Call<Void> checkIfUserNameExist(@Header("authorization") String token, @Path("userName") String userName);

    @PATCH("/profile/")
    Call<Void> editProfile(@Header("authorization") String token, @Body HashMap<String, Object> map);

    @DELETE("/profile/{userName}")
    Call<Void> deleteProfile(@Header("authorization") String token, @Path("userName") String userName);

    /*--------------------------------- Post -------------------------------*/

    /******************************************************************************************/

    @GET("/post")
    Call<JsonArray> executeGetAllPosts(@Header("authorization") String token);

    @POST("/post")
    Call<JsonObject> addNewPost(@Header("authorization") String token, @Body HashMap<String, Object> map);


    /*--------------------------------- Category -------------------------------*/

    @GET("/category")
    Call<JsonArray> getAllCategories(@Header("authorization") String token);

    /*--------------------------------- SubCategory -------------------------------*/

    @GET("/subcategory")
    Call<JsonArray> getAllSubCategories(@Header("authorization") String token);
}
