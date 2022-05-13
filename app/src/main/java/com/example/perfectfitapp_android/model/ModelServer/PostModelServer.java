package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostModelServer {

    Server server = new Server();

    public void getAllPosts(Model.GetAllPostsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.executeGetAllPosts(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                //TODO: check the code == 200 or 400
                if (response.isSuccessful()) {
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                } else {
                    Log.d("TAG", "failed in getAllPosts in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllPosts in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }

    public void addNewPost(Post post, Model.AddNewPostListener listener) {
        HashMap<String, Object> postMap = post.toJson();
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.addNewPost(token, postMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Post newPost = new Post();
                    newPost = Post.jsonElementToPost(response.body().get("post"));
                    listener.onComplete(newPost);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "problem in createProfile in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }


    public void deletePost(String postId, Model.deletePostFromServerListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = server.service.deletePost(token, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in deletePost 1");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in deletePost 2");
                listener.onComplete(false);
            }
        });
    }

    public void editPost(Post post, Model.editPostListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        HashMap<String, Object> postMap = post.toJson();

        Call<Void> call = server.service.editPost(token, postMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in editPost 1");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in editPost 2");
                listener.onComplete(false);
            }
        });
    }


    public void getProfilePosts(String userName, Model.getProfilePostsListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getProfilePosts(token, userName);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    List<Post> posts = Post.jsonArrayToPost(response.body());
                    /**** we get the posts checked of isDeleted from the server ****/
//                    List<Post> finalList = new LinkedList<>();
//                    for(int i=0; i<posts.size(); i++){
//                        if(posts.get(i).getIsDeleted().equals("false")){
//                            finalList.add(posts.get(i));
//                        }
//                    }
                    listener.onComplete(posts);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });
    }


    public void getPostById(String postId, Model.getPostByIdListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = server.service.getPostById(token, postId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    Post post = Post.jsonElementToPost(js);
                    listener.onComplete(post);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });

    }

    public void getPostsBySubCategoryId(String subCategoryId, Model.GetPostsBySubCategoryIdListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getPostsBySubCategoryId(token, subCategoryId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });

    }

    public void getWishList(Model.getWishListListener listener){

        //TODO: check if the post belongs to the profile
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getWishList(token, Model.instance.getProfile().getUserName());

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    //TODO

                    JsonArray wishListArray = response.body();
                    List<Post> postsWishList = new ArrayList<>();
                    postsWishList = Post.jsonArrayToPost(response.body());
                    listener.onComplete(postsWishList);

                }
                else{
                    Log.d("TAG", "failed in getWishList in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getWishList in ModelServer 2");
                listener.onComplete(null);
            }
        });

    }

    public void getDatesFromServer(String date, Model.getDatesListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = server.service.getDates(token, date);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    Log.d("TAG111", response.body().toString());
                    System.out.println("222222222222222222222222222222222222222222");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void timeSince(String date, Model.TimeSinceListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonElement> call = server.service.timeSince(token, date);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    String timeAgo = response.body().getAsJsonObject().get("timeAgo").getAsString() + " ago";
                    listener.onComplete(timeAgo);
                }
                else {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in timeSince");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Log.d("TAG", "failed in timeSince");
                listener.onComplete(null);
            }
        });
    }


    public void getSuitablePosts(String profileId, Model.getSuitablePostsListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = server.service.getSuitablePosts(token, profileId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    JsonArray js = response.body();
                    List<Post> list = Post.jsonArrayToPost(js);
                    System.out.println("the list is: " + list);
                    listener.onComplete(list);
                }
                else{
                    listener.onComplete(null);
                    Log.d("TAG", "failed in getSuitablePosts 1");
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                listener.onComplete(null);
                Log.d("TAG", "failed in getSuitablePosts 2");
            }
        });
    }


    public void getSearchPosts(HashMap<String, List<String >> map, Model.getSearchPostsListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = server.service.getSearchPosts(token, map);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    System.out.println("we did it " + response.body());
                    List<Post> list = Post.jsonArrayToPost(response.body());
                    listener.onComplete(list);
                }
                else{
                    listener.onComplete(null);
                    Log.d("TAG", "failed in getSearchPosts 1");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                listener.onComplete(null);
                Log.d("TAG", "failed in getSearchPosts 2");
            }
        });
//        Call<JsonArray> call = server.service.getSearchPosts(token);

//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                if(response.code() == 200){
//
//                }
//                else{
//                    listener.onComplete(null);
//                    Log.d("TAG", "failed in getSearchPosts 1");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                listener.onComplete(null);
//                Log.d("TAG", "failed in getSearchPosts 2");
//            }
//        });
    }




}
