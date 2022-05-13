package com.example.perfectfitapp_android.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.List;

public class SearchViewModel extends ViewModel {

    List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
