package com.example.perfectfitapp_android;

import android.util.Log;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
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


    public void getProfile(String email, String userName, Model.getProfileListener listener){

        Call<JsonObject> call = service.executeGetProfile(email, userName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){

                    Profile profile = new Profile();
                    profile = profile.jsonObjectToProfile(response.body());
                    listener.onComplete(profile);
//                    model.setProfile(profile);
//                    Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
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






    }
