package com.example.perfectfitapp_android.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.perfectfitapp_android.R;
import com.google.android.material.textfield.TextInputLayout;


public class RegisterFragment extends Fragment {

    EditText firstNameEt, lastNameEt, EmailEt, PasswordEt, birthdatEt;
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firstNameEt = view.findViewById(R.id.register_step1_name_et);
        lastNameEt = view.findViewById(R.id.register_step1_lastname_et);
        EmailEt = view.findViewById(R.id.register_step1_email_et);
        PasswordEt = view.findViewById(R.id.register_step1_password_et);
        birthdatEt = view.findViewById(R.id.register_step1_birthday_et);
        image = view.findViewById(R.id.register_step1_image_imv);

        cameraBtn = view.findViewById(R.id.register_step1_camera_imv);
        galleryBtn = view.findViewById(R.id.register_step1_camera_imv);
        continueBtn = view.findViewById(R.id.register_step1_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep2(view));


        /****** gender ******/
        genderTxtIL = view.findViewById(R.id.register_step1_gender_txl);
        genderAuto = view.findViewById(R.id.register_step1_gender_et);
        genderArr = getResources().getStringArray(R.array.gender);
        genderAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, genderArr);
        genderAuto.setAdapter(genderAdapter);
        genderAuto.setThreshold(1);

        return view;
    }

    private void continueStep2(View view) {
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_registerStep2Fragment);
    }
}