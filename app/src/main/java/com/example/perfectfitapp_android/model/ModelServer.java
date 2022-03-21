package com.example.perfectfitapp_android.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    /******************************************************************************************/

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/

    public void register(String email, String password, Model.RegisterListener listener) {
        User user = new User(email, password);
        HashMap<String, String> userMap = user.toJson();
        Call<Void> call = service.executeRegister(userMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
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


    //**********************************************//
    public void getUser(String email, Model.GetUserListener listener) {

        Call<JsonObject> callUser = service.executeGetUser(email);
        callUser.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 200) {
                    JsonElement js = response.body().get("tokens");
                    Model.instance.setToken(js.getAsString());
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
        Call<User> call = service.executeLogin(map);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 404) {
                    Toast.makeText(MyApplication.getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in LogIn in ModelServer 1");
                    listener.onComplete(false);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "Wrong email or password",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(false);
                    Log.d("TAG", "failed in LogIn in ModelServer 2");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "failed in LogIn in ModelServer 3");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(false);
            }
        });

    }

    public void checkIfEmailExist(String email, Model.CheckIfEmailExist listener) {

        Call<Void> call = service.checkIfEmailExist(email);

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

    //TODO: Understand why logout is Forbidden.
    public void logout(Model.LogoutListener listener) {
        Call<Void> call = service.executeLogout(Model.instance.getToken());
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
                    Log.d("TAG2", response.message());
                    Toast.makeText(MyApplication.getContext(), "invalid request",
                            Toast.LENGTH_LONG).show();
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

    /******************************************************************************************/

    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/


    public void getProfileFromServer(String email, String userName, Model.GetProfileListener listener) {

        Call<JsonObject> call = service.executeGetProfile(Model.instance.getToken(), email, userName);

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

        HashMap<String, Object> profileMap = profile.toJson();

        Call<Void> call = service.executeCreateProfile(Model.instance.getToken(), profileMap);

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

        Call<Void> call = service.checkIfUserNameExist(Model.instance.getToken(), userName);

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

    public void editProfile(String previousName, Profile profile, Model.EditProfile listener) {
        HashMap<String, Object> profileMap = profile.toJson();

        if(previousName != null){
            profileMap.put("previousName", previousName);
        }

        Call<Void> call = service.editProfile(Model.instance.getToken(), profileMap);
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
        Call<Void> call = service.deleteProfile(Model.instance.getToken(), userName);
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

    /******************************************************************************************/

    /*--------------------------------- Post -------------------------------*/

    /******************************************************************************************/

    public void getAllPosts(Model.GetAllPostsListener listener) {

        Call<JsonArray> call = service.executeGetAllPosts(Model.instance.getToken());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                //TODO: check the code == 200 or 400
                if (response.isSuccessful()) {
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                } else {
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

    public void addNewPost(Post post, Model.AddNewPostListener listener) {
        HashMap<String, Object> postMap = post.toJson();
        Call<JsonObject> call = service.addNewPost(Model.instance.getToken(), postMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", response.toString());

                if (response.code() == 200) {
                    Log.d("TAG", response.body().toString());
                    Log.d("TAG", response.body().get("post").getAsJsonObject().get("productName").toString());

                    Post newPost = new Post();
                    newPost = Post.jsonElementToPost(response.body().get("post"));
                    listener.onComplete(newPost);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", response.toString());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "problem in createProfile in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }

    /******************************************************************************************/

    /*--------------------------------- Category -------------------------------*/

    /******************************************************************************************/

    public void getAllCategoriesListener(Model.GetAllCategoriesListener listener) {

        Call<JsonArray> call = service.getAllCategories(Model.instance.getToken());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray categoriesJson = response.body();
//                    Log.d("TAG12", response.body().toString());
                    List<Category> categories = Category.jsonArrayToCategory(categoriesJson);
                    listener.onComplete(categories);
                } else {
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

    /******************************************************************************************/

    /*--------------------------------- SubCategory -------------------------------*/

    /******************************************************************************************/

    public void getAllSubCategories(Model.GetAllSubCategoriesListener listener) {

        Call<JsonArray> call = service.getAllSubCategories(Model.instance.getToken());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    JsonArray subCategoriesJson = response.body();
                    List<SubCategory> subCategories = SubCategory.jsonArrayToSubCategory(subCategoriesJson);
                    listener.onComplete(subCategories);
                }else{
                    Log.d("TAG", "failed in getAllSubCategories1 in ModelServer");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllSubCategories2 in ModelServer");
                listener.onComplete(null);
            }
        });
    }

    public void getSubCategoriesByCategoryId(String categoryId, String gender, Model.GetSubCategoriesByCategoryIdListener listener) {
        Call<JsonObject> call = service.getSubCategoriesByCategoryId(Model.instance.getToken(), categoryId,gender);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    Log.d("TAG","OK");
                    //TODO: get The list from the server
                }else if(response.code() == 400){
                    Log.d("TAG", "failed in getSubCategoriesByCategoryId1 in ModelServer");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "failed in getSubCategoriesByCategoryId2 in ModelServer");
                listener.onComplete(null);
            }
        });
    }
}


