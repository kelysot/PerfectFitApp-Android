package com.example.perfectfitapp_android.profile;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;

public class ModelProfile {

    public static final ModelProfile instance = new ModelProfile();

    Profile editProfile;

    public ModelProfile(Profile editProfile) {
        this.editProfile = editProfile;
    }

    public ModelProfile() {
        this.editProfile = new Profile();
    }

    public Profile getEditProfile() {
        return editProfile;
    }

    public void setEditProfile(Profile editProfile) {
        this.editProfile = editProfile;
    }
}
