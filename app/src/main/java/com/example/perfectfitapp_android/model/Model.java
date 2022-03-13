package com.example.perfectfitapp_android.model;

import android.view.Display;

import com.example.perfectfitapp_android.ModelServer;
import com.example.perfectfitapp_android.RestClient;
import com.example.perfectfitapp_android.RetrofitInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {

    public static final Model instance = new Model();
    int count = 0;
    User user;
    Profile profile;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http://10.0.2.2:4000";
    List<Post> data = new LinkedList<Post>();
    ModelServer modelServer = new ModelServer();


    public Model(){
        this.count = 0;
        user = new User();
        profile = new Profile();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public RetrofitInterface getRetrofitInterface() {
        return retrofitInterface;
    }

    public void setRetrofitInterface(RetrofitInterface retrofitInterface) {
        this.retrofitInterface = retrofitInterface;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    /*--------------------------------- Post -------------------------------*/


    public List<Post> getAllPosts(){
        return data;
    }

    public void addPost(Post post){
        data.add(post);
    }

    public Post getPostById(String postId) {
        for (Post s:data
        ) {
            if (s.getPostId().equals(postId)){
                return s;
            }
        }
        return null;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void deletePost(int pos){ data.remove(pos); }
    public void deletePostByPost(Post post){

        int index = data.indexOf(post);
        data.remove(index);
    }
    public Post getPost(int pos){ return data.get(pos); }

    /*--------------------------------- Profile -------------------------------*/

    List<Profile> profiles = new LinkedList<>();

    public Profile getProfileById(String profileId) {
        for (Profile s:profiles) {
            if (s.getProfileId().equals(profileId)){
                return s;
            }
        }
        return null;
    }


    /*--------------------------------------------------------*/

    public interface getAllPostsListener {
        void onComplete(List<Post> postList);
    }

    public void getAllPostsFromServer(getAllPostsListener listener) {
        modelServer.getAllPosts(listener);
    }

    /*--------------------------------------------------------*/

    public interface getProfileListener{
        void onComplete(Profile profile);
    }

    public void getProfile(String email, String userName,getProfileListener listener) {
        modelServer.getProfile(email,userName, listener);
    }


    /*--------------------------------------------------------*/




}
