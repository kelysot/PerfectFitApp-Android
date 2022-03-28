package com.example.perfectfitapp_android.sub_category;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.SubCategory;

import java.util.List;

public class SubCategoryViewModel extends ViewModel {

    List<SubCategory> data;

    public List<SubCategory> getData() {
        return data;
    }

    public void setData(List<SubCategory> data) {
        this.data = data;
    }
}
