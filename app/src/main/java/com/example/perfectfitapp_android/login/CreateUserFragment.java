package com.example.perfectfitapp_android.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

        registerBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        Model.instance.checkIfEmailExist(email, new Model.CheckIfEmailExist() {
            @Override
            public void onComplete(Boolean isSuccess) {
                if(isSuccess){
                    Model.instance.register(email, password, isSuccess1 -> {
                        if(isSuccess1){
                            Model.instance.getUserFromServer(email, user -> {
                                if(user != null){
                                    Model.instance.setUser(user);
                                    startActivity(new Intent(getContext(), MainActivity.class));
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
                    Toast.makeText(MyApplication.getContext(), "Your email addresses already exist in our database, " +
                                    "please try a different one", Toast.LENGTH_LONG).show();
                    registerBtn.setEnabled(true);
                }
            }
        });

    }
}