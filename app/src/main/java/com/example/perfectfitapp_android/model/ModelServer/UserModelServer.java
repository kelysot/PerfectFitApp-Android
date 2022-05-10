package com.example.perfectfitapp_android.model.ModelServer;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserModelServer {
    Server server = new Server();

    public void register(String email, String password, Model.RegisterListener listener) {
        User user = new User(email, password, "true");
        HashMap<String, String> userMap = user.toJson();
        Call<JsonObject> call = server.service.executeRegister(userMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    User user = new User();
                    user = User.jsonObjectToUser(response.body().getAsJsonObject());
                    listener.onComplete(user);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "Wrong email or password",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                    Log.d("TAG", "failed in Register in ModelServer 2");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed to register in ModelServer 2");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });

    }

    public void getUser(String email, Model.GetUserListener listener) {

        Call<JsonObject> callUser = server.service.executeGetUser(email);
        callUser.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 200) {
                    JsonElement js = response.body().get("tokens");
                    String aToken = "Bearer " + js.getAsJsonArray().get(0).getAsString();
                    String rToken = "Bearer " + js.getAsJsonArray().get(1).getAsString();
                    SharedPreferences.Editor preferences = MyApplication.getContext().getSharedPreferences("TAG", ContextThemeWrapper.MODE_PRIVATE).edit();
                    preferences.putString("ACCESS_TOKEN", aToken);
                    preferences.putString("REFRESH_TOKEN", rToken);
                    preferences.commit();

                    User user = new User();
                    user = user.fromJson(response.body());
                    listener.onComplete(user);
                } else if (response.code() == 400) {
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


    public void logIn(String email, String password, Model.LogInListener listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("profilesArray", null);
        Call<JsonObject> call = server.service.executeLogin(map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //JsonObject js = response.body().get("user").getAsJsonObject();
                if (response.code() == 200) {
                    User user = new User();
                    user = User.jsonObjectToUser(response.body().get("user").getAsJsonObject());
                    listener.onComplete(user);
                } else if (response.code() == 404) {
                    Toast.makeText(MyApplication.getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in LogIn in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "Wrong email or password",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                    Log.d("TAG", "failed in LogIn in ModelServer 2");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed in LogIn in ModelServer 3");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later", Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });

    }

    public void checkIfEmailExist(String email, Model.CheckIfEmailExist listener) {

        Call<Void> call = server.service.checkIfEmailExist(email);

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

    public void logout(Model.LogoutListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
//        String refreshToken = sp.getString("REFRESH_TOKEN", "");


        Call<Void> call = server.service.executeLogout(token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 404) {
                    Toast.makeText(MyApplication.getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                } else if (response.code() == 401) {
                    Toast.makeText(MyApplication.getContext(), "No token",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Toast.makeText(MyApplication.getContext(), "invalid request",
                            Toast.LENGTH_LONG).show();
//                    getTokens(isSuccess -> {
//                        if(isSuccess){
//                            Toast.makeText(MyApplication.getContext(), "Please try again",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });

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
}
