package com.example.perfectfitapp_android.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Model {

    public static final Model instance = new Model();
    int count = 0;
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Model(){
        this.count = 0;
        user = new User();
    }

    /*--------------------------------- Post -------------------------------*/

    List<Post> data = new LinkedList<Post>();

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
}
