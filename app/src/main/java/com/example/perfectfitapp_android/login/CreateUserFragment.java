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
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateUserFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button registerBtn;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:4000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_user, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        emailEt = view.findViewById(R.id.create_user_email_et);
        passwordEt = view.findViewById(R.id.create_user_password_et);

        registerBtn = view.findViewById(R.id.create_user_register_btn);
        registerBtn.setOnClickListener(v-> register(view));

        return view;
    }

    private void register(View view) {

        registerBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        User user = new User(email, password);
        Model.instance.setUser(user);
        HashMap<String, String> userMap = user.toJson();

        Call<Void> call = retrofitInterface.executeRegister(userMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){

                    Log.d("TAG", "the response: " + response.message());
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed to register");
                registerBtn.setEnabled(true);

            }
        });

        //TODO: send userMap to server




    }
}