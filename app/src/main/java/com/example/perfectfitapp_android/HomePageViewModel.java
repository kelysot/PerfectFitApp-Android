package com.example.perfectfitapp_android;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Post;

import java.util.List;

public class HomePageViewModel extends ViewModel {

    List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
