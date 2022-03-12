package com.example.perfectfitapp_android.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.User;

import java.util.HashMap;


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
        registerBtn.setOnClickListener(v-> register(view));

        return view;
    }

    private void register(View view) {

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        User user = new User(email, password);
        HashMap<String, String> userMap = user.toJson();

        //TODO: send userMap to server



        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();

    }
}