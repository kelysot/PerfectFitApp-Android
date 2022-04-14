package com.example.perfectfitapp_android.comment;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Comment;

import java.util.List;

public class CommentViewModel extends ViewModel {

    List<Comment> data;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
