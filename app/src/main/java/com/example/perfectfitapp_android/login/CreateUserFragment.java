package com.example.perfectfitapp_android.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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


public class CreateUserFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button registerBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_user, container, false);
        emailEt = view.findViewById(R.id.create_user_email_et);
        passwordEt = view.findViewById(R.id.create_user_password_et);
        registerBtn = view.findViewById(R.id.create_user_register_btn);
        registerBtn.setOnClickListener(v-> register());
        return view;
    }

    private void register() {
        //TODO: add validations of email and password

        registerBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        Model.instance.checkIfEmailExist(email, isSuccess -> {
            if(isSuccess){
                Model.instance.register(email, password, user1 -> {
                    if(user1 != null){
                        Model.instance.getUserFromServer(email, user -> {
                            if(user != null){
                                Model.instance.setUser(user);
                                startActivity(new Intent(getContext(), UserProfilesActivity.class));
                                getActivity().finish();
                            }
                            else{
                                registerBtn.setEnabled(true);
                            }
                        });
                    }
                    else{
                        registerBtn.setEnabled(true);
                    }
                });
            } else {
                String s = "The email addresses already exist, " + "\n" + "please try a different one.";
                showOkDialog(s);
                registerBtn.setEnabled(true);
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