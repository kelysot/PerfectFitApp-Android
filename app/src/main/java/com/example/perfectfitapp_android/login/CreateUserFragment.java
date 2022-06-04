package com.example.perfectfitapp_android.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.example.perfectfitapp_android.model.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateUserFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button registerBtn;
    LottieAnimationView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_user, container, false);
        emailEt = view.findViewById(R.id.create_user_email_et);
        passwordEt = view.findViewById(R.id.create_user_password_et);
        registerBtn = view.findViewById(R.id.create_user_register_btn);
        registerBtn.setOnClickListener(v -> register());
        progressBar = view.findViewById(R.id.create_user_progress_bar);
        progressBar.setVisibility(View.GONE);
        return view;
    }

    private void register() {
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        /* *************************************** Validations *************************************** */

//        if (!isValidEmail(email)) {
//            progressBar.setVisibility(View.GONE);
//            emailEt.setError("Please enter valid email address.");
//            emailEt.requestFocus();
//            registerBtn.setEnabled(true);
//            return;
//        }
//
//        if (!isValidPassword(password)) {
//            progressBar.setVisibility(View.GONE);
//            passwordEt.requestFocus();
//            String s = "Your password needs to contain 8 characters.";
//            showOkDialog(s);
//            registerBtn.setEnabled(true);
//            return;
//        }

        Model.instance.checkIfEmailExist(email, isSuccess -> {
            if (isSuccess) {
                Model.instance.register(email, password, user1 -> {
                    if (user1 != null) {
                        Model.instance.getUserFromServer(email, user -> {
                            if (user != null) {
                                Model.instance.setUser(user);
                                startActivity(new Intent(getContext(), UserProfilesActivity.class));
                                getActivity().finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                registerBtn.setEnabled(true);
                                showOkDialog(getResources().getString(R.string.outError));
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setEnabled(true);
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                String s = "The email addresses already exist, " + "\n" + "please try a different one.";
                showOkDialog(s);
                registerBtn.setEnabled(true);
            }
        });
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    private void showOkDialog(String text) {
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
}