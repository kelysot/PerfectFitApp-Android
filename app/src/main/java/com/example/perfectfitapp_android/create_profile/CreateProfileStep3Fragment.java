package com.example.perfectfitapp_android.create_profile;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateProfileStep3Fragment extends Fragment {

    EditText shoulderEt, chestEt, basinEt, waistEt, heightEt, weightEt, footEt;
    Button registerBtn, b, continueBtn;
    ImageView explenationImg;
    Model model;

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

        if(CreateProfileModel.instance.profile.getGender().equals("Female")){
            explenationImg.setImageResource(R.drawable.body_measurement_for_her);
        }
        else{
            explenationImg.setImageResource(R.drawable.body_measurement_for_him);
        }

        registerBtn = view.findViewById(R.id.register_step3_register_btn);
        registerBtn.setOnClickListener(v-> registerApp(view));

//        continueBtn = view.findViewById(R.id.register_step3_continue_btn);
//        continueBtn.setOnClickListener(v-> continueStep4(view));

        //TODO: set image by female or male
        return view;
    }

    private void registerApp(View view) {

        registerBtn.setEnabled(false);

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
            if(isSuccess){
                model.getUser().getProfilesArray().add(CreateProfileModel.instance.profile.getUserName());

                Model.instance.getProfileFromServer(email, userName, profile -> {
                    if(profile != null){
                        Model.instance.setProfile(profile);
                        Navigation.findNavController(view).navigate(R.id.action_registerStep3Fragment2_to_homePageFragment);
                    }
                    else{
                        registerBtn.setEnabled(true);
                    }
                });
            }
            else{
                registerBtn.setEnabled(true);
            }
        });
    }

//    private void continueStep4(View view) {
//        Navigation.findNavController(view).navigate(R.id.action_registerStep3Fragment2_to_homePageFragment);
//    }


    private void pickDate(View view) {
        showDatePickerDialog(view);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }



}