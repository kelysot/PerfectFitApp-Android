package com.example.perfectfitapp_android.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.perfectfitapp_android.R;
import com.google.android.material.textfield.TextInputLayout;


public class RegisterFragment extends Fragment {

    EditText firstNameEt, lastNameEt, EmailEt, PasswordEt, genderEt, birthdatEt;
    ImageView image;
    TextInputLayout genderTxtIL;
    AutoCompleteTextView genderAuto;
    String[] genderArr;
    ArrayAdapter<String> genderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        /****** gender ******/
        genderTxtIL = view.findViewById(R.id.register_step1_gender_txl);
        genderAuto = view.findViewById(R.id.register_step1_gender_et);
        genderArr = getResources().getStringArray(R.array.gender);
        genderAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, genderArr);
        genderAuto.setAdapter(genderAdapter);
        genderAuto.setThreshold(1);

        return view;
    }
}