package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfilesFragment extends Fragment {

    ImageButton addProfile;
    Button homepageBtn;
    Button user1Btn, user2Btn, user3Btn, user4Btn, user5Btn;
    ArrayList<Button> buttonList;
    Model model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);

        model = Model.instance;

        addProfile = view.findViewById(R.id.user_profiles_addprofile_btn);
        addProfile.setOnClickListener(v-> addProfile(view));

        homepageBtn = view.findViewById(R.id.move_to_home_page);

        buttonList = new ArrayList<>();

        user1Btn = view.findViewById(R.id.user_profiles_profile1_btn);
        user2Btn = view.findViewById(R.id.user_profiles_profile2_btn);
        user3Btn = view.findViewById(R.id.user_profiles_profile3_btn);
        user4Btn = view.findViewById(R.id.user_profiles_profile4_btn);
        user5Btn = view.findViewById(R.id.user_profiles_profile5_btn);

        buttonList.add(user1Btn);
        buttonList.add(user2Btn);
        buttonList.add(user3Btn);
        buttonList.add(user4Btn);
        buttonList.add(user5Btn);

        for(int j=0; j<buttonList.size(); j++){
            buttonList.get(j).setVisibility(View.GONE);
        }

        for(int i=0; i < Model.instance.getUser().getProfilesArray().size(); i++){
            buttonList.get(i).setVisibility(View.VISIBLE);
            buttonList.get(i).setText(Model.instance.getUser().getProfilesArray().get(i));
            int finalI = i;
            buttonList.get(i).setOnClickListener(v-> moveToHomePageWithProfile(view, model.getUser().getProfilesArray().get(finalI)));
        }

        return view;
    }


    private void moveToHomePageWithProfile(View view, String userName) {

        // getting the profile in order to show relevant details

        Model.instance.getProfile(model.getUser().getEmail(), userName, profile -> {
            if(profile != null){
                model.setProfile(profile);
                Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
            }
            else{
                Log.d("TAG", "failed in UserProfileFragment 1");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
            }

        });

//        Call<JsonObject> call = model.getRetrofitInterface().executeGetProfile(model.getUser().getEmail(), userName);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if(response.code() == 200){
//
//                    Profile profile = new Profile();
//                    profile = profile.jsonObjectToProfile(response.body());
//                    model.setProfile(profile);
//                    Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
//                }
//                else if(response.code() == 400){
//                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                            Toast.LENGTH_LONG).show();
//                    Log.d("TAG", "problem in userProfilesFragment 1");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                        Toast.LENGTH_LONG).show();
//
//                Log.d("TAG", "problem in userProfilesFragment 2");
//
//            }
//        });
    }

    private void addProfile(View view) {
        Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_registerFragment2);
    }
}