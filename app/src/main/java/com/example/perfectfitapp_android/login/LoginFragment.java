package com.example.perfectfitapp_android.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.example.perfectfitapp_android.model.Model;

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
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_createUserFragment);
    }

    private void LogIn() {

        loginBtn.setEnabled(false);

        String localInputIEmail = emailEt.getText().toString().trim();
        String localInputPassword = passwordEt.getText().toString().trim();

        /* *************************************** Validations *************************************** */

//        if (!Patterns.EMAIL_ADDRESS.matcher(localInputIEmail).matches()) {
//            emailEt.setError("Please provide valid email");
//            emailEt.requestFocus();
//
//            return;
//        }
//        if (localInputIEmail.isEmpty()) {
//            emailEt.setError("Please enter your Email");
//            emailEt.requestFocus();
//            return;
//        }
//
//        if (localInputPassword.length() < 6) {
//            passwordEt.setError("Password length should be at least 6 characters");
//            passwordEt.requestFocus();
//            return;
//        }


      Model.instance.logIn(localInputIEmail, localInputPassword, user1 -> {
          if(user1 != null){
              Model.instance.getUserFromServer(localInputIEmail, user -> {
                  if(user != null){
                      Model.instance.setUser(user);
                      startActivity(new Intent(getContext(), UserProfilesActivity.class));
                      getActivity().finish();
                  }
                  else{
                      loginBtn.setEnabled(true);
                     // Toast.makeText(MyApplication.getContext(), "No Connection, please try later", Toast.LENGTH_LONG).show();
                  }
              });
          }
          else{
              getActivity().runOnUiThread(() -> {
                  String s = "Incorrect email or password," + "\n" + " please try again.";
                  showOkDialog(s);
                  loginBtn.setEnabled(true);
              });
          }
      });
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
}