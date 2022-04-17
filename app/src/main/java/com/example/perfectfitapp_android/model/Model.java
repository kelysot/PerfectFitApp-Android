package com.example.perfectfitapp_android.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {

    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    int count = 0;
    User user;
    Profile profile;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "http://10.0.2.2:4000";
    List<Post> data = new LinkedList<Post>();
    List<Category> categories = new ArrayList<>();
    List<SubCategory> subCategories = new ArrayList<>();
    Map<String, ArrayList<String>> categoriesAndSubCategories = new HashMap<>();
    ModelServer modelServer = new ModelServer();
    Post post, newPost;


    /************************************  LoadingState  ************************************/
    enum PostListLoadingState{
        loading,
        loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    public void setPostListLoadingState(MutableLiveData<PostListLoadingState> postListLoadingState) {
        this.postListLoadingState = postListLoadingState;
    }

    /***************************************************************************************/


    public Post getNewPost() {
        return newPost;
    }

    public void setNewPost(Post newPost) {
        this.newPost = newPost;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Map<String, ArrayList<String>> getCategoriesAndSubCategories() {
        return categoriesAndSubCategories;
    }

    public void setCategoriesAndSubCategories(Map<String, ArrayList<String>> categoriesAndSubCategories) {
        this.categoriesAndSubCategories = categoriesAndSubCategories;
    }

    public void putCategoriesAndSubCategories(String category, ArrayList<String> subCategories) {
        this.categoriesAndSubCategories.put(category, subCategories);
    }

    public Model(){
        this.count = 0;
        user = new User();
        profile = new Profile();
        post = new Post();
        newPost = new Post();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        postListLoadingState.setValue(PostListLoadingState.loaded);
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
        for (Post s:data) {
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

//        int index = data.indexOf(post);
//        data.remove(index);
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

    /*--------------------------------------------------------*/

    public interface LogoutListener{
        void onComplete(Boolean isSuccess);
    }

    public void logout(LogoutListener listener){
        modelServer.logout(listener);
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

//    public void getAllPostsFromServer(GetAllPostsListener listener) {
//        modelServer.getAllPosts(listener);
//    }

    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    public LiveData<List<Post>> getAll(){
        refreshPostsList();
//        if(postsList.getValue() == null){
//            refreshPostsList();
//        }
        return  postsList;
    }

    public void refreshPostsList(){
        postListLoadingState.setValue(PostListLoadingState.loading);

        modelServer.getAllPosts(postList -> {
            postsList.setValue(postList);
            postListLoadingState.setValue(PostListLoadingState.loaded);

        });
    }

    /*--------------------------------------------------------*/

    public interface AddNewPostListener {
        void onComplete(Post post);
    }

    public void addNewPost(Post post, AddNewPostListener listener) {
        modelServer.addNewPost(post, listener);
    }

    /*--------------------------------------------------------*/

    public interface getWishListListener {
        void onComplete(List<Post> list);
    }

    public void getWishListFromServer(getWishListListener listener) {
        modelServer.getWishList(listener);
    }

    /*--------------------------------------------------------*/

    public interface deletePostFromServerListener{
        void onComplete(Boolean isSuccess);
    }

    public void deletePostFromServer(String postId, deletePostFromServerListener listener){
        modelServer.deletePost(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface editPostListener{
        void onComplete(Boolean isSuccess);
    }

    public void editPost(Post post, editPostListener listener){
        modelServer.editPost(post, listener);
    }

    /*--------------------------------------------------------*/

    public interface getProfilePostsListener{
        void onComplete(List<Post> list);
    }

    public void getProfilePosts(String userName, getProfilePostsListener listener){
        modelServer.getProfilePosts(userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface getPostByIdListener{
        void onComplete(Post post);
    }

    public void getPostById(String postId,getPostByIdListener listener ){
        modelServer.getPostById(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetPostsBySubCategoryIdListener{
        void onComplete(List<Post> post);
    }

    public void getPostsBySubCategoryId(String subCategoryId,GetPostsBySubCategoryIdListener listener ){
        modelServer.getPostsBySubCategoryId(subCategoryId, listener);
    }

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

    public interface GetSubCategoriesByCategoryIdListener {
        void onComplete(List<SubCategory> subCategoryList);
    }

    public void getSubCategoriesByCategoryId(String categoryId,String gender,GetSubCategoriesByCategoryIdListener listener){
        modelServer.getSubCategoriesByCategoryId(categoryId,gender,listener);
    }

    public interface GetSubCategoryById {
        void onComplete(SubCategory subCategory);
    }

    public void getSubCategoryById(String subCategoryId ,GetSubCategoryById listener){
        modelServer.getSubCategoryById(subCategoryId, listener);
    }


    /******************************************************************************************/

    /*--------------------------------- Comment -------------------------------*/

    /******************************************************************************************/

    public interface GetCommentsByPostIdListener {
        void onComplete(List<Comment> comments);
    }

    public void getCommentsByPostId(String postId ,GetCommentsByPostIdListener listener) {
        modelServer.getCommentsByPostId(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface AddNewCommentListener {
        void onComplete(Comment comment);
    }

    public void addNewComment(Comment comment, AddNewCommentListener listener) {
        modelServer.addNewComment(comment, listener);
    }

}
