package com.example.perfectfitapp_android.profile.edit_profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.profile.ModelProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.util.Calendar;


public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;

    TextInputEditText firstNameEt, lastNameEt, birthdayEt, userNameEt;
    ImageView image, addPhoto, birthdayDateImv;
    Button continueBtn;
    Bitmap mBitmap;
    CheckBox femaleCB, maleCB, noneCB;
    String gender;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEt = view.findViewById(R.id.edit_profile_step1_name_et);
        lastNameEt = view.findViewById(R.id.edit_profile_step1_lastname_et);
        userNameEt = view.findViewById(R.id.edit_profile_step1_username_et);
        birthdayEt = view.findViewById(R.id.edit_profile_step1_birthday_et);
        image = view.findViewById(R.id.edit_profile_step1_image_imv);
        femaleCB = view.findViewById(R.id.edit_profile_step1_female_cb);
        maleCB = view.findViewById(R.id.edit_profile_step1_male_cb);
        noneCB = view.findViewById(R.id.edit_profile_step1_none_cb);
        birthdayDateImv = view.findViewById(R.id.edit_profile_step1_birthday_imv);
        progressBar = view.findViewById(R.id.edit_profile_step1_progress_bar);
        progressBar.setVisibility(View.GONE);

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
            else if (genderFromServer.equals("Male")){
                maleCB.setChecked(true);
                gender = "Male";
            } else{
                noneCB.setChecked(true);
                gender = "None";
            }
        }

        femaleCB.setOnClickListener(v -> {
            if(maleCB.isChecked()){
                maleCB.setChecked(false);
            }
            else if (noneCB.isChecked()){
                noneCB.setChecked(false);
            }
            gender = "Female";
        });

        maleCB.setOnClickListener(v -> {
            if(femaleCB.isChecked()){
                femaleCB.setChecked(false);
            }
            else if (noneCB.isChecked()){
                noneCB.setChecked(false);
            }
            gender = "Male";
        });

        noneCB.setOnClickListener(v -> {
            if(femaleCB.isChecked()){
                femaleCB.setChecked(false);
            }
            else if (maleCB.isChecked()){
                maleCB.setChecked(false);
            }
            gender = "None";
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
        progressBar.setVisibility(View.VISIBLE);
        continueBtn.setEnabled(false);
        boolean flag = true;

        String userName = userNameEt.getText().toString();

        if (flag) {
            if(!ModelProfile.instance.getEditProfile().getUserName().equals(userName)){
                Model.instance.checkIfUserNameExist(userName, isSuccess -> {
                    if (isSuccess) {
                        continueStep3(view);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        String s = "The user name you choose already exist, please try another one.";
                        showOkDialog(s);
                        continueBtn.setEnabled(true);
                    }
                });
            }else
                continueStep3(view);
        }
        else{
            progressBar.setVisibility(View.GONE);
            continueBtn.setEnabled(true);

        }
    }

    private void continueStep3(View view) {
        //TODO: add validations
        String firstName = firstNameEt.getText().toString();
        String lastName = lastNameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String birthday = birthdayEt.getText().toString();

        boolean flag = true;

        if(firstName.isEmpty()){
            firstNameEt.setError("Please enter your first name");
            continueBtn.setEnabled(true);
            flag = false;
        }
        if(lastName.isEmpty()){
            lastNameEt.setError("Please enter your last name");
            continueBtn.setEnabled(true);
            flag = false;
        }
        if(userName.isEmpty()){
            userNameEt.setError("Please enter your user name");
            continueBtn.setEnabled(true);
            flag = false;
        }
        if(birthday.isEmpty()){
            birthdayEt.setError("Please enter your birthday");
            continueBtn.setEnabled(true);
            flag = false;
        }
        if(!femaleCB.isChecked() && !maleCB.isChecked() && !noneCB.isChecked()){
            String s = "Please chose your gender.";
            showOkDialog(s);
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
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showOkDialog(String text){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
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