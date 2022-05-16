package com.example.perfectfitapp_android.create_profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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

import java.io.InputStream;


public class CreateProfileStep1Fragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;
    private String mImageUrl = "";

    TextInputEditText firstNameEt, lastNameEt, birthdayEt, userNameEt;
    ImageView image, addPhoto;
    TextInputLayout genderTxtIL;
    AutoCompleteTextView genderAuto; // catch the gender
    String[] genderArr;
    ArrayAdapter<String> genderAdapter;
    Button continueBtn, chooseDate;
    Bitmap mBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile_step1, container, false);

        firstNameEt = view.findViewById(R.id.register_step1_name_et);
        lastNameEt = view.findViewById(R.id.register_step1_lastname_et);
        userNameEt = view.findViewById(R.id.register_step1_username_et);
        birthdayEt = view.findViewById(R.id.register_step1_birthday_et);
        image = view.findViewById(R.id.register_step1_image_imv);

        addPhoto = view.findViewById(R.id.register_step1_add_photo_imv);
        continueBtn = view.findViewById(R.id.register_step1_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep2(view));

        addPhoto.setOnClickListener(v -> showImagePickDialog());

        chooseDate = view.findViewById(R.id.register_step1_birthday_btn);
        chooseDate.setOnClickListener(v -> {
            pickDate(view);
        });

        setAllDropDownMenus(view);

        //TODO: fix the problem - in case we turn back from f.2 to f.1, we can't choose again female/male.
        // Can use checkbox instead.

        return view;
    }

    private void showImagePickDialog() {

        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose an Option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i == 0) {
                    openCam();
                }

                if (i == 1) {
                    openGallery();
                }
            }
        });

        builder.create().show();
    }


    public void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void openGallery() {
        Intent photoPicerIntent = new Intent(Intent.ACTION_PICK);
        photoPicerIntent.setType("image/*");
        startActivityForResult(photoPicerIntent, REQUEST_IMAGE_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                mBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(mBitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_PIC) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    mBitmap = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void continueStep2(View view) {

        if (mBitmap != null) {
            Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                mImageUrl = url;
                checkIfUserNameExist(view);
            });
        }
        else checkIfUserNameExist(view);
    }

    public void checkIfUserNameExist(View view){

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
                    CreateProfileModel.instance.profile.setUserImageUrl(mImageUrl);
                    Navigation.findNavController(view).navigate(R.id.action_createProfileStep1Fragment2_to_createProfileStep2Fragment2);
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


    private void pickDate(View view) {
        showDatePickerDialog(view);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");


    }
}