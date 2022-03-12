package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class UserProfilesFragment extends Fragment {

    ImageButton addProfile;
    Button homepageBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);

        addProfile = view.findViewById(R.id.user_profiles_addprofile_btn);
        addProfile.setOnClickListener(v-> addProfile(view));

        homepageBtn = view.findViewById(R.id.move_to_home_page);
        homepageBtn.setOnClickListener(v-> moveToHomePage(view));

        return view;
    }

    private void moveToHomePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
    }


    private void addProfile(View view) {

        System.out.println("addProfile was clicked");

        Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_registerFragment2);

    }
}