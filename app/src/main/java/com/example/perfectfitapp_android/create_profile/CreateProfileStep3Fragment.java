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
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:4000";
    Model model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_profile_step3, container, false);
//        b = view.findViewById(R.id.register_step3_date_btn);
//        b.setOnClickListener(v-> pickDate(view));

        model = Model.instance;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


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

        CreateProfileModel.instance.profile.setShoulder(shoulderEt.getText().toString());
        CreateProfileModel.instance.profile.setChest(chestEt.getText().toString());
        CreateProfileModel.instance.profile.setBasin(basinEt.getText().toString());
        CreateProfileModel.instance.profile.setWaist(waistEt.getText().toString());
        CreateProfileModel.instance.profile.setHeight(heightEt.getText().toString());
        CreateProfileModel.instance.profile.setWeight(weightEt.getText().toString());
        CreateProfileModel.instance.profile.setFoot(footEt.getText().toString());

        CreateProfileModel.instance.profile.setUserId(Model.instance.getUser().getEmail());

        HashMap<String, String> profileMap = new HashMap<>();
        profileMap = CreateProfileModel.instance.profile.toJson();

        //TODO: change to ModelServer

        Call<Void> call = retrofitInterface.executeCreateProfile(profileMap); // the send call to the server

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){

                    model.getUser().getProfilesArray().add(CreateProfileModel.instance.profile.getUserName());
                    Navigation.findNavController(view).navigate(R.id.action_registerStep3Fragment2_to_homePageFragment);
                }
                else if(response.code() == 400){
                    Log.d("TAG", response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", t.getMessage());
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