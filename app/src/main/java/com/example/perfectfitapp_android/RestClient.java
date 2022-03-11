package com.example.perfectfitapp_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient{

    static RestClient instance = null;

    ResultReadyCallback callback;

    static final String BASE_URL = "http://10.0.2.2:4000";
    RetrofitInterface service;
    List<Post> posts = new ArrayList<>();

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

    public interface ResultReadyCallback {
        public void resultReady(List<Post> posts);
    }

    public List<Post> getPosts() {

        Call<JsonArray> postlist = service.getAllPosts();

        postlist.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    JsonArray postsJson = new JsonArray();
                    postsJson = response.body();
//                    Log.d("TAG444", postsJson.get(0).toString() + "**********");
                    posts = Post.jsonArrayToPost(postsJson);

                    callback.resultReady(posts);
//                    Log.d("TAG444", posts + "**********");
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("REST", t.getMessage());
            }
        });
        return posts;
    }

    public void setCallback(ResultReadyCallback callback) {
        this.callback = callback;
    }

//    public boolean createPost(final Context ctx, Post post) {
//        Call<Post> u = service.getAllPosts();
//        u.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                success = response.isSuccessful();
//                if(success) {
//                    Toast.makeText(ctx, "User Created", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ctx, "Couldn't create user", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                Log.w("REST", t.getMessage());
//                Toast.makeText(ctx, "Couldn't create user", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return success;
//    }

    public static RestClient getInstance() {
        if(instance == null) {
            instance = new RestClient();
        }
        return instance;
    }






}