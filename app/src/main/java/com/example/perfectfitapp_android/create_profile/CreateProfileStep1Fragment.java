package com.example.perfectfitapp_android.create_profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class CreateProfileStep1Fragment extends Fragment {

    TextInputEditText firstNameEt, lastNameEt, birthdayEt, userNameEt;
    ImageView image;
    TextInputLayout genderTxtIL;
    AutoCompleteTextView genderAuto; // catch the gender
    String[] genderArr;
    ArrayAdapter<String> genderAdapter;
    ImageButton cameraBtn, galleryBtn;
    Button continueBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile_step1, container, false);

        firstNameEt = view.findViewById(R.id.register_step1_name_et);
        lastNameEt = view.findViewById(R.id.register_step1_lastname_et);
        userNameEt = view.findViewById(R.id.register_step1_username_et);
        birthdayEt = view.findViewById(R.id.register_step1_birthday_et);
        image = view.findViewById(R.id.register_step1_image_imv);

        cameraBtn = view.findViewById(R.id.register_step1_camera_imv);
        galleryBtn = view.findViewById(R.id.register_step1_camera_imv);
        continueBtn = view.findViewById(R.id.register_step1_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep2(view));

        setAllDropDownMenus(view);

        //TODO: fix the problem - in case we turn back from f.2 to f.1, we can't choose again female/male.
        // Can use checkbox instead.

        return view;
    }

    private void continueStep2(View view) {

        String firstName = firstNameEt.getText().toString();
        String lastName = lastNameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String birthday = birthdayEt.getText().toString();
        String gender = genderAuto.getText().toString();

        Model.instance.checkIfUserNameExist(userName, new Model.CheckIfUserNameExist() {
            @Override
            public void onComplete(Boolean isSuccess) {
                if(isSuccess){
                    CreateProfileModel.instance.profile.setUserName(userName);
                    CreateProfileModel.instance.profile.setFirstName(firstName);
                    CreateProfileModel.instance.profile.setLastName(lastName);
                    CreateProfileModel.instance.profile.setBirthday(birthday);
                    CreateProfileModel.instance.profile.setGender(gender);
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment2_to_registerStep2Fragment2);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "The user name you choose already exist, please try another one.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setAllDropDownMenus(View view){
        genderTxtIL = view.findViewById(R.id.register_step1_gender_txl);
        genderAuto = view.findViewById(R.id.register_step1_gender_et);

        genderArr = getResources().getStringArray(R.array.gender);
        genderAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, genderArr);
        if(!CreateProfileModel.instance.profile.getGender().equals("")){
            genderAuto.setText(CreateProfileModel.instance.profile.getGender());
        }
        genderAuto.setAdapter(genderAdapter);
        genderAuto.setThreshold(1);

    }
}