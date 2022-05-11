package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.SubCategory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationModelServer {

    Server server = new Server();

    public void getNotificationsByIds(List<String> notificationsIds, Model.GetNotificationsByIdsListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        String json = new Gson().toJson(notificationsIds);
        Call<JsonArray> call = server.service.getNotificationsByIds(token, json);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){

                    List<Notification> notifications = Notification.jsonArrayToNotification(response.body());
                    listener.onComplete(notifications);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getNotificationsByIds 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getNotificationsByIds 2");
                listener.onComplete(null);
            }
        });
    }

    public void addNewNotification(Notification notification, Model.AddNewNotificationListener listener) {
        HashMap<String, Object> notificationMap = notification.toJson();
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.addNewNotification(token, notificationMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Notification newNotification  = new Notification();
                    newNotification = Notification.jsonElementToNotification(response.body().get("notification"));
                    listener.onComplete(newNotification);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
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

    public void getAllNotification(Model.GetAllNotificationListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getAllNotification(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    JsonArray notificationJson = response.body();
                    List<Notification> notifications = Notification.jsonArrayToNotification(notificationJson);
                    listener.onComplete(notifications);
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

    public void getNotificationById(String notificationId, Model.GetNotificationByIdListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = server.service.getNotificationById(token, notificationId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    Notification notification = Notification.jsonElementToNotification(js);
                    listener.onComplete(notification);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
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
