package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;

import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryModelServer {

    Server server = new Server();

    public void getAllCategoriesListener(Model.GetAllCategoriesListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getAllCategories(token, Model.instance.getProfile().getGender());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray categoriesJson = response.body();
                    List<Category> categories = Category.jsonArrayToCategory(categoriesJson);
                    for(int i = 0; i < categories.size(); i++){
                        if(categories.get(i).getSubCategory().size() == 0){
                            categories.remove(categories.get(i));
                        }
                    }
                    listener.onComplete(categories);
                } else if(response.code() == 400){
                    Log.d("TAG", "failed in getAllPosts in ModelServer 1");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getAllCategoriesListener(listener);
                        }
                        else{
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllPosts in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }
}
