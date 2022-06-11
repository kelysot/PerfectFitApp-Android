package com.example.perfectfitapp_android.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.List;

public class HomePageViewModel extends ViewModel {

    LiveData<List<Post>> data;

    public HomePageViewModel() {
        data = Model.instance.getAll();
    }

    public LiveData<List<Post>> getData() {
        return data;
    }

    public void setData(LiveData<List<Post>> data) {
        this.data = data;
    }
}
