package com.example.perfectfitapp_android.sub_category;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.SubCategory;

import java.util.List;

public class SubCategoryDetailsPostsViewModel extends ViewModel {

    List<Post> data;
    SubCategory subCategory;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
