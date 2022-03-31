package com.example.perfectfitapp_android.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;

import java.util.ArrayList;

public class EditProfileStep3Fragment extends Fragment {

    EditText shoulderEt, chestEt, basinEt, waistEt, heightEt, weightEt, footEt;
    Button saveChangesBtn;
    ImageView explenationImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_step3, container, false);

        shoulderEt = view.findViewById(R.id.edit_profile_step3_shoulder_et);
        chestEt = view.findViewById(R.id.edit_profile_step3_chest_et);
        basinEt = view.findViewById(R.id.edit_profile_step3_basin_et);
        waistEt = view.findViewById(R.id.edit_profile_step3_waist_et);
        heightEt = view.findViewById(R.id.edit_profile_step3_height_et);
        weightEt = view.findViewById(R.id.edit_profile_step3_weight_et);
        footEt = view.findViewById(R.id.edit_profile_step3_foot_et);
        explenationImg = view.findViewById(R.id.edit_profile_step3_explanation_image);

        if(ModelProfile.instance.getEditProfile().getGender().equals("Female")){
            explenationImg.setImageResource(R.drawable.body_measurement_for_her);
        }
        else{
            explenationImg.setImageResource(R.drawable.body_measurement_for_him);
        }

        saveChangesBtn = view.findViewById(R.id.edit_profile_step3_savechanges_btn);
        saveChangesBtn.setOnClickListener(v-> saveChanges(view));

        shoulderEt.setText(ModelProfile.instance.getEditProfile().getShoulder());
        chestEt.setText(ModelProfile.instance.getEditProfile().getChest());
        basinEt.setText(ModelProfile.instance.getEditProfile().getBasin());
        waistEt.setText(ModelProfile.instance.getEditProfile().getWaist());
        heightEt.setText(ModelProfile.instance.getEditProfile().getHeight());
        weightEt.setText(ModelProfile.instance.getEditProfile().getWeight());
        footEt.setText(ModelProfile.instance.getEditProfile().getFoot());

        return view;
    }

    private void saveChanges(View view) {

        saveChangesBtn.setEnabled(false);

        ModelProfile.instance.getEditProfile().setShoulder(shoulderEt.getText().toString());
        ModelProfile.instance.getEditProfile().setChest(chestEt.getText().toString());
        ModelProfile.instance.getEditProfile().setBasin(basinEt.getText().toString());
        ModelProfile.instance.getEditProfile().setWaist(waistEt.getText().toString());
        ModelProfile.instance.getEditProfile().setWeight(weightEt.getText().toString());
        ModelProfile.instance.getEditProfile().setFoot(footEt.getText().toString());
        ModelProfile.instance.getEditProfile().setShoulder(shoulderEt.getText().toString());
        ModelProfile.instance.getEditProfile().setShoulder(shoulderEt.getText().toString());

        //TODO: change the function when we have the refresh

        Model.instance.editProfile(ModelProfile.instance.getPreviousName(),Model.instance.getProfile(), isSuccess -> {
            if(isSuccess){
                //change the profile list of the user in the Model
                Model.instance.setProfile(ModelProfile.instance.getEditProfile());
                ArrayList<String> arr = Model.instance.getUser().getProfilesArray();
                int index = arr.indexOf(ModelProfile.instance.getPreviousName());
                String userName = ModelProfile.instance.getEditProfile().getUserName();
                Model.instance.getUser().getProfilesArray().set(index, userName);

                Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);
//                Navigation.findNavController(view).navigate(R.id.action_global_profileFragment);
            }
        });
    }
}