package com.example.perfectfitapp_android.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class ProfileFragment extends Fragment {

    ProfileViewModel viewModel;
    ImageView userPic, bigPic;
    TextView userNameTv;
    TextView numOfPosts, numOfFollowers, numOfFollowing;
    //    ImageButton editProfileBtn;
    MyAdapter adapter;
    String profileId;
    Button followBtn;
    int followersSize = 0;
    SwipeRefreshLayout swipeRefresh;
    View followersView, followingView;
    int likesSize = 0;
    LottieAnimationView noPostImg;
    TextView noPostTv;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userPic = view.findViewById(R.id.profile_profile_img);
        bigPic = view.findViewById(R.id.profile_big_img);
        userNameTv = view.findViewById(R.id.profile_user_name);
        numOfPosts = view.findViewById(R.id.profile_num_posts_tv);

        numOfFollowers = view.findViewById(R.id.profile_num_followers_tv);
        numOfFollowing = view.findViewById(R.id.profile_num_following_tv);
        followBtn = view.findViewById(R.id.profile_follow_btn);
        followersView = view.findViewById(R.id.profile_followers_view);
        followingView = view.findViewById(R.id.profile_following_view);

        noPostImg = view.findViewById(R.id.profile_no_post_img);
        noPostTv = view.findViewById(R.id.profile_no_post_tv);
        noPostImg.setVisibility(View.GONE);
        noPostTv.setVisibility(View.GONE);


        swipeRefresh = view.findViewById(R.id.profile_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refresh());


        if (!getArguments().isEmpty()) {
            profileId = ProfileFragmentArgs.fromBundle(getArguments()).getProfileId();
            Model.instance.getProfileByUserName(profileId, profile -> {

                if(profile != null){

                    userNameTv.setText(profile.getUserName());
                    setNumOfPosts(profile.getMyPostsListId());

                    String userImg = profile.getUserImageUrl();
                    if (userImg != null && !userImg.equals("")) {
                        Model.instance.getImages(userImg, bitmap -> {
                            userPic.setImageBitmap(bitmap);
                        });
                    }
                    String userBigImg = profile.getBigPictureUrl();
                    if (userBigImg != null && !userBigImg.equals("")) {
                        Model.instance.getImages(userBigImg, bitmap1 -> {
                            bigPic.setImageBitmap(bitmap1);
                        });
                    }

                    setNumOfFollowers(profile);

                    followersView.setOnClickListener(v -> moveToFollowersList(v));
                    followingView.setOnClickListener(v -> moveToTrackersList(v));

                    String currentUserName = Model.instance.getProfile().getUserName();
                    if (!profile.getUserName().equals(currentUserName)) { //Check if the user go to his profile by click on his name or picture.
                        followBtn.setVisibility(View.VISIBLE);
                        if (profile.getTrackers().contains(currentUserName))
                            followBtn.setText("Following");
                        else
                            followBtn.setText("Follow");

                        followBtn.setOnClickListener(v -> checkIfFollow(profileId, currentUserName));
                    } else
                        followBtn.setVisibility(View.GONE);

                }
                else{
                    //TODO: dialog
                }
            });
        } else {
            Profile profile = Model.instance.getProfile();
            profileId = profile.getUserName();
            userNameTv.setText(profile.getUserName());
            setNumOfPosts(profile.getMyPostsListId());
            String userImg = profile.getUserImageUrl();
            if (userImg != null && !userImg.equals("")) {
                Model.instance.getImages(userImg, bitmap -> {
                    userPic.setImageBitmap(bitmap);
                });
            }
            String userBigImg = profile.getBigPictureUrl();
            if (userBigImg != null && !userBigImg.equals("")) {
                Model.instance.getImages(userBigImg, bitmap1 -> {
                    bigPic.setImageBitmap(bitmap1);
                });
            }

            setNumOfFollowers(profile);

            followingView.setOnClickListener(v -> moveToTrackersList(v));
            followersView.setOnClickListener(v -> moveToFollowersList(v));

            followBtn.setVisibility(View.GONE);
        }


//        editProfileBtn = view.findViewById(R.id.profile_edit_profile_btn);
//        editProfileBtn.setOnClickListener(v-> editProfile(view));


        RecyclerView postsList = view.findViewById(R.id.profile_user_posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setNestedScrollingEnabled(true);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));
        //postsList.setNestedScrollingEnabled(false);

        adapter = new MyAdapter();
        postsList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            //TODO: bring the post from appLocalDB
            Model.instance.getPostById(postId, post -> {
                Model.instance.setPost(post);
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionGlobalPostPageFragment(postId, "profile"));
            });
        });

        setHasOptionsMenu(true);
        Model.instance.checkNotification();
        refresh();

        return view;
    }

    private void setNumOfFollowers(Profile profile1){
        Model.instance.getProfilesByUserNames(profile1.getFollowers(), profilesList -> {
            List<Profile> profiles = profilesList;

            for(int i = 0; i < profilesList.size(); i ++){
                if(profilesList.get(i).getIsDeleted().equals("true")){
                    profiles.remove(profilesList.get(i));
                }
            }
            followersSize = profiles.size();
            numOfFollowers.setText(String.valueOf(profiles.size()));
            setNumOfFollowing(profile1);
        });
    }

    private void setNumOfFollowing(Profile profile1){
        Model.instance.getProfilesByUserNames(profile1.getTrackers(), profilesList -> {
            List<Profile> profiles = profilesList;
            for(int i = 0; i < profilesList.size(); i ++){
                if(profilesList.get(i).getIsDeleted().equals("true")){
                    profiles.remove(profilesList.get(i));
                }
            }
            numOfFollowing.setText(String.valueOf(profiles.size()));
        });

    }

    private void setNumOfPosts(ArrayList<String> myPostsListId) {
        if(myPostsListId.size() != 0){
            Model.instance.getPostsByIds(myPostsListId, postList -> {
                for(int i = 0; i < postList.size(); i++){
                    if(postList.get(i).getIsDeleted().equals("true")){
                        postList.remove(postList.get(i));
                    }
                }
                numOfPosts.setText(String.valueOf(postList.size()));
            });
        }
        else {
            noPostImg.setVisibility(View.VISIBLE);
            noPostTv.setVisibility(View.VISIBLE);
            numOfPosts.setText("0");
        }


    }

    private void refresh() {
        //        swipeRefresh.setRefreshing(true);
        Model.instance.getProfileByUserName(profileId, profile -> {
            if (profile != null) {
                Model.instance.getProfilePosts(profile.getUserName(), postList -> {
                    if (postList != null) {
                        swipeRefresh.setRefreshing(false);
                        viewModel.setData(postList);
                        adapter.notifyDataSetChanged();
                    } else {
                        swipeRefresh.setRefreshing(false);
                        //TODO: create a popup
                    }
                });
            } else {
                swipeRefresh.setRefreshing(false);
                //TODO: create a popup
            }
        });
    }


    //If current profile follow clicked profile.
    private void checkIfFollow(String profileId, String currentUserName) {
        Model.instance.getProfileByUserName(profileId, profile -> {
            if (profile.getFollowers().contains(currentUserName)) {
                removeFollower(profile, currentUserName);

            } else {  //If current profile not following clicked profile.
                addFollower(profile, currentUserName);
            }
        });

    }


    private void addFollower(Profile profile, String currentUserName) {
        followBtn.setText("Follow");
        profile.getFollowers().add(currentUserName);
        Model.instance.editProfile(null, profile, isSuccess -> { // Change the profile we inside followers list.
            if (isSuccess) {
                Model.instance.getProfile().getTrackers().add(profile.getUserName());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess1 -> { // Change my profile trackers list.
                    if (isSuccess1) {
                        followBtn.setText("Following");
                        followersSize++;
                        numOfFollowers.setText(String.valueOf(followersSize));
                        Notification notification =  new Notification("0", currentUserName,
                                profile.getUserName(), currentUserName + " started following you.", "", " ", "false");
                        Model.instance.addNewNotification(notification, notification1 -> {});
                    } else
                        //TODO: dialog
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later11",
                                Toast.LENGTH_LONG).show();
                });
            } else
                //TODO: dialog
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later11",
                        Toast.LENGTH_LONG).show();
        });

    }

    private void removeFollower(Profile profile, String currentUserName) {
        followBtn.setText("Following");
        profile.getFollowers().remove(currentUserName);
        Model.instance.editProfile(null, profile, isSuccess -> { // Change the profile we inside followers list.
            if (isSuccess) {
                Model.instance.getProfile().getTrackers().remove(profile.getUserName());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess1 -> { // Change my profile trackers list.
                    if (isSuccess1) {
                        followBtn.setText("Follow");
                        followersSize--;
                        numOfFollowers.setText(String.valueOf(followersSize));
                    } else
                        //TODO: dialog
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later11",
                                Toast.LENGTH_LONG).show();
                });
            } else
                //TODO: dialog
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
        });

    }

    private void moveToFollowersList(View v) {
        Model.instance.getProfileByUserName(profileId, profile -> {
            if (profile.getFollowers().size() > 0) {
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(profileId));
            }
        });
    }

    private void moveToTrackersList(View v) {
        Model.instance.getProfileByUserName(profileId, profile -> {
            if (profile.getFollowers().size() > 0) {
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToTrackersFragment(profileId));
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTv, categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ShapeableImageView postPic, userPic;
        ImageButton addToWishList, commentsBtn, addToLikes;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            likesNumberTV = itemView.findViewById(R.id.listrow_post_likes_number);
            addToLikes = itemView.findViewById(R.id.listrow_post_likes_btn);
            timeAgoTv = itemView.findViewById(R.id.listrow_time_ago_tv);

            addToWishList = itemView.findViewById(R.id.add_to_wish_list_btn);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));
            holder.addToLikes.setOnClickListener(v -> addToLikes(holder, post));
            Model.instance.timeSince(post.getDate(), timeAgo -> holder.timeAgoTv.setText(timeAgo));

            Model.instance.getProfilesByUserNames(post.getLikes(), profilesList -> {
                likesSize = 0;
                for(int i = 0; i< profilesList.size(); i++){
                    if(profilesList.get(i).getIsDeleted().equals("false")){
                        likesSize++;
                    }
                }
                holder.likesNumberTV.setText(String.valueOf(likesSize) + " likes");
            });

            Model.instance.getProfileByUserName(post.getProfileId(), profile -> {
                if(profile!= null){
                    String userImg = profile.getUserImageUrl();
                    if (userImg != null && !userImg.equals("")) {
                        Model.instance.getImages(userImg, bitmap -> {
                            holder.userPic.setImageBitmap(bitmap);
                        });
                    } else {
                        Picasso.get()
                                .load(R.drawable.user_default).resize(250, 180)
                                .centerCrop()
                                .into(holder.userPic);
                    }
                }
                else{
                    errorDialog(getResources().getString(R.string.connectionError));
//                    Model.instance.refreshToken(tokensList -> {
//                        refresh();
//                    });
                }
            });


            if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0) {
                Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                    holder.postPic.setImageBitmap(bitmap);
                });
            } else {
                Picasso.get()
                        .load(R.drawable.ic_image).resize(250, 180)
                        .centerCrop()
                        .into(holder.postPic);
            }

            if (checkIfInsideWishList(post)) {
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
            } else {
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
            }

            if (checkIfInsideLikes(post)) {
                holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
            } else {
                holder.addToLikes.setImageResource(R.drawable.ic_heart1);
            }

            if (post.getLikes().size() != 0) {
                holder.likesNumberTV.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToLikesFragment(post.getPostId()));
                });
            } else {
                holder.likesNumberTV.setOnClickListener(v -> {
                }); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener((v) -> {
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToCommentFragment(post.getPostId()));
            });
        }

        private void addToLikes(MyViewHolder holder, Post post) {
            String userName = Model.instance.getProfile().getUserName();
            if (checkIfInsideLikes(post)) {
                post.getLikes().remove(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if (isSuccess) {
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_heart1);
                        refresh();
                    } else {
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                post.getLikes().add(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if (isSuccess) {
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
                        refresh();
                    } else {
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });

                if (!Model.instance.getProfile().getUserName().equals(post.getProfileId())) {
                    Notification notification = new Notification("0", Model.instance.getProfile().getUserName(),
                            post.getProfileId(), Model.instance.getProfile().getUserName() + " liked your post.", "10/5/22", post.getPostId(), "false");
                    Model.instance.addNewNotification(notification, notification1 -> {
                    });
                }
            }
        }

        private void addToWishList(MyViewHolder holder, Post post) {

            if (checkIfInsideWishList(post)) {
                Model.instance.getProfile().getWishlist().remove(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if (isSuccess) {
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
                    } else {
                        //TODO: dialog
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Model.instance.getProfile().getWishlist().add(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if (isSuccess) {
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
                        System.out.println("the posts added to the list");
                        System.out.println(Model.instance.getProfile().getWishlist());
                    } else {
                        //TODO: dialog
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData() == null) {
                return 0;
            }
            return viewModel.getData().size();
        }

        public boolean checkIfInsideWishList(Post post) {
            return Model.instance.getProfile().getWishlist().contains(post.getPostId());
        }

        public boolean checkIfInsideLikes(Post post) {
            return post.getLikes().contains(Model.instance.getProfile().getUserName());
        }
    }


//    private void editProfile(View view) {
//        Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());
//    }

    public void errorDialog(String str){

        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(str);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setText("OK");

        btnNo.setOnClickListener(v -> {
            btnNo.setEnabled(false);
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });
        //TODO: set the buttons to be enable false
        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setVisibility(View.GONE);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setVisibility(View.GONE);
        dialog.show();
    }

    /* *************************************** Menu Functions *************************************** */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_edit_menuItem) {
            NavHostFragment.findNavController(this).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());
            return true;
        }
        else if(item.getItemId() == R.id.profile_delete_menuItem){
            String s = "Are you sure you want to delete your profile?";
            showDialog(s);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void delete() {
        Model.instance.deleteProfile(Model.instance.getProfile().getUserName(),isSuccess -> {
            if(isSuccess){
                ArrayList<String> profileArr = Model.instance.getUser().getProfilesArray();
                for(int i = 0; i< profileArr.size(); i ++){
                    if(profileArr.get(i).equals(Model.instance.getProfile().getUserName())){
                        Model.instance.getUser().getProfilesArray().remove(i); //current user
                        startActivity(new Intent(getContext(), UserProfilesActivity.class));
                        getActivity().finish();
                        break;
                    }
                }
            }else{
                //TODO: dialog
                Log.d("TAG","not work");
            }
        });
    }

    private void showDialog(String text){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            delete();
            dialog.dismiss();
        });

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}