package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;

import java.util.ArrayList;


public class AddNewPostStep1Fragment extends Fragment {

    ImageButton cameraBtn, galleryBtn;
    ImageView image;
    Button okBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_post_step1, container, false);

        cameraBtn = view.findViewById(R.id.add_new_post_step1_camera_btn);
        galleryBtn = view.findViewById(R.id.add_new_post_step1_gallery_btn);
        image = view.findViewById(R.id.add_new_post_step1_image_imv);
        okBtn = view.findViewById(R.id.add_new_post_step1_ok_btn);
        okBtn.setOnClickListener(v -> moveStep2());

        return view;
    }

    private void moveStep2() {
        ArrayList<String> picturesArr = new ArrayList<>();
        Model.instance.getNewPost().setPicturesUrl(picturesArr);
        Navigation.findNavController(okBtn)
                .navigate(AddNewPostStep1FragmentDirections.actionAddNewPostStep1FragmentToAddNewPostFragment());
    }
}