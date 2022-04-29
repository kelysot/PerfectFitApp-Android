package com.example.perfectfitapp_android.model;

import static com.google.gson.internal.bind.TypeAdapters.URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelServer {

    static ModelServer instance = null;
    static final String BASE_URL = "http://10.0.2.2:4000";
    RetrofitInterface service;
    SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", ContextThemeWrapper.MODE_PRIVATE);

    public ModelServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitInterface.class);
    }

    public void uploadImage(Bitmap imageBytes, Context context, Model.UploadImageListener listener) {
      //  RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        File filesDir = context.getFilesDir();
        File file = new File(filesDir, "image" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBytes.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        Call<JsonObject> call = service.uploadImage(body, name);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject responseBody = response.body();
                    Log.d("TAG", responseBody.toString());
//                    String mImageUrl = URL + responseBody.get("path").toString();
                    String mImageUrl = responseBody.get("path").toString();
                    Log.d("TAG", mImageUrl);
                    listener.onComplete(mImageUrl);

                } else {
                    Toast.makeText(MyApplication.getContext(), "Didn't Upload pics.",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
                listener.onComplete(null);

            }

        });
    }

    public void getImages(String urlImage, Model.GetImagesListener listener){
        Call<ResponseBody> call = service.getImage(urlImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    if(response.body() != null){
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        listener.onComplete(bitmap);
                    }
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "Didn't get pics.",
                            Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
                listener.onComplete(null);
            }
        });

    }

    /******************************************************************************************/

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/

    public void register(String email, String password, Model.RegisterListener listener) {
        User user = new User(email, password, "true");
        HashMap<String, String> userMap = user.toJson();
        Call<JsonObject> call = service.executeRegister(userMap);
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

        Call<JsonObject> callUser = service.executeGetUser(email);
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
        Call<JsonObject> call = service.executeLogin(map);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject js = response.body().get("user").getAsJsonObject();
                Log.d("TAG", js.toString());
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

        String token = sp.getString("ACCESS_TOKEN", "");
//        String refreshToken = sp.getString("REFRESH_TOKEN", "");


        Call<Void> call = service.executeLogout(token);
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

    /******************************************************************************************/

    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/


    public void getProfileFromServer(String email, String userName, Model.GetProfileListener listener) {

        String token = sp.getString("ACCESS_TOKEN", "");

        Call<JsonObject> call = service.executeGetProfile(token, email, userName);

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
        String token = sp.getString("ACCESS_TOKEN", "");
        HashMap<String, Object> profileMap = profile.toJson();

        Call<Void> call = service.executeCreateProfile(token, profileMap);

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

        String token = sp.getString("ACCESS_TOKEN", "");

        Call<Void> call = service.checkIfUserNameExist(token, userName);

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

        String token = sp.getString("ACCESS_TOKEN", "");

        Call<Void> call = service.editProfile(token, profileMap);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = service.deleteProfile(token, userName);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.executeGetAllPosts(token);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = service.addNewPost(token, postMap);
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


    public void deletePost(String postId, Model.deletePostFromServerListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = service.deletePost(token, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in deletePost 1");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in deletePost 2");
                listener.onComplete(false);
            }
        });
    }

    public void editPost(Post post, Model.editPostListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        HashMap<String, Object> postMap = post.toJson();

        Call<Void> call = service.editPost(token, postMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in editPost 1");
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in editPost 2");
                listener.onComplete(false);
            }
        });
    }


    public void getProfilePosts(String userName, Model.getProfilePostsListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getProfilePosts(token, userName);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
//                    JsonArray profilePostsArray = response.body();
//                    List<Post> posts = new ArrayList<>();
                    List<Post> posts = Post.jsonArrayToPost(response.body());
                    listener.onComplete(posts);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });
    }


    public void getPostById(String postId, Model.getPostByIdListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = service.getPostById(token, postId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    Post post = Post.jsonElementToPost(js);
                    listener.onComplete(post);
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
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if(response.code() == 200){
//                    JsonElement js = response.body();
//                    Post post = Post.jsonElementToPost(js);
//                    listener.onComplete(post);
//                }
//                else{
//                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                            Toast.LENGTH_LONG).show();
//                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
//                    listener.onComplete(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                        Toast.LENGTH_LONG).show();
//                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
//                listener.onComplete(null);
//            }
//        });


    }

    public void getPostsBySubCategoryId(String subCategoryId, Model.GetPostsBySubCategoryIdListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getPostsBySubCategoryId(token, subCategoryId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });

    }

    /******************************************************************************************/

    /*--------------------------------- Category -------------------------------*/

    /******************************************************************************************/

    public void getAllCategoriesListener(Model.GetAllCategoriesListener listener) {

        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getAllCategories(token, Model.instance.getProfile().getGender());
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

        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getAllSubCategories(token);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getSubCategoriesByCategoryId(token, categoryId, gender);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = service.getSubCategoryById(token, subCategoryId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.code() == 200){
                    JsonElement js = response.body();
                    SubCategory subCategory = SubCategory.jsonElementToSubCategory(js);
                    listener.onComplete(subCategory);
                }
                else{
                    Log.d("TAG", "444444");
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


    public void getWishList(Model.getWishListListener listener){

        //TODO: check if the post belongs to the profile
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getWishList(token, Model.instance.getProfile().getUserName());

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    //TODO

                    JsonArray wishListArray = response.body();
                    List<Post> postsWishList = new ArrayList<>();
                    postsWishList = Post.jsonArrayToPost(response.body());
                    listener.onComplete(postsWishList);

                }
                else{
                    Log.d("TAG", "failed in getWishList in ModelServer 1");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getWishList in ModelServer 2");
                listener.onComplete(null);
            }
        });

    }

    /******************************************************************************************/

    /*--------------------------------- Comment -------------------------------*/

    /******************************************************************************************/

    public void getCommentsByPostId(String postId, Model.GetCommentsByPostIdListener listener){
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = service.getCommentsByPostId(token, postId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray commentsJson = response.body();
                    List<Comment> comments = Comment.jsonArrayToCategory(commentsJson);
                    listener.onComplete(comments);
                }
                else {
                    Log.d("TAG", "failed in getCommentsByPostId");
                    listener.onComplete(null);
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
        String token = sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = service.addNewComment(token, commentMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("TAG", response.body().toString());
                if (response.code() == 200) {

                    Comment newComment = new Comment();
                    newComment = Comment.jsonElementToComment(response.body().get("comment"));
                    listener.onComplete(newComment);
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
                listener.onComplete(null);
            }
        });
    }

    public void getDatesFromServer(String date, Model.getDatesListener listener) {
        String token = sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = service.getDates(token, date);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 200){
                    System.out.println("222222222222222222222222222222222222222222");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
}


