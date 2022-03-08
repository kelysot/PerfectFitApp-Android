package com.example.perfectfitapp_android.model;

import java.util.LinkedList;
import java.util.List;

public class Model {

    public static final Model instance = new Model();
    int count = 0;

    public Model(){
        this.count = 0;
    }

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
    public Post getStudent(int pos){ return data.get(pos); }
}
