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
    Model model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_user, container, false);

        model = Model.instance;

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

        User user = new User(email, password);
        HashMap<String, String> userMap = user.toJson();

        Call<Void> call = model.getRetrofitInterface().executeRegister(userMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){

                    Log.d("TAG", "the response: " + response.message());

                    Call<JsonObject> callUser = model.getRetrofitInterface().executeGetUser(email);

                    callUser.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("TAG", "succeed getCurrentUser");

                            User currentUser = new User();
                            currentUser = currentUser.fromJson(response.body());
                            Model.instance.setUser(currentUser);

                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("TAG", "failed to getCurrentUser");

                            Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed to register");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                registerBtn.setEnabled(true);

            }
        });
    }
}