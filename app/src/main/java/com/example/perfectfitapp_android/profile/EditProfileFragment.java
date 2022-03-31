package com.example.perfectfitapp_android.profile;

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
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EditProfileFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEt = view.findViewById(R.id.edit_profile_step1_name_et);
        lastNameEt = view.findViewById(R.id.edit_profile_step1_lastname_et);
        userNameEt = view.findViewById(R.id.edit_profile_step1_username_et);
        birthdayEt = view.findViewById(R.id.edit_profile_step1_birthday_et);
        image = view.findViewById(R.id.edit_profile_step1_image_imv);

        cameraBtn = view.findViewById(R.id.edit_profile_step1_camera_imv);
        galleryBtn = view.findViewById(R.id.edit_profile_step1_camera_imv);
        continueBtn = view.findViewById(R.id.edit_profile_step1_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep2(view));

        ModelProfile.instance.setEditProfile(Model.instance.getProfile());
        firstNameEt.setText(ModelProfile.instance.getEditProfile().getFirstName());
        lastNameEt.setText(ModelProfile.instance.getEditProfile().getLastName());
        userNameEt.setText(ModelProfile.instance.getEditProfile().getUserName());
        birthdayEt.setText(ModelProfile.instance.getEditProfile().getBirthday());

        setAllDropDownMenus(view);

        ModelProfile.instance.setPreviousName(Model.instance.getProfile().getUserName());

        // TODO: set image

        return view;
    }

    private void setAllDropDownMenus(View view) {
        genderTxtIL = view.findViewById(R.id.edit_profile_step1_gender_txl);
        genderAuto = view.findViewById(R.id.edit_profile_step1_gender_et);

        genderArr = getResources().getStringArray(R.array.gender);
        genderAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, genderArr);
        if(!ModelProfile.instance.getEditProfile().getGender().equals("")){
            genderAuto.setText(ModelProfile.instance.getEditProfile().getGender());
        }
        genderAuto.setText(ModelProfile.instance.getEditProfile().getGender());
        genderAuto.setAdapter(genderAdapter);
        genderAuto.setThreshold(1);
    }

    private void continueStep2(View view) {
        String firstName = firstNameEt.getText().toString();
        String lastName = lastNameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String birthday = birthdayEt.getText().toString();
        String gender = genderAuto.getText().toString();

        ModelProfile.instance.getEditProfile().setFirstName(firstName);
        ModelProfile.instance.getEditProfile().setLastName(lastName);
        ModelProfile.instance.getEditProfile().setUserName(userName);
        ModelProfile.instance.getEditProfile().setBirthday(birthday);
        ModelProfile.instance.getEditProfile().setGender(gender);

        Navigation.findNavController(view).navigate(R.id.action_editProfileFragment2_to_editProfileStep2Fragment2);

//        Navigation.findNavController(view).navigate(R.id.action_editProfileFragment_to_editProfileStep2Fragment);
    }
}