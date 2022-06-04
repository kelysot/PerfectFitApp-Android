package com.example.perfectfitapp_android.profile.edit_profile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.profile.ModelProfile;

import java.util.ArrayList;

public class EditProfileStep3Fragment extends Fragment {

    EditText shoulderEt, chestEt, basinEt, waistEt, heightEt, weightEt, footEt;
    Button saveChangesBtn;
    ImageView explenationImg;
    ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.edit_profile_step3_progress_bar);
        progressBar.setVisibility(View.GONE);

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

        progressBar.setVisibility(View.VISIBLE);
        saveChangesBtn.setEnabled(false);
        boolean flag = true;

        if(shoulderEt.getText().toString().isEmpty()){
            shoulderEt.setError("Please enter your shoulder size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(chestEt.getText().toString().isEmpty()){
            chestEt.setError("Please enter your chest size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(basinEt.getText().toString().isEmpty()){
            basinEt.setError("Please enter your hips size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(waistEt.getText().toString().isEmpty()){
            waistEt.setError("Please enter your waist size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(heightEt.getText().toString().isEmpty()){
            heightEt.setError("Please enter your height size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(weightEt.getText().toString().isEmpty()){
            weightEt.setError("Please enter your weight size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }
        if(footEt.getText().toString().isEmpty()){
            footEt.setError("Please enter your shoes size.");
            saveChangesBtn.setEnabled(true);
            flag = false;
        }

        if(flag) {

            ModelProfile.instance.getEditProfile().setShoulder(shoulderEt.getText().toString());
            ModelProfile.instance.getEditProfile().setChest(chestEt.getText().toString());
            ModelProfile.instance.getEditProfile().setBasin(basinEt.getText().toString());
            ModelProfile.instance.getEditProfile().setWaist(waistEt.getText().toString());
            ModelProfile.instance.getEditProfile().setHeight(heightEt.getText().toString());
            ModelProfile.instance.getEditProfile().setWeight(weightEt.getText().toString());
            ModelProfile.instance.getEditProfile().setFoot(footEt.getText().toString());
            ModelProfile.instance.getEditProfile().setSimilarProfileId(new ArrayList<>());

            //TODO: change the function when we have the refresh

            Model.instance.editProfile(ModelProfile.instance.getPreviousName(), Model.instance.getProfile(), isSuccess -> {
                if (isSuccess) {
                    //change the profile list of the user in the Model
                    Model.instance.setProfile(ModelProfile.instance.getEditProfile());
                    ArrayList<String> arr = Model.instance.getUser().getProfilesArray();
                    int index = arr.indexOf(ModelProfile.instance.getPreviousName());
                    String userName = ModelProfile.instance.getEditProfile().getUserName();
                    Model.instance.getUser().getProfilesArray().set(index, userName);

                    String des = Navigation.findNavController(view).getGraph().getDisplayName();
                    Log.d("TAG4444", des);
                    if(des.equals("com.example.perfectfitapp_android:id/user_profiles_graph")){
                        Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);
                    } else {
                        Navigation.findNavController(view).navigate(R.id.action_global_profileFragment);
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    saveChangesBtn.setEnabled(true);
                    //TODO: dialog
                    showOkDialog();
                }
            });
        }
        else{
            progressBar.setVisibility(View.GONE);
            saveChangesBtn.setEnabled(true);
        }
    }

    private void showOkDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(getResources().getString(R.string.outError));

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}