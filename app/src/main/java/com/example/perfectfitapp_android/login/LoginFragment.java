package com.example.perfectfitapp_android.login;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.BinderThread;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.BindException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button loginBtn, signupBtn;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:4000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

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
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
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

        HashMap<String, String> map = new HashMap<>();

        map.put("email", localInputIEmail);
        map.put("password", localInputPassword);
        map.put("profilesArray", null);

        Call<User> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                if (response.code() == 200) {
                    Toast.makeText(MyApplication.getContext(), "It Worked!!!",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                    return;

                } else if (response.code() == 404) {
                    Log.d("TAG111",  call.toString());
                    Log.d("TAG112",  response.message());
                    Toast.makeText(MyApplication.getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();

                    loginBtn.setEnabled(true);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "Wrong email or password",
                            Toast.LENGTH_LONG).show();
                    loginBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG111",  t.getMessage());
                System.out.println( "sout***************************************" + t.getMessage());
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
//                Toast.makeText(MyApplication.getContext(), t.getMessage(),
//                        Toast.LENGTH_LONG).show();
                loginBtn.setEnabled(true);

            }
        });
    }
}