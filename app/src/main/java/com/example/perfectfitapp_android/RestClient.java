package com.example.perfectfitapp_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.builders.MapBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient{

    private static RestClient instance = null;

    private ResultReadyCallback callback;

    private static final String BASE_URL = "http://10.0.2.2:4000";
    private RetrofitInterface service;
    List<Post> posts = null;
    boolean success = false;
    Map<String, Object> map;



    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

//    public void try1(){
//        Log.d("TAG444", "00000000000");
//
//        Call<Map<String, Object>> str = service.try1();
//        Log.d("TAG444", "11111111111111");
//
//        str.enqueue(new Callback<Map<String, Object>>() {
//            @Override
//            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                Log.d("TAG444", "222222222222222");
//                if (response.isSuccessful()) {
//
//                    Log.d("TAG444", response.body().toString());
//                    response.body().
//                    str = response.body();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
//                Log.d("TAG444", "3333333333333333");
//                Log.d("TAG444", t.getMessage());
//            }
//        });
//    }

    public List<Post> getPosts() {

        Call<JsonArray> postlist = service.getAllPosts();

        postlist.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    JsonArray a = new JsonArray();
                    a = (response.body());
                    // Log.d("TAG444", a.toString());
                    Log.d("TAG444", a.get(0).toString() + "**********");
                    //  callback.resultReady(posts);
                    a.get(0);
//                    HashMap<String,Object> result = new HashMap<>();
//                    Post post = Post.fromJson( a.get(0));
//                    Log.d("TAG444", post.toString() + "$$$$$$$$$$");
//                    Map<String, Object> map = a.stream().map(Object::toString).collect(Collectors.toMap(s -> s, s -> "value"));


                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("REST", t.getMessage());
            }
        });


        return null;
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

    public interface ResultReadyCallback {
        public void resultReady(List<Post> posts);
    }

}