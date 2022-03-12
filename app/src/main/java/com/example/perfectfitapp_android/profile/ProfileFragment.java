package com.example.perfectfitapp_android.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RestClient;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;

import java.util.List;


public class ProfileFragment extends Fragment {

    ImageView userPic;
    TextView userNameTv;
    ProfileRestClient restClient = new ProfileRestClient();
    String name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        userPic = view.findViewById(R.id.profile_profile_img);
        userNameTv = view.findViewById(R.id.profile_user_name);

        restClient.setCallback(new ProfileRestClient.ResultReadyCallbackProfile() {
            @Override
            public void resultReady(Profile profile) {
//                Log.d("TAG123", profile + "11111111111111111111");
//                Log.d("TAG123", profile.getFirstName() + "11111111111111111111");
                // name = profile.getUserName();
                Log.d("TAG123", profile.getUserName() + "11111111111111111111");
                //  userNameTv.setText(profile.getUserName());
            }
        });
        restClient.getProfile("622b6fa21977037a1057d3ca");

        return view;
    }


}