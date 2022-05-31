package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Comment;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentModelServer {

    Server server = new Server();

    public void getCommentsByPostId(String postId, Model.GetCommentsByPostIdListener listener){
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getCommentsByPostId(token, postId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray commentsJson = response.body();
                    List<Comment> comments = Comment.jsonArrayToCategory(commentsJson);
                    listener.onComplete(comments);
                }
                else if(response.code() == 400) {
                    Log.d("TAG", "failed in getCommentsByPostId");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getCommentsByPostId(postId, listener);
                        }
                        else{
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });
    }

    public void addNewComment(Comment comment, Model.AddNewCommentListener listener) {
        HashMap<String, Object> commentMap = comment.toJson();
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.addNewComment(token, commentMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 200) {

                    Comment newComment = new Comment();
                    newComment = Comment.jsonElementToComment(response.body().get("comment"));
                    listener.onComplete(newComment);
                } else if (response.code() == 400) {
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            addNewComment(comment, listener);
                        }
                        else{
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                listener.onComplete(null);
            }
        });
    }


    public void getCommentById(String commentId, Model.GetCommentByIdListener listener) {

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = server.service.getCommentById(token, commentId);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    Comment comment = Comment.jsonElementToComment(js);
                    listener.onComplete(comment);
                }
                else if(response.code() == 400){
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getCommentById 1");
                    listener.onComplete(null);
                }
                else if(response.code() == 403){
                    Model.instance.refreshToken(tokensList -> {
                        if(tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getCommentById(commentId, listener);
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
                Log.d("TAG", "failed in ModelServer in getCommentById 2");
                listener.onComplete(null);
            }
        });
    }

}
