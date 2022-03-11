package com.example.perfectfitapp_android.profile;

import android.util.Log;

import com.example.perfectfitapp_android.RestClient;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRestClient {

    static ProfileRestClient instance = null;
    ProfileRestClient.ResultReadyCallbackProfile callback;
    static final String BASE_URL = "http://10.0.2.2:4000";
    RetrofitInterface service;
    Profile profile;


    public ProfileRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

    public interface ResultReadyCallbackProfile {
        public void resultReady(Profile profile);
    }

    public Profile getProfile(String id) {

        Log.d("TAG444",  "111112222");

        Call<JsonObject> profileJson = service.getUser(id);

        profileJson.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG444",  response.body() +"222222");

                if(response.isSuccessful()){
                    Log.d("TAG444",  "3333");

                    JsonObject profileJson = new JsonObject();
                    profileJson = response.body();
                    Log.d("TAG444", profileJson.toString() + "**********");
                    profile = Profile.jsonObjectToProfile(profileJson);

                    callback.resultReady(profile);
                    Log.d("TAG444", profile + "**********");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("REST", t.getMessage());

            }
        });
        return null;
    }

    public void setCallback(ProfileRestClient.ResultReadyCallbackProfile callback) {
        this.callback = callback;
    }

    public static ProfileRestClient getInstance() {
        if(instance == null) {
            instance = new ProfileRestClient();
        }
        return instance;
    }
}
