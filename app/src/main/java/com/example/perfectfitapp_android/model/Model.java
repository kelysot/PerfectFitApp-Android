package com.example.perfectfitapp_android.model;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.RetrofitInterface;
import com.example.perfectfitapp_android.model.ModelServer.CategoryModelServer;
import com.example.perfectfitapp_android.model.ModelServer.CommentModelServer;
import com.example.perfectfitapp_android.model.ModelServer.ImagesModelServer;
import com.example.perfectfitapp_android.model.ModelServer.NotificationModelServer;
import com.example.perfectfitapp_android.model.ModelServer.PostModelServer;
import com.example.perfectfitapp_android.model.ModelServer.ProfileModelServer;
import com.example.perfectfitapp_android.model.ModelServer.SubCategoryModelServer;
import com.example.perfectfitapp_android.model.ModelServer.UserModelServer;
import com.example.perfectfitapp_android.notification.NotificationCounter;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    //    String BASE_URL = "http://193.106.55.142:4000";
    String BASE_URL = "http://10.0.2.2:4000";
    List<Post> data = new LinkedList<Post>();
    List<Category> categories = new ArrayList<>();
    List<SubCategory> subCategories = new ArrayList<>();
    List<Notification> notifications = new ArrayList<>();
    List<Profile> profiles = new ArrayList<>();
    Map<String, ArrayList<String>> categoriesAndSubCategories = new HashMap<>();
    BottomNavigationView bottomNavigationView;
    Boolean flagBell = false;
    Boolean refreshFlag = false;

    public void setRefreshFlag(Boolean b) {
        this.refreshFlag = b;
    }

    public Boolean getRefreshFlag() {
        return this.refreshFlag;
    }


    Post post, newPost;
    String lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getString("PostsLastUpdateDate", "");

    /************************************  Model Server  ************************************/

    ImagesModelServer imagesModelServer = new ImagesModelServer();
    UserModelServer userModelServer = new UserModelServer();
    ProfileModelServer profileModelServer = new ProfileModelServer();
    PostModelServer postModelServer = new PostModelServer();
    CommentModelServer commentModelServer = new CommentModelServer();
    CategoryModelServer categoryModelServer = new CategoryModelServer();
    SubCategoryModelServer subCategoryModelServer = new SubCategoryModelServer();
    NotificationModelServer notificationModelServer = new NotificationModelServer();


    /************************************  LoadingState  ************************************/
    public enum PostListLoadingState {
        loading,
        loaded
    }

    public MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();

    public MutableLiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    private Model() {
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

    public List<Post> getAllPosts() {
        return data;
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public List<SubCategory> getAllSubCategories() {
        return subCategories;
    }

    public void addPost(Post post) {
        data.add(post);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
    }

    public Post getPostById(String postId) {
        for (Post s : data) {
            if (s.getPostId().equals(postId)) {
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

    public void deletePost(int pos) {
        data.remove(pos);
    }

    public Post getPost(int pos) {
        return data.get(pos);
    }


    public interface UploadImageListener {
        void onComplete(String mImageUrl);
    }

    public void uploadImage(Bitmap mBitmap, Context context, UploadImageListener listener) {
        imagesModelServer.uploadImage(mBitmap, context, listener);
    }

    public interface GetImagesListener {
        void onComplete(Bitmap bitmap);
    }

    public void getImages(String urlImage, GetImagesListener listener) {
        imagesModelServer.getImages(urlImage, listener);
    }

    /******************************************************************************************/

    public interface getDatesListener {
        void onComplete(Boolean isSuccess);
    }

    public void getDates(String date, getDatesListener listener) {
        postModelServer.getDatesFromServer(date, listener);
    }

    public interface TimeSinceListener {
        void onComplete(String timeAgo);
    }

    public void timeSince(String date, TimeSinceListener listener) {
        postModelServer.timeSince(date, listener);
    }

    /*--------------------------------- User -------------------------------*/

    /******************************************************************************************/


    public interface RegisterListener {
        void onComplete(User user);
    }

    public void register(String email, String password, RegisterListener listener) {
        userModelServer.register(email, password, user -> {
            executor.execute(() -> {
                AppLocalDb.db.userDao().insertUser(user);
                listener.onComplete(user);

            });
        });
    }

    /*--------------------------------------------------------*/

    public interface GetUserListener {
        void onComplete(User user);
    }

    public void getUserFromServer(String email, GetUserListener listener) {
        userModelServer.getUser(email, listener);
    }

    /*--------------------------------------------------------*/

    public interface LogInListener {
        void onComplete(User user);
    }

    public void logIn(String email, String password, LogInListener listener) {
        userModelServer.logIn(email, password, user -> {
            executor.execute(() -> {
                if (user != null) {
                    AppLocalDb.db.userDao().insertUser(user);
                }
                listener.onComplete(user);
            });
        });
    }


    /*--------------------------------------------------------*/

    public interface CheckIfEmailExist {
        void onComplete(Boolean isSuccess);
    }

    public void checkIfEmailExist(String email, CheckIfEmailExist listener) {
        userModelServer.checkIfEmailExist(email, listener);

    }

    /*--------------------------------------------------------*/

    public interface LogoutListener {
        void onComplete(Boolean isSuccess);
    }

    public void printUserFromAppLocalDB() {
        executor.execute(() -> {
            System.out.println("-------------- user in AppLocalDB ------------------------------- ");
            System.out.println(AppLocalDb.db.userDao().getUserRoom().getEmail());
        });
    }

    public void logout(LogoutListener listener) {
        userModelServer.logout(isSuccess -> {
            if (isSuccess) {
                listener.onComplete(true);
            } else {
                listener.onComplete(false);
            }
        });
    }

    public interface logoutFromAppLocalDBListener {
        void onComplete(Boolean isSuccess);
    }

    public void logoutFromAppLocalDB(logoutFromAppLocalDBListener listener) {
        executor.execute(() -> {
            User user = AppLocalDb.db.userDao().getUserRoom();
            if (user != null) {
                AppLocalDb.db.userDao().deleteByUserEmail(user.getEmail());
                listener.onComplete(true);
            }
        });
    }

    /*--------------------------------------------------------*/

    public Boolean isSignIn() {
        User user = AppLocalDb.db.userDao().getUserRoom();
        return user != null;
    }

    /*--------------------------------------------------------*/

    public interface AddToRoomListener {
        void onComplete(Boolean isSuccess);
    }

    public void addToRoom(User user, AddToRoomListener listener) {
        if (user != null) {
            executor.execute(() -> {
                AppLocalDb.db.userDao().insertUser(user);
                listener.onComplete(true);
            });
        } else
            listener.onComplete(false);
    }

    /*--------------------------------------------------------*/

    public interface RemoveFromRoomListener {
        void onComplete(Boolean isSuccess);
    }

    public void removeFromRoom(RemoveFromRoomListener listener) {
        executor.execute(() -> {
            User user = AppLocalDb.db.userDao().getUserRoom();
            AppLocalDb.db.userDao().deleteByUserEmail(user.getEmail());
            listener.onComplete(true);
        });
    }

    /*--------------------------------------------------------*/

    public interface GetUserFromRoomListener {
        void onComplete(User user);
    }

    public void getUserFromRoom(GetUserFromRoomListener listener) {
        executor.execute(() -> {
            User user = AppLocalDb.db.userDao().getUserRoom();
            listener.onComplete(user);
        });
    }

    /*--------------------------------------------------------*/

    public interface EditUserListener {
        void onComplete(User user);
    }

    public void editUser(String previousEmail, User user, EditUserListener listener) {
        userModelServer.editUser(previousEmail, user, listener);
    }

    /*--------------------------------------------------------*/

    public interface ResetPasswordListener {
        void onComplete(String code);
    }

    public void resetPassword(String email, ResetPasswordListener listener) {
        userModelServer.resetPassword(email, listener);
    }

    /*--------------------------------------------------------*/

    public interface ChangePasswordListener {
        void onComplete(Boolean isSuccess);
    }

    public void changePassword(User user, ChangePasswordListener listener) {
        userModelServer.changePassword(user, listener);

    }

    /******************************************************************************************/

    /*--------------------------------- General -------------------------------*/

    public interface generalListener {
        void onComplete(Boolean isSuccess);
    }

    public void general(generalListener listener) {
        userModelServer.general(listener);

    }
    /*--------------------------------- Profile -------------------------------*/

    /******************************************************************************************/


    public interface GetProfileListener {
        void onComplete(Profile profile);
    }

    public void getProfileFromServer(String email, String userName, GetProfileListener listener) {
        profileModelServer.getProfileFromServer(email, userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetProfileByUserName {
        void onComplete(Profile profile);
    }

    public void getProfileByUserName(String userName, GetProfileByUserName listener) {
        profileModelServer.getProfileByUserName(userName, listener);
    }
    /*--------------------------------------------------------*/

    public interface CreateProfileListener {
        void onComplete(Boolean isSuccess);
    }

    public void createProfile(Profile profile, CreateProfileListener listener) {
        profileModelServer.createProfile(profile, listener);

    }

    /*--------------------------------------------------------*/

    public interface CheckIfUserNameExist {
        void onComplete(Boolean isSuccess);
    }

    public void checkIfUserNameExist(String userName, CheckIfUserNameExist listener) {
        profileModelServer.checkIfUserNameExist(userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface EditProfile {
        void onComplete(Boolean isSuccess);
    }

    public void editProfile(String previousName, Profile profile, EditProfile listener) {
        profileModelServer.editProfile(previousName, profile, listener);
    }

    /*--------------------------------------------------------*/

    public interface DeleteProfileListener {
        void onComplete(Boolean isSuccess);
    }

    public void deleteProfile(String userName, DeleteProfileListener listener) {
        profileModelServer.deleteProfile(userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetProfilesByUserNamesListener {
        void onComplete(List<Profile> profilesList);
    }

    public void getProfilesByUserNames(List<String> userNames, GetProfilesByUserNamesListener listener) {
        profileModelServer.getProfilesByUserNames(userNames, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetAllProfileListener {
        void onComplete(List<Profile> profileList);
    }

    public void getAllProfile(GetAllProfileListener listener) {
        profileModelServer.getAllProfile(listener);
    }

    /******************************************************************************************/

    /*--------------------------------- Post -------------------------------*/

    /******************************************************************************************/

    public interface GetAllPostsListener {
        void onComplete(List<Post> postList);
    }


    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();

    public LiveData<List<Post>> getAll() {
        refreshPostsList();
        return postsList;
    }

    public void refreshPostsList() {
        postListLoadingState.setValue(PostListLoadingState.loading);

        // get last local update date

        // server - get all updates since lastLocalUpdateDate

        // need to send the last update date

        postModelServer.getSuitablePosts(Model.instance.getProfile().getUserName(), posts -> {
            if (posts != null) {
                executor.execute(() -> {

                    List<Post> finalList = new LinkedList<>();
                    // add all records to the local db

                    if (!posts.isEmpty()) {
                        String lud = posts.get(0).getDate();
                        // update last local update date

                        MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit()
                                .putString("PostsLastUpdateDate", lud).commit();
                    }
                    // return all data to caller

                    postsList.postValue(posts);
                    postListLoadingState.postValue(PostListLoadingState.loaded);
                });
            } else {
                System.out.println("---------------- wrong ------------------");
                postListLoadingState.postValue(PostListLoadingState.loaded);
            }
        });
    }

    /*--------------------------------------------------------*/

    public interface AddNewPostListener {
        void onComplete(Post post);
    }

    public void addNewPost(Post post, AddNewPostListener listener) {
        postModelServer.addNewPost(post, listener);
    }

    /*--------------------------------------------------------*/

    public interface getWishListListener {
        void onComplete(List<Post> list);
    }

    public void getWishListFromServer(getWishListListener listener) {
        postModelServer.getWishList(listener);
    }

    /*--------------------------------------------------------*/

    public interface deletePostFromServerListener {
        void onComplete(Boolean isSuccess);
    }

    public void deletePostFromServer(String postId, deletePostFromServerListener listener) {
        postModelServer.deletePost(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface editPostListener {
        void onComplete(Boolean isSuccess);
    }

    public void editPost(Post post, editPostListener listener) {
        postModelServer.editPost(post, listener);
    }

    /*--------------------------------------------------------*/

    public interface getProfilePostsListener {
        void onComplete(List<Post> list);
    }

    public void getProfilePosts(String userName, getProfilePostsListener listener) {
        postModelServer.getProfilePosts(userName, listener);
    }

    /*--------------------------------------------------------*/

    public interface getPostByIdListener {
        void onComplete(Post post);
    }

    public void getPostById(String postId, getPostByIdListener listener) {
        postModelServer.getPostById(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetPostsBySubCategoryIdListener {
        void onComplete(List<Post> post);
    }

    public void getPostsBySubCategoryId(String subCategoryId, GetPostsBySubCategoryIdListener listener) {
        postModelServer.getPostsBySubCategoryId(subCategoryId, listener);
    }

    /*--------------------------------------------------------*/

    public interface getSuitablePostsListener {
        void onComplete(List<Post> post);
    }

    public void getSuitablePosts(String profileId, getSuitablePostsListener listener) {
        postModelServer.getSuitablePosts(profileId, listener);
    }

    /*--------------------------------------------------------*/

    public interface getSearchPostsListener {
        void onComplete(List<Post> posts);
    }

    public void getSearchPosts(HashMap<String, List<String>> map, getSearchPostsListener listener) {
        postModelServer.getSearchPosts(map, listener);
    }

    /*--------------------------------------------------------*/

    public interface getGeneralListener {
        void onComplete(HashMap<String, List<String>> map);
    }

    public void getGeneral(getGeneralListener listener) {
        postModelServer.getGeneral(listener);
    }


    /*--------------------------------------------------------*/

    public interface GetPostsByIdsListener {
        void onComplete(List<Post> postList);
    }

    public void getPostsByIds(List<String> postsId, GetPostsByIdsListener listener) {
        postModelServer.getPostsByIds(postsId, listener);
    }

    /******************************************************************************************/

    /*--------------------------------- Category -------------------------------*/

    /******************************************************************************************/

    public interface GetAllCategoriesListener {
        void onComplete(List<Category> categoryList);
    }

    public void getAllCategoriesListener(GetAllCategoriesListener listener) {
        categoryModelServer.getAllCategoriesListener(listener);
    }

    /******************************************************************************************/

    /*--------------------------------- SubCategory -------------------------------*/

    /******************************************************************************************/

    public interface GetAllSubCategoriesListener {
        void onComplete(List<SubCategory> subCategoryList);
    }

    public void getAllSubCategories(GetAllSubCategoriesListener listener) {
        subCategoryModelServer.getAllSubCategories(listener);
    }

    public interface GetSubCategoriesByCategoryIdListener {
        void onComplete(List<SubCategory> subCategoryList);
    }

    public void getSubCategoriesByCategoryId(String categoryId, String gender, GetSubCategoriesByCategoryIdListener listener) {
        subCategoryModelServer.getSubCategoriesByCategoryId(categoryId, gender, listener);
    }

    public interface GetSubCategoryById {
        void onComplete(SubCategory subCategory);
    }

    public void getSubCategoryById(String subCategoryId, GetSubCategoryById listener) {
        subCategoryModelServer.getSubCategoryById(subCategoryId, listener);
    }


    /******************************************************************************************/

    /*--------------------------------- Comment -------------------------------*/

    /******************************************************************************************/

    public interface GetCommentsByPostIdListener {
        void onComplete(List<Comment> comments);
    }

    public void getCommentsByPostId(String postId, GetCommentsByPostIdListener listener) {
        commentModelServer.getCommentsByPostId(postId, listener);
    }

    /*--------------------------------------------------------*/

    public interface AddNewCommentListener {
        void onComplete(Comment comment);
    }

    public void addNewComment(Comment comment, AddNewCommentListener listener) {
        commentModelServer.addNewComment(comment, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetCommentByIdListener {
        void onComplete(Comment comment);
    }

    public void getCommentById(String commentId, GetCommentByIdListener listener) {
        commentModelServer.getCommentById(commentId, listener);
    }


    /******************************************************************************************/

    /*--------------------------------- Notification -------------------------------*/

    /******************************************************************************************/

    public interface GetNotificationsByIdsListener {
        void onComplete(List<Notification> notificationsList);
    }

    public void getNotificationsByIds(List<String> notificationsIds, GetNotificationsByIdsListener listener) {
        notificationModelServer.getNotificationsByIds(notificationsIds, listener);
    }

    /*--------------------------------------------------------*/

    public interface AddNewNotificationListener {
        void onComplete(Notification notification);
    }

    public void addNewNotification(Notification notification, AddNewNotificationListener listener) {
        notificationModelServer.addNewNotification(notification, listener);
    }

    /*--------------------------------------------------------*/

    public interface GetAllNotificationListener {
        void onComplete(List<Notification> notificationList);
    }

    public void getAllNotification(GetAllNotificationListener listener) {
        notificationModelServer.getAllNotification(listener);
    }

    /*--------------------------------------------------------*/

    public interface GetNotificationByIdListener {
        void onComplete(Notification notification);
    }

    public void getNotificationById(String subCategoryId, GetNotificationByIdListener listener) {
        notificationModelServer.getNotificationById(subCategoryId, listener);
    }

    /*--------------------------------------------------------*/

    public interface EditNotificationListener {
        void onComplete(Boolean isSuccess);
    }

    public void editNotification(Notification notification, EditNotificationListener listener) {
        notificationModelServer.editNotification(notification, listener);
    }

    /*--------------------------------------------------------*/

    public void removeBadge() {
        flagBell = false;
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) Model.instance.getBottomNavigationView().getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        itemView.removeViewAt(itemView.getChildCount() - 1);
    }

    public void addBadge(int count) {
        flagBell = true;
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) Model.instance.getBottomNavigationView().getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(MyApplication.getContext())
                .inflate(R.layout.bell, itemView, true);

        NotificationCounter notificationCounter = new NotificationCounter(badge);
        notificationCounter.setNotificationNumber(count);

    }

    public void checkNotification() {
        //Check if the user have notifications
        count = 0;
        Model.instance.getProfileFromServer(getUser().getEmail(), getProfile().getUserName(), profile -> {
            if (profile != null) {
                List<String> notifications = profile.getNotifications();
                if (!notifications.isEmpty()) {
                    Model.instance.getNotificationsByIds(notifications, notificationsList -> {
                        if (notificationsList != null) {
                            for (int i = 0; i < notificationsList.size(); i++) {
                                if (notificationsList.get(i).getSeen().equals("false")) {
                                    count++;
                                }
                            }
                            if (count != 0) {
                                if (flagBell)
                                    removeBadge();
                                Model.instance.addBadge(count);
                                flagBell = true;
                            }
                        }
                    });
                }
            }
        });
    }

    /******************************************************************************************/

    /*--------------------------------- RefreshToken -------------------------------*/

    /******************************************************************************************/

    public interface refreshTokenListener {
        void onComplete(List<String> tokensList);
    }

    public void refreshToken(Model.refreshTokenListener listener) {
        userModelServer.refreshToken(listener);
    }

    public void insertTokens(List<String> tokensList) {
        String aToken = "Bearer " + tokensList.get(0);
        String rToken = "Bearer " + tokensList.get(1);

        SharedPreferences.Editor preferences = MyApplication.getContext().getSharedPreferences("TAG", ContextThemeWrapper.MODE_PRIVATE).edit();
        preferences.putString("ACCESS_TOKEN", aToken);
        preferences.putString("REFRESH_TOKEN", rToken);
        preferences.commit();
    }

    public void showOkDialog(String str) {
        Dialog dialog = new Dialog(MyApplication.getContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(str);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}
