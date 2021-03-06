package com.example.perfectfitapp_android.model.ModelServer;

import android.content.Intent;
import android.util.Log;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

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
                System.out.println("the code in getProfileFromServer is: " + response.code());
                if (response.code() == 200) {
                    Profile profile = new Profile();
                    profile = profile.jsonObjectToProfile(response.body());
                    listener.onComplete(profile);
                } else if (response.code() == 400) {
                    Log.d("TAG", "problem in getProfile in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    System.out.println("the token is forbidden, need to do a refreshToken - code 403 in getProfileFromServer");
                    Log.d("TAG", "the token is forbidden, need to do a refreshToken - code 403 in getProfileFromServer");
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getProfileFromServer(email, userName, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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
                    Log.d("TAG", "problem in createProfile in ModelServer 1");
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            createProfile(profile, listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            checkIfUserNameExist(userName, listener);
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
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getProfileByUserName(userName, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                listener.onComplete(null);
            }
        });
    }

    public void editProfile(String previousName, Profile profile, Model.EditProfile listener) {
        HashMap<String, Object> profileMap = profile.toJson();

        if (previousName != null) {
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
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            editProfile(previousName, profile, listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed to editProfile in ModelServer 2");
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
                } else if (response.code() == 400) {
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            deleteProfile(userName, listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "onFailure");
                listener.onComplete(false);
            }
        });
    }

    public void getProfilesByUserNames(List<String> userNames, Model.GetProfilesByUserNamesListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getProfilesByUserNames(token, userNames);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    List<Profile> profiles = Profile.jsonArrayToProfile(response.body());
                    listener.onComplete(profiles);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getProfilesByUserNames(userNames, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });
    }

    //Not used - if won't needed we can delete.
    public void getAllProfile(Model.GetAllProfileListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getAllProfile(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray profileJson = response.body();
                    List<Profile> profiles = Profile.jsonArrayToProfile(profileJson);
                    listener.onComplete(profiles);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in getAllSubCategories1 in ModelServer");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getAllProfile(listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllSubCategories2 in ModelServer");
                listener.onComplete(null);
            }
        });
    }
}
