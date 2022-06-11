package com.example.perfectfitapp_android.create_profile;

import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;

public class CreateProfileModel {

    User user;
    Profile profile;

    public static final CreateProfileModel instance = new CreateProfileModel();

    public CreateProfileModel(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public CreateProfileModel() {
        user = new User();
        profile = new Profile();
    }


}
