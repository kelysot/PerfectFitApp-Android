package com.example.perfectfitapp_android.category;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    List<Category> data;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

}
