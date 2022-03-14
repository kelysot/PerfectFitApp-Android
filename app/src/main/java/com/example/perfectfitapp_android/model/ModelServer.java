package com.example.perfectfitapp_android.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelServer {

    static ModelServer instance = null;
    static final String BASE_URL = "http://10.0.2.2:4000";
    RetrofitInterface service;

    public ModelServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

    /************************************************************************************/

    public void getAllPosts(Model.getAllPostsListener listener){

        Call<JsonArray> call = service.executeGetAllPosts();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                //TODO: check the code == 200 or 400
                if(response.isSuccessful()){
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                }
                else{
                    Log.d("TAG", "failed in getAllPosts in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllPosts in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }


    public void getProfileFromServer(String email, String userName, Model.getProfileListener listener){

        Call<JsonObject> call = service.executeGetProfile(email, userName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    Profile profile = new Profile();
                    profile = profile.jsonObjectToProfile(response.body());
                    listener.onComplete(profile);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "problem in getProfile in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();

                Log.d("TAG", "problem in getProfile in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }

    public void createProfile(Profile profile, Model.createProfileListener listener){

        HashMap<String, String> profileMap = new HashMap<>();
        profileMap = profile.toJson();

        Call<Void> call = service.executeCreateProfile(profileMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "problem in createProfile in ModelServer 1");
                    Log.d("TAG", response.message());
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "problem in createProfile in ModelServer 2");
                listener.onComplete(false);
            }
        });
    }


    public void register(String email, String password, Model.registerListener listener){
        User user = new User(email, password);
        HashMap<String, String> userMap = user.toJson();
        Call<Void> call = service.executeRegister(userMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else if (response.code() == 400){
                    Log.d("TAG", "failed to register in ModelServer 1");
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed to register in ModelServer 2");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(false);
            }
        });
    }


    public void getUser(String email, Model.getUserListener listener){

        Call<JsonObject> callUser = service.executeGetUser(email);
        callUser.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.code() == 200){
                    User user = new User();
                    user = user.fromJson(response.body());
                    listener.onComplete(user);
                }
                else if(response.code() == 400){
                    Log.d("TAG", "failed to getUser in ModelServer 1");
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed to getUser in ModelServer 2");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });
    }



    public void logIn(String email, String password, Model.logInListener listener){

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("profilesArray", null);
        Call<User> call = service.executeLogin(map);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                if (response.code() == 200) {
                    listener.onComplete(true);

//                    Call<JsonObject> callUser = retrofitInterface.executeGetUser(localInputIEmail);
//
//
//                    callUser.enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                            if(response.code() == 200){
//                                JsonObject json = response.body();
//                                User user = new User();
//                                user = user.fromJson(json);
//                                Model.instance.setUser(user);
//                                startActivity(new Intent(getContext(), MainActivity.class));
//                                getActivity().finish();
//                            }
//                            else if(response.code() == 400){
//                                Toast.makeText(MyApplication.getContext(), "Wrong email or password",
//                                        Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });

                } else if (response.code() == 404) {

                    Toast.makeText(MyApplication.getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG",  "failed in LogIn in ModelServer 1");


                    listener.onComplete(false);
//                    loginBtn.setEnabled(true);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "Wrong email or password",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                    Log.d("TAG",  "failed in LogIn in ModelServer 2");


//                    loginBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG",  "failed in LogIn in ModelServer 3");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
//                Toast.makeText(MyApplication.getContext(), t.getMessage(),
//                        Toast.LENGTH_LONG).show();

                listener.onComplete(false);

//                loginBtn.setEnabled(true);

            }
        });

    }






    }
