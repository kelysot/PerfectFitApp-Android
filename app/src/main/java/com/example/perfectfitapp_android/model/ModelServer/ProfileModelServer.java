package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileModelServer {

    Server server = new Server();
    public void getProfileFromServer(String email, String userName, Model.GetProfileListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonObject> call = server.service.executeGetProfile(token, email, userName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 200) {
                    Profile profile = new Profile();
                    profile = profile.jsonObjectToProfile(response.body());
                    listener.onComplete(profile);
                } else if (response.code() == 400) {
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

    public void createProfile(Profile profile, Model.CreateProfileListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        HashMap<String, Object> profileMap = profile.toJson();

        Call<Void> call = server.service.executeCreateProfile(token, profileMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "problem in createProfile in ModelServer 1");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "problem in createProfile in ModelServer 2");
                listener.onComplete(false);
            }
        });
    }

    public void checkIfUserNameExist(String userName, Model.CheckIfUserNameExist listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<Void> call = server.service.checkIfUserNameExist(token, userName);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(false);
            }
        });
    }

    public void getProfileByUserName(String userName, Model.GetProfileByUserName listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonElement> call = server.service.getProfileByUserName(token, userName);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    JsonElement js = response.body();
                    Profile profile = Profile.jsonElementToProfile(js);
                    listener.onComplete(profile);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });
    }

    public void editProfile(String previousName, Profile profile, Model.EditProfile listener) {
        HashMap<String, Object> profileMap = profile.toJson();

        if(previousName != null){
            profileMap.put("previousName", previousName);
        }

        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<Void> call = server.service.editProfile(token, profileMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed to editProfile in ModelServer 1");
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed to editProfile in ModelServer 2");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(false);
            }
        });
    }

    public void deleteProfile(String userName, Model.DeleteProfileListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = server.service.deleteProfile(token, userName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                }else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG","onFailure");
                listener.onComplete(false);
            }
        });
    }
}