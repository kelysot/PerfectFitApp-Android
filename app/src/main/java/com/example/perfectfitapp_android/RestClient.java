package com.example.perfectfitapp_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

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
    List<String> posts = null;
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

//        Call<JSONArray> postlist = service.getAllPosts();
//
//        postlist.enqueue(new Callback<JSONArray>() {
//            @Override
//            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
//                if (response.isSuccessful()) {
//                    JSONArray arr = null;
//                    try {
//                        arr[0] = response.body().getJSONObject(0);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("TAG444", arr.toString());
//                    Log.d("TAG444","222222222");
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JSONArray> call, Throwable t) {
//                Log.d("TAG444", "55555555555555");
//            }
//        });

        /*********************************************************************************/
        Call<ArrayList<String>> postlist = service.getAllPosts();

        postlist.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", response.body().toString());
                    Log.d("TAG444",  posts.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("TAG444", "55555555555555");

            }
        });






        /*********************************************************************************/
//        Call<Map<String, Object>> postlist = service.getAllPosts();
//
//        postlist.enqueue(new Callback<Map<String, Object>>() {
//            @Override
//            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                if (response.isSuccessful()) {
//
//                    map = response.body();
//                    Log.d("TAG444", "444444444444");
//                    Log.d("TAG444",  map.toString());
////                    HashMap<String, String>(map.get("posts"));
//
//
////                    Object o = map.get("");
////                    response.body();
//
////                    posts = Post.getPostsFromJson();
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
//                Log.d("TAG444", "55555555555555");
//                Log.e("REST", t.getMessage());
//            }
//        });

                //////////////////////////////////////////////////////////////////////////////////////

//        Log.d("TAG444", "11111111111111");
//        Call<List<Post>> postlist = service.getAllPosts();
//        Log.d("TAG444", "22222222222222");
//        postlist.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                Log.d("TAG444", "3333333333333333");
//                if (response.isSuccessful()) {
//                    Log.d("TAG444", response.body().toString());
////                    posts = response.body();
////                    Log.d("TAG333", posts.get(0).getPostId());
////                    callback.resultReady(posts);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Log.d("TAG444", "55555555555555");
//                Log.d("TAG444", call.toString());
//                Log.e("TAG444", t.getMessage());
//                Log.e("REST", t.getMessage());
//            }
//        });
//        return posts;

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