package com.example.perfectfitapp_android.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.R;

public class LoginFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button loginBtn, signupBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEt = view.findViewById(R.id.login_input_email_et);
        passwordEt = view.findViewById(R.id.login_input_password_et);

        loginBtn = view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v -> LogIn());

        signupBtn = view.findViewById(R.id.login_signup_btn);
        signupBtn.setOnClickListener(v -> SignUp(view));


        return view;
    }

    private void SignUp(View view) {
        System.out.println("move to signup page");
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_singUpFragment);
    }

    private void LogIn() {

        String localInputIEmail = emailEt.getText().toString().trim();
        String localInputPassword = passwordEt.getText().toString().trim();

        /* *************************************** Validations *************************************** */


        if (!Patterns.EMAIL_ADDRESS.matcher(localInputIEmail).matches()) {
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();

            return;
        }
        if (localInputIEmail.isEmpty()) {
            emailEt.setError("Please enter your Email");
            emailEt.requestFocus();
            return;
        }

        if (localInputPassword.length() < 6) {
            passwordEt.setError("Password length should be at least 6 characters");
            passwordEt.requestFocus();
            return;
        }

        System.out.println("move to home page activity");

        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();

    }
}