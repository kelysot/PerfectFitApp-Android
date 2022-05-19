package com.example.perfectfitapp_android.post.likes;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Profile;

import java.util.List;

public class LikesViewModel extends ViewModel {

    List<Profile> likesProfiles;

    public List<Profile> getData() {
        return likesProfiles;
    }

    public void setData(List<Profile> likesProfiles) {
        this.likesProfiles = likesProfiles;
    }
}
