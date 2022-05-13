package com.example.perfectfitapp_android.profile.followers;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Profile;

import java.util.List;

public class FollowersViewModel extends ViewModel {

    List<Profile> followersProfiles;

    public List<Profile> getFollowersProfiles() {
        return followersProfiles;
    }

    public void setFollowersProfiles(List<Profile> followersProfiles) {
        this.followersProfiles = followersProfiles;
    }
}
