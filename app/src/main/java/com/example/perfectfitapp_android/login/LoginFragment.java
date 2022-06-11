package com.example.perfectfitapp_android.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
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
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.example.perfectfitapp_android.model.Model;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button loginBtn, signupBtn;
    TextView resetPass;
    LottieAnimationView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEt = view.findViewById(R.id.login_input_email_et);
        passwordEt = view.findViewById(R.id.login_input_password_et);

        resetPass = view.findViewById(R.id.login_reset_password);
        resetPass.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToResetPassword1EmailFragment());
        });

        loginBtn = view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v -> LogIn());

        signupBtn = view.findViewById(R.id.login_signup_btn);
        signupBtn.setOnClickListener(v -> SignUp(view));

        progressBar = view.findViewById(R.id.log_in_progress_bar);
        progressBar.setVisibility(View.GONE);


        return view;
    }

    private void SignUp(View view) {
        System.out.println("move to signup page");
        signupBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_createUserFragment);
    }

    private void LogIn() {

        loginBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String localInputIEmail = emailEt.getText().toString().trim();
        String localInputPassword = passwordEt.getText().toString().trim();

        /* *************************************** Validations *************************************** */

        if (!isValidEmail(localInputIEmail)) {
            progressBar.setVisibility(View.GONE);
            emailEt.setError("Please enter valid email address.");
            emailEt.requestFocus();
            loginBtn.setEnabled(true);
            return;
        }

        if (!isValidPassword(localInputPassword)) {
            progressBar.setVisibility(View.GONE);
            passwordEt.requestFocus();
            String s = "Your password needs to contain 8 characters.";
            showOkDialog(s);
            loginBtn.setEnabled(true);
            return;
        }

        Model.instance.logIn(localInputIEmail, localInputPassword, user1 -> {
            if (user1 != null) {
                Model.instance.getUserFromServer(localInputIEmail, user -> {
                    if (user != null) {
                        Model.instance.setUser(user);
                        startActivity(new Intent(getContext(), UserProfilesActivity.class));
                        getActivity().finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setEnabled(true);
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    String s = "Incorrect email or password," + "\n" + " please try again.";
                    showOkDialog(s);
                    loginBtn.setEnabled(true);
                });
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