package com.example.perfectfitapp_android.create_profile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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


public class CreateProfileStep3Fragment extends Fragment {

    EditText shoulderEt, chestEt, basinEt, waistEt, heightEt, weightEt, footEt;
    Button registerBtn, b, continueBtn;
    ImageView explenationImg;
    Model model;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_profile_step3, container, false);
//        b = view.findViewById(R.id.register_step3_date_btn);
//        b.setOnClickListener(v-> pickDate(view));

        model = Model.instance;

        shoulderEt = view.findViewById(R.id.register_step3_shoulder_et);
        chestEt = view.findViewById(R.id.register_step3_chest_et);
        basinEt = view.findViewById(R.id.register_step3_basin_et);
        waistEt = view.findViewById(R.id.register_step3_waist_et);
        heightEt = view.findViewById(R.id.register_step3_height_et);
        weightEt = view.findViewById(R.id.register_step3_weight_et);
        footEt = view.findViewById(R.id.register_step3_foot_et);
        explenationImg = view.findViewById(R.id.register_step3_explanation_image);
        progressBar = view.findViewById(R.id.register_step3_progress_bar);
        progressBar.setVisibility(View.GONE);

        if(CreateProfileModel.instance.profile.getGender().equals("Female")){
            explenationImg.setImageResource(R.drawable.body_measurement_for_her);
        }
        else{
            explenationImg.setImageResource(R.drawable.body_measurement_for_him);
        }

        registerBtn = view.findViewById(R.id.register_step3_register_btn);
        registerBtn.setOnClickListener(v-> registerApp(view));

        //TODO: set image by female or male
        return view;
    }

    private void registerApp(View view) {

        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        boolean flag = true;

        if(shoulderEt.getText().toString().isEmpty()){
            shoulderEt.setError("Please enter your shoulder size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(chestEt.getText().toString().isEmpty()){
            chestEt.setError("Please enter your chest size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(basinEt.getText().toString().isEmpty()){
            basinEt.setError("Please enter your hips size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(waistEt.getText().toString().isEmpty()){
            waistEt.setError("Please enter your waist size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(heightEt.getText().toString().isEmpty()){
            heightEt.setError("Please enter your height size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(weightEt.getText().toString().isEmpty()){
            weightEt.setError("Please enter your weight size.");
            registerBtn.setEnabled(true);
            flag = false;
        }
        if(footEt.getText().toString().isEmpty()){
            footEt.setError("Please enter your shoes size.");
            registerBtn.setEnabled(true);
            flag = false;
        }

        if(flag) {

            CreateProfileModel.instance.profile.setShoulder(shoulderEt.getText().toString());
            CreateProfileModel.instance.profile.setChest(chestEt.getText().toString());
            CreateProfileModel.instance.profile.setBasin(basinEt.getText().toString());
            CreateProfileModel.instance.profile.setWaist(waistEt.getText().toString());
            CreateProfileModel.instance.profile.setHeight(heightEt.getText().toString());
            CreateProfileModel.instance.profile.setWeight(weightEt.getText().toString());
            CreateProfileModel.instance.profile.setFoot(footEt.getText().toString());

            String email = Model.instance.getUser().getEmail();
            String userName = CreateProfileModel.instance.profile.getUserName();

            CreateProfileModel.instance.profile.setUserId(Model.instance.getUser().getEmail());

            Model.instance.createProfile(CreateProfileModel.instance.profile, isSuccess -> {
                if (isSuccess) {
                    model.getUser().getProfilesArray().add(CreateProfileModel.instance.profile.getUserName());

                    Model.instance.getProfileFromServer(email, userName, profile -> {
                        if (profile != null) {
                            Model.instance.setProfile(profile);
                            Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);
                        } else {
                            registerBtn.setEnabled(true);
                        }
                    });
                } else {
                    registerBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    //TODO: dialog
                    showOkDialog();
                }
            });
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void pickDate(View view) {
        showDatePickerDialog(view);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    private void showOkDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText("Opss.. There is something wrong. Please try again later");

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }


}