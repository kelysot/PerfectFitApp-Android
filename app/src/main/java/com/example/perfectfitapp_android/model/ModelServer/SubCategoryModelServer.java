package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.SubCategory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryModelServer {

    Server server = new Server();

    public void getAllSubCategories(Model.GetAllSubCategoriesListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getAllSubCategories(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    JsonArray subCategoriesJson = response.body();
                    List<SubCategory> subCategories = SubCategory.jsonArrayToSubCategory(subCategoriesJson);
                    listener.onComplete(subCategories);
                }else if(response.code() == 400){
                    Log.d("TAG", "failed in getAllSubCategories1 in ModelServer");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getAllSubCategories(listener);
                        }
                        else{
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

    public void getSubCategoriesByCategoryId(String categoryId, String gender, Model.GetSubCategoriesByCategoryIdListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getSubCategoriesByCategoryId(token, categoryId, gender);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    List<SubCategory> subCategories = new ArrayList<>();
                    subCategories = SubCategory.jsonArrayToSubCategory(response.body());
                    listener.onComplete(subCategories);
                    //TODO: get The list from the server
                }else if(response.code() == 400){
                    Log.d("TAG", "failed in getSubCategoriesByCategoryId1 in ModelServer");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getSubCategoriesByCategoryId(categoryId, gender, listener);
                        }
                        else{
                            listener.onComplete(null);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getSubCategoriesByCategoryId2 in ModelServer");
                Log.d("TAG", t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getSubCategoryById(String subCategoryId, Model.GetSubCategoryById listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = server.service.getSubCategoryById(token, subCategoryId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    SubCategory subCategory = SubCategory.jsonElementToSubCategory(js);
                    listener.onComplete(subCategory);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getSubCategoryById(subCategoryId, listener);
                        }
                        else{
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });
    }
}
