package com.example.perfectfitapp_android.model.ModelServerCalls;

import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.RetrofitInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    static final String BASE_URL = "http://10.0.2.2:4000";
    RetrofitInterface service;
    SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", ContextThemeWrapper.MODE_PRIVATE);

    public Server() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

}
