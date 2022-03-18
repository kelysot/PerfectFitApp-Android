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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.post.PostPageFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProfileFragment extends Fragment {

    ImageView userPic;
    TextView userNameTv;
    ImageButton editProfileBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userPic = view.findViewById(R.id.profile_profile_img);
        userNameTv = view.findViewById(R.id.profile_user_name);
        editProfileBtn = view.findViewById(R.id.profile_edit_profile_btn);
        editProfileBtn.setOnClickListener(v-> editProfile(view));

        Profile profile = Model.instance.getProfile();
        userNameTv.setText(profile.getUserName());
//        if (profile.getUserImageUrl() != null) {
//            Picasso.get().load(profile.getUserImageUrl()).into(userPic);
//        }

        return view;
    }

    private void editProfile(View view) {

        Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());

    }


}