package com.example.perfectfitapp_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

    private static RestClient instance = null;

    private ResultReadyCallback callback;

    private static final String BASE_URL = "http://10.0.2.2:4000";
    private RetrofitInterface service;
    List<Post> posts = new ArrayList<>();
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
                    JsonArray postsJson = new JsonArray();
                    postsJson = response.body();
                    Log.d("TAG444", postsJson.get(0).toString() + "**********");

                    for (int i = 0; i < postsJson.size(); i++) {
                        String id = postsJson.get(i).getAsJsonObject().get("_id").toString();
                        String profileId = postsJson.get(i).getAsJsonObject().get("profileId").toString();
                        String productName = postsJson.get(i).getAsJsonObject().get("productName").toString();
                        String sku = postsJson.get(i).getAsJsonObject().get("sku").toString();
                        String size = postsJson.get(i).getAsJsonObject().get("size").toString();
                        String company = postsJson.get(i).getAsJsonObject().get("company").toString();
                        String price = postsJson.get(i).getAsJsonObject().get("price").toString();
                        String color = postsJson.get(i).getAsJsonObject().get("color").toString();
                        String categoryId = postsJson.get(i).getAsJsonObject().get("categoryId").toString();
                        String subCategoryId = postsJson.get(i).getAsJsonObject().get("subCategoryId").toString();
                        String description = postsJson.get(i).getAsJsonObject().get("description").toString();
                        String date = postsJson.get(i).getAsJsonObject().get("date").toString();
                        String link = postsJson.get(i).getAsJsonObject().get("link").toString();
                        String sizeAdjustment = postsJson.get(i).getAsJsonObject().get("sizeAdjustment").toString();
                        String rating = postsJson.get(i).getAsJsonObject().get("rating").toString();

                        JsonElement picturesJson = postsJson.get(i).getAsJsonObject().get("picturesUrl");
                        ArrayList<String> picturesUrl = new ArrayList<>();
                        if(picturesJson != null){
                            for (JsonElement pic : picturesJson.getAsJsonArray()) {
                                picturesUrl.add(pic.toString());
                            }
                        }

                        JsonElement likesJson = postsJson.get(i).getAsJsonObject().get("likes");
                        ArrayList<String> likes = new ArrayList<>();
                        if(!likesJson.toString().equals("null")){
                            for (JsonElement like : likesJson.getAsJsonArray()) {
                                likes.add(like.toString());
                            }
                        }

                        JsonElement commentsJson = postsJson.get(i).getAsJsonObject().get("comments");
                        ArrayList<String> comments = new ArrayList<>();
                        if(!commentsJson.toString().equals("null")){
                            for (JsonElement comment : commentsJson.getAsJsonArray()) {
                                comments.add(comment.toString());
                            }
                        }


                        Post post = new Post(id, profileId, productName, sku, size, company,
                                color, categoryId, subCategoryId, description,
                                date, link, sizeAdjustment, rating, picturesUrl,
                                price, likes, comments);
//                        Log.d("TAG444", post + "******+++++++****");

                        posts.add(post);
                    }
                    Log.d("TAG444", posts + "**********");

                   // callback.resultReady(posts);
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

    public interface ResultReadyCallback {
        public void resultReady(List<Post> posts);
    }

}