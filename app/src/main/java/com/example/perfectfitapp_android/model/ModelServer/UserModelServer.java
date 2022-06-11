package com.example.perfectfitapp_android.model.ModelServer;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.AppLocalDb;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class UserModelServer {
    Server server = new Server();

    public void register(String email, String password, Model.RegisterListener listener) {
        User user = new User(email, password, "true");
        HashMap<String, Object> userMap = user.toJson();
        Call<JsonObject> call = server.service.executeRegister(userMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    User user = new User();
                    user = User.jsonObjectToUser(response.body().getAsJsonObject());
                    listener.onComplete(user);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in Register in ModelServer 2");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed to register in ModelServer 2");
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
                    if (js.getAsJsonArray().size() != 0) {
                        String aToken = "Bearer " + js.getAsJsonArray().get(0).getAsString();
                        String rToken = null;
                        if (js.getAsJsonArray().size() > 1) {
                            rToken = "Bearer " + js.getAsJsonArray().get(1).getAsString();
                        }
                        SharedPreferences.Editor preferences = MyApplication.getContext().getSharedPreferences("TAG", ContextThemeWrapper.MODE_PRIVATE).edit();
                        preferences.putString("ACCESS_TOKEN", aToken);
                        if (js.getAsJsonArray().size() > 1) {
                            preferences.putString("REFRESH_TOKEN", rToken);
                        }
                        preferences.commit();

                        User user = new User();
                        user = user.fromJson(response.body());
                        listener.onComplete(user);
                    } else {
                        listener.onComplete(null);
                    }
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed to getUser in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed to getUser in ModelServer 2");
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

                if (response.code() == 200) {
                    User user = new User();
                    user = User.jsonObjectToUser(response.body().get("user").getAsJsonObject());
                    listener.onComplete(user);
                } else if (response.code() == 404) {
                    Log.d("TAG", "failed in LogIn in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in LogIn in ModelServer 2");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed in LogIn in ModelServer 3");
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
                listener.onComplete(false);
            }
        });
    }

    public void logout(Model.LogoutListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = server.service.executeLogout(token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 404) {
                    listener.onComplete(false);
                } else if (response.code() == 401) {
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token in logout *********************************");
                            logout(listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onComplete(false);
            }
        });
    }

    public void general(Model.generalListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call call = server.service.general(token);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    System.out.println("general worked");
                } else if (response.code() == 400) {
                    System.out.println("general not worked " + response.code());
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            general(listener);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("general not worked: " + t.getMessage());
            }
        });
    }

    public void editUser(String previousEmail, User user, Model.EditUserListener listener) {
        HashMap<String, Object> userMap = user.toJson();

        if (previousEmail != null) {
            userMap.put("previousEmail", previousEmail);
        }
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.editUser(token, userMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    User user = new User();
                    user = user.jsonElementToUser(response.body().get("user"));
                    listener.onComplete(user);

                } else if (response.code() == 400) {
                    Log.d("TAG", "failed to editUser in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            editUser(previousEmail, user, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed to editUser in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }

    public void resetPassword(String email, Model.ResetPasswordListener listener) {

        Call<JsonObject> call = server.service.resetPassword(email);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    listener.onComplete(response.body().get("code").getAsString());
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "problem in resetPassword");
                listener.onComplete(null);
            }
        });
    }

    public void changePassword(User user, Model.ChangePasswordListener listener) {
        HashMap<String, Object> userMap = user.toJson();

        Call<Void> call = server.service.changePassword(userMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in changePassword");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed in changePassword");
                listener.onComplete(false);
            }
        });
    }


    public void refreshToken(Model.refreshTokenListener listener) {

        System.out.println("********************************** inside the refresh **********************************");
        String token = server.sp.getString("REFRESH_TOKEN", "");

        Call<JsonObject> call = server.service.refreshToken(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    String tokena = server.sp.getString("ACCESS_TOKEN", "");
                    String tokenr = server.sp.getString("ACCESS_TOKEN", "");

                    List<String> tokenList = new ArrayList<>();
                    JsonObject js = response.body();
                    tokenList.add(js.get("accessToken").getAsString());
                    tokenList.add(js.get("refreshToken").getAsString());
                    listener.onComplete(tokenList);
                } else {
                    Model.instance.setRefreshFlag(true);
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onComplete(null);
            }
        });
    }


}
