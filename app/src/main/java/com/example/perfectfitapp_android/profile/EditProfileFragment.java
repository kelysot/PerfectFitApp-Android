package com.example.perfectfitapp_android.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.util.Calendar;


public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;

    TextInputEditText firstNameEt, lastNameEt, birthdayEt, userNameEt;
    EditText genderEt;
    ImageView image, addPhoto, birthdayDateImv;
    TextInputLayout genderTxtIL;
    AutoCompleteTextView genderAuto; // catch the gender
//    String[] genderArr;
//    ArrayAdapter<String> genderAdapter;
    Button continueBtn;
    Bitmap mBitmap;
    CheckBox femaleCB, maleCB;
    String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEt = view.findViewById(R.id.edit_profile_step1_name_et);
        lastNameEt = view.findViewById(R.id.edit_profile_step1_lastname_et);
        userNameEt = view.findViewById(R.id.edit_profile_step1_username_et);
        birthdayEt = view.findViewById(R.id.edit_profile_step1_birthday_et);
        genderEt = view.findViewById(R.id.edit_profile_gendet_txt);
        image = view.findViewById(R.id.edit_profile_step1_image_imv);
        femaleCB = view.findViewById(R.id.edit_profile_step1_female_cb);
        maleCB = view.findViewById(R.id.edit_profile_step1_male_cb);
        birthdayDateImv = view.findViewById(R.id.edit_profile_step1_birthday_imv);

        genderEt.setEnabled(false);

        addPhoto = view.findViewById(R.id.edit_profile_step1_add_photo_imv);
        continueBtn = view.findViewById(R.id.edit_profile_step1_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep2(view));

        ModelProfile.instance.setEditProfile(Model.instance.getProfile());
        firstNameEt.setText(ModelProfile.instance.getEditProfile().getFirstName());
        lastNameEt.setText(ModelProfile.instance.getEditProfile().getLastName());
        userNameEt.setText(ModelProfile.instance.getEditProfile().getUserName());
        birthdayEt.setText(ModelProfile.instance.getEditProfile().getBirthday());

        birthdayDateImv = view.findViewById(R.id.edit_profile_step1_birthday_imv);
        birthdayDateImv.setOnClickListener(v -> {
            pickBirthdayDate();
        });

        if(!ModelProfile.instance.getEditProfile().getGender().isEmpty()){
            String genderFromServer = ModelProfile.instance.getEditProfile().getGender().toString();
            if(genderFromServer.equals("Female")){
                femaleCB.setChecked(true);
                gender = "Female";
            }
            else{
                maleCB.setChecked(true);
                gender = "Male";
            }
        }

        femaleCB.setOnClickListener(v -> {
            if(maleCB.isChecked()){
                maleCB.setChecked(false);
            }
            gender = "Female";
        });

        maleCB.setOnClickListener(v -> {
            if(femaleCB.isChecked()){
                femaleCB.setChecked(false);
            }
            gender = "Male";
        });

        ModelProfile.instance.setPreviousName(Model.instance.getProfile().getUserName());
        addPhoto.setOnClickListener(v -> showImagePickDialog());

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

        continueBtn.setEnabled(false);
        boolean flag = true;

        String firstName = firstNameEt.getText().toString();
        String lastName = lastNameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String birthday = birthdayEt.getText().toString();

        //TODO: stop the error after fix it
        if((!(femaleCB.isChecked())) && (!(maleCB.isChecked()))){
            genderEt.setError("You must chose gender");
            flag = false;
        }

        if(flag) {

            ModelProfile.instance.getEditProfile().setFirstName(firstName);
            ModelProfile.instance.getEditProfile().setLastName(lastName);
            ModelProfile.instance.getEditProfile().setUserName(userName);
            ModelProfile.instance.getEditProfile().setBirthday(birthday);
            ModelProfile.instance.getEditProfile().setGender(gender);


            if (mBitmap != null) {
                Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                    ModelProfile.instance.getEditProfile().setUserImageUrl(url);
                });
            }

            Navigation.findNavController(view).navigate(R.id.action_editProfileFragment2_to_editProfileStep2Fragment2);
        }
        else{
            continueBtn.setEnabled(true);

        }
    }

    private void pickBirthdayDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month +1)  + "/" + year;
        birthdayEt.setText(date);
    }

}