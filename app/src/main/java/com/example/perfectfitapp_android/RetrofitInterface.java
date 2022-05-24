package com.example.perfectfitapp_android;

import android.util.Log;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {


    /*--------------------------------- Images -------------------------------*/

    @Multipart
    @POST("images/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @GET("/images/image/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/

    @POST("/auth/login")
    Call<JsonObject> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/register")
    Call<JsonObject> executeRegister (@Body HashMap<String,Object> map);

    @POST("/auth/logout")
    Call<Void> executeLogout(@Header("authorization") String token);

    @GET("/auth/getUser/{email}")
    Call<JsonObject> executeGetUser (@Path("email") String email);

    @GET("/auth/checkIfEmailExist/{email}")
    Call<Void> checkIfEmailExist(@Path("email") String email);

    @GET("/profile/:{id}")
    Call<JsonObject> getUser(@Path("id") String id);
    // http://10.0.2.2:4000/profile/1
    // Call<User> service.getUser(1);

    @GET("/auth/refreshToken")
    Call<JsonObject> getTokens (@Header("authorization") String token);

    @PATCH("/auth/")
    Call<JsonObject> editUser(@Header("authorization") String token, @Body HashMap<String, Object> map);

    @GET("/auth/resetPassword/{email}")
    Call<JsonObject> resetPassword(@Path("email") String email);

    @POST("/auth/changePassword")
    Call<Void> changePassword(@Body HashMap<String, Object> map);

    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/

    @GET("/profile/getProfile/{email}/{userName}")
    Call<JsonObject> executeGetProfile (@Header("authorization") String token,@Path("email") String email, @Path("userName") String userName);

    @POST("/profile/")
    Call<Void> executeCreateProfile (@Header("authorization") String token, @Body HashMap<String, Object>  map);

    @GET("/profile/checkIfUserNameExist/{userName}")
    Call<Void> checkIfUserNameExist(@Header("authorization") String token, @Path("userName") String userName);

    @PATCH("/profile/")
    Call<Void> editProfile(@Header("authorization") String token, @Body HashMap<String, Object> map);

    @DELETE("/profile/{userName}")
    Call<Void> deleteProfile(@Header("authorization") String token, @Path("userName") String userName);

    @GET("/profile/getProfile/{userName}")
    Call<JsonElement> getProfileByUserName(@Header("authorization") String token, @Path("userName") String userName);

    @GET("/profile/getProfilesByUserNames/{userNames}")
    Call<JsonArray> getProfilesByUserNames(@Header("authorization") String token, @Path("userNames") List<String> userNames);

    @GET("/profile")
    Call<JsonArray> getAllProfile(@Header("authorization") String token);

    /*--------------------------------- Post -------------------------------*/

    /******************************************************************************************/

    @GET("/post")
    Call<JsonArray> executeGetAllPosts(@Header("authorization") String token);

    @POST("/post")
    Call<JsonObject> addNewPost(@Header("authorization") String token, @Body HashMap<String, Object> map);

    @GET("/post/getWishList/{userName}")
    Call<JsonArray> getWishList(@Header("authorization") String token, @Path("userName") String userName);

    @DELETE("/post/{postId}")
    Call<Void> deletePost(@Header("authorization") String token, @Path("postId") String postId);

    @PATCH("/post")
    Call<Void> editPost(@Header("authorization") String token, @Body HashMap<String, Object> post);
//    Call<JsonArray> getWishList(@Header("authorization") String token, @Path("wishListId") List<String> wishListId);

    @GET("/post/getProfilePosts/{userName}")
    Call<JsonArray> getProfilePosts(@Header("authorization") String token, @Path("userName") String userName);

    @GET("/post/getPostById/{postId}")
    Call<JsonElement> getPostById(@Header("authorization") String token, @Path("postId") String postId);

    @GET("/post/getPostsBySubCategoryId/{subCategoryId}")
    Call<JsonArray> getPostsBySubCategoryId(@Header("authorization") String token, @Path("subCategoryId") String subCategoryId);

    @GET("/post/getSuitablePosts/{profileId}")
    Call<JsonArray> getSuitablePosts(@Header("authorization") String token, @Path("profileId") String profileId);

//    @GET("/post/getSearchPosts}")
//    Call<JsonArray> getSearchPosts(@Header("authorization") String token, @Query("map") HashMap<String, List<String>> map);
//
//    @GET ("/post/getSearchPosts")
//    Call<JsonArray> getSearchPosts(@Header("authorization") String token, @Query("map") HashMap<String, List<String>> map);

    @POST ("/post/getSearchPosts")
    Call<JsonArray> getSearchPosts(@Header("authorization") String token, @Body HashMap<String, List<String>> map);
    /*--------------------------------- Category -------------------------------*/

    @GET("/category/{gender}")
    Call<JsonArray> getAllCategories(@Header("authorization") String token, @Path("gender") String gender );

    @GET("/post/getPostsByIds/{postsId}")
    Call<JsonArray> getPostsByIds(@Header("authorization") String token, @Path("postsId") List<String> postsId);

    /*--------------------------------- SubCategory -------------------------------*/

    @GET("/subcategory")
    Call<JsonArray> getAllSubCategories(@Header("authorization") String token);

    @GET("/subCategory/{categoryId}/{gender}")
    Call<JsonArray> getSubCategoriesByCategoryId(@Header("authorization") String token, @Path("categoryId") String categoryId , @Path("gender") String gender);

    @GET("/subCategory/getSubCategoryById/{subCategoryId}")
    Call<JsonElement> getSubCategoryById(@Header("authorization") String token, @Path("subCategoryId") String subCategoryId);

    /*--------------------------------- Comment -------------------------------*/

    @GET("/comment/{postId}")
    Call<JsonArray> getCommentsByPostId(@Header("authorization") String token, @Path("postId") String postId );

    @POST("/comment")
    Call<JsonObject> addNewComment(@Header("authorization") String token, @Body HashMap<String, Object> map);

    /*--------------------------------- Dates -------------------------------*/

    @GET("/post/getDates/{date}")
    Call<JsonArray> getDates (@Header("authorization") String token, @Path("date") String dates);

    @GET("/post/timeSince/{date}")
    Call<JsonElement> timeSince (@Header("authorization") String token, @Path("date") String dates);

    /*--------------------------------- Notifications -------------------------------*/

    @GET("/notification/getNotificationsByIds/{notificationsIds}")
    Call<JsonArray> getNotificationsByIds(@Header("authorization") String token, @Path("notificationsIds") List<String> notificationsIds);

    @POST("/notification")
    Call<JsonObject> addNewNotification(@Header("authorization") String token, @Body HashMap<String, Object> map);

    @GET("/notification")
    Call<JsonArray> getAllNotification(@Header("authorization") String token);

    @GET("/notification/getNotificationById/{notificationId}")
    Call<JsonElement> getNotificationById(@Header("authorization") String token, @Path("notificationId") String notificationId);

    @PATCH("/notification")
    Call<Void> editNotification(@Header("authorization") String token, @Body HashMap<String, Object> notification);

    /*--------------------------------- Search -------------------------------*/

    @POST("/post/general")
    Call<Void> general(@Header("authorization") String token);

//    @GET("/post/getGeneral")
//    Call<JsonArray> getGeneral(@Header("authorization") String token);

    @GET("/post/getGeneral")
    Call<JsonObject> getGeneral(@Header("authorization") String token);

}
