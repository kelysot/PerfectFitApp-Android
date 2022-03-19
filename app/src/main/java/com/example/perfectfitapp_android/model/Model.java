package com.example.perfectfitapp_android.model;

import android.content.Context;
import android.util.Log;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.RetrofitInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {

    public static final Model instance = new Model();
    int count = 0;
    User user;
    String token;
    Profile profile;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http://10.0.2.2:4000";
    List<Post> data = new LinkedList<Post>();
    List<Category> categories = new LinkedList<Category>();
    List<SubCategory> subCategories = new ArrayList<>();
    ModelServer modelServer = new ModelServer();


    public Model(){
        this.count = 0;
        user = new User();
        profile = new Profile();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public RetrofitInterface getRetrofitInterface() {
        return retrofitInterface;
    }

    public void setRetrofitInterface(RetrofitInterface retrofitInterface) {
        this.retrofitInterface = retrofitInterface;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "Bearer " + token;
    }

    public List<Post> getAllPosts(){
        return data;
    }

    public List<Category> getAllCategories(){
        return categories;
    }

    public List<SubCategory> getAllSubCategories(){
        return subCategories;
    }

    public void addPost(Post post){
        data.add(post);
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public void addSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
    }

    public Post getPostById(String postId) {
        for (Post s:data
        ) {
            if (s.getPostId().equals(postId)){
                return s;
            }
        }
        return null;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void deletePost(int pos){ data.remove(pos); }
    public void deletePostByPost(Post post){

        int index = data.indexOf(post);
        data.remove(index);
    }
    public Post getPost(int pos){ return data.get(pos); }


    /******************************************************************************************/

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/


    public interface RegisterListener{
        void onComplete(Boolean isSuccess);
    }

    public void register(String email, String password, RegisterListener listener){
        modelServer.register(email, password, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetUserListener{
        void onComplete(User user);
    }

    public void getUserFromServer(String email, GetUserListener listener){
        modelServer.getUser(email, listener);
    }

    /*--------------------------------------------------------*/

    public interface LogInListener{
        void onComplete(Boolean isSuccess);
    }

    public void logIn(String email, String password, LogInListener listener){
        modelServer.logIn(email, password, listener);
    }


    /*--------------------------------------------------------*/

    public interface CheckIfEmailExist{
        void onComplete(Boolean isSuccess);
    }

    public void checkIfEmailExist(String email, CheckIfEmailExist listener){
        modelServer.checkIfEmailExist(email, listener);

    }

    /******************************************************************************************/

    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/

    List<Profile> profiles = new LinkedList<>();

    public Profile getProfileById(String profileId) {
        for (Profile s:profiles) {
            if (s.getProfileId().equals(profileId)){
                return s;
            }
        }
        return null;
    }

    /*--------------------------------------------------------*/

    public interface GetProfileListener{
        void onComplete(Profile profile);
    }

    public void getProfileFromServer(String email, String userName,GetProfileListener listener) {
        modelServer.getProfileFromServer(email,userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface  CreateProfileListener{
        void onComplete(Boolean isSuccess);
    }

    public void createProfile(Profile profile, CreateProfileListener listener){
        modelServer.createProfile(profile, listener);

    }

    /*--------------------------------------------------------*/

    public interface CheckIfUserNameExist{
        void onComplete(Boolean isSuccess);
    }

    public void checkIfUserNameExist(String userName, CheckIfUserNameExist listener){
        modelServer.checkIfUserNameExist(userName, listener);

    }

    /*--------------------------------------------------------*/

    public interface EditProfile{
        void onComplete(Boolean isSuccess);
    }

    public void editProfile(String previousName, Profile profile, EditProfile listener){
        modelServer.editProfile(previousName, profile, listener);
    }

    public interface DeleteProfileListener{
        void onComplete(Boolean isSuccess);
    }

    public void deleteProfile(String userName, DeleteProfileListener listener){
        modelServer.deleteProfile(userName, listener);
    }

    /******************************************************************************************/

    /*--------------------------------- Post -------------------------------*/

    /******************************************************************************************/

    public interface GetAllPostsListener {
        void onComplete(List<Post> postList);
    }

    public void getAllPostsFromServer(GetAllPostsListener listener) {
        modelServer.getAllPosts(listener);
    }

    /*--------------------------------------------------------*/

    /******************************************************************************************/

    /*--------------------------------- Category -------------------------------*/

    /******************************************************************************************/

    public interface GetAllCategoriesListener {
        void onComplete(List<Category> categoryList);
    }

    public void getAllCategoriesListener(GetAllCategoriesListener listener) {
        modelServer.getAllCategoriesListener(listener);
    }

    /******************************************************************************************/

    /*--------------------------------- SubCategory -------------------------------*/

    /******************************************************************************************/

    public interface GetAllSubCategoriesListener {
        void onComplete(List<SubCategory> subCategoryList);
    }

    public void getAllSubCategories(GetAllSubCategoriesListener listener) {
        modelServer.getAllSubCategories(listener);
    }
}
