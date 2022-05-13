package com.example.perfectfitapp_android.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;


public class ProfileFragment extends Fragment {

    ProfileViewModel viewModel;
    ImageView userPic;
    TextView userNameTv;
    TextView numOfPosts, numOfFollowers, numOfFollowing;
//    ImageButton editProfileBtn;
    MyAdapter adapter;
    String profileId;
    Button followBtn;
    int followersSize = 0;


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
        userNameTv = view.findViewById(R.id.profile_user_name);
        numOfPosts = view.findViewById(R.id.profile_num_posts_tv);

        numOfFollowers = view.findViewById(R.id.profile_num_followers_tv);
        numOfFollowing = view.findViewById(R.id.profile_num_following_tv);
        followBtn = view.findViewById(R.id.profile_follow_btn);

//        if(followingSize > 0){
//            numOfFollowing.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO:fffff
//                }
//            });
//        }


        if(!getArguments().isEmpty()){
            profileId = ProfileFragmentArgs.fromBundle(getArguments()).getProfileId();
            Model.instance.getProfileByUserName(profileId, profile -> {
                userNameTv.setText(profile.getUserName());
                numOfPosts.setText(String.valueOf(profile.getMyPostsListId().size()));
                String userImg = profile.getUserImageUrl();
                if(userImg != null && !userImg.equals("")){
                    Model.instance.getImages(userImg, bitmap -> {
                        userPic.setImageBitmap(bitmap);
                    });
                }

                followersSize = profile.getFollowers().size();
                numOfFollowing.setText(String.valueOf(profile.getTrackers().size()));
                numOfFollowers.setText(String.valueOf(profile.getFollowers().size()));

                String currentUserName = Model.instance.getProfile().getUserName();
                if(!profile.getUserName().equals(currentUserName)){ //Check if the user go to his profile by click on his name or picture.
                    followBtn.setVisibility(View.VISIBLE);
                    if(profile.getTrackers().contains(currentUserName))
                        followBtn.setText("Following");
                    else
                        followBtn.setText("Follow");

                    followBtn.setOnClickListener(v -> checkIfFollow(profileId, currentUserName));
                }
                else
                    followBtn.setVisibility(View.GONE);




//                    if(followersSize > 0){
//                        numOfFollowers.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
//                    }
            });
        }
        else {
            Profile profile = Model.instance.getProfile();
            profileId = profile.getUserName();
            userNameTv.setText(profile.getUserName());
            numOfPosts.setText(String.valueOf(profile.getMyPostsListId().size()));
            String userImg = profile.getUserImageUrl();
            if(userImg != null && !userImg.equals("")){
                Model.instance.getImages(userImg, bitmap -> {
                    userPic.setImageBitmap(bitmap);
                });
            }
            numOfFollowing.setText(String.valueOf(profile.getTrackers().size()));
            numOfFollowers.setText(String.valueOf(profile.getFollowers().size()));
            followBtn.setVisibility(View.GONE);
        }


//        editProfileBtn = view.findViewById(R.id.profile_edit_profile_btn);
//        editProfileBtn.setOnClickListener(v-> editProfile(view));


        RecyclerView postsList = view.findViewById(R.id.profile_user_posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));

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

        refresh();

        return view;
    }

    private void refresh() {
        Model.instance.getProfileByUserName(profileId, profile -> Model.instance.getProfilePosts(profile.getUserName(), postList -> {
            viewModel.setData(postList);
            adapter.notifyDataSetChanged();
        }));
    }

    //If current profile follow clicked profile.
    private void checkIfFollow(String profileId, String currentUserName) {
        Model.instance.getProfileByUserName(profileId, profile -> {
            if(profile.getFollowers().contains(currentUserName)){
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
                    } else
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later11",
                                Toast.LENGTH_LONG).show();
                });
            } else
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
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later11",
                                Toast.LENGTH_LONG).show();
                });
            } else
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
        });

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv, descriptionTv,categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ShapeableImageView postPic, userPic;
        ImageButton addToWishList, commentsBtn, addToLikes;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //TODO: change the productName to userName - by the profileID in the mongo
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
            holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));
            holder.addToLikes.setOnClickListener(v-> addToLikes(holder, post));
            Model.instance.timeSince(post.getDate(), timeAgo -> holder.timeAgoTv.setText(timeAgo));

            Model.instance.getProfileByUserName(post.getProfileId(), new Model.GetProfileByUserName() {
                @Override
                public void onComplete(Profile profile) {
                    String userImg = profile.getUserImageUrl();
                    if(userImg != null && !userImg.equals("")){
                        Model.instance.getImages(userImg, bitmap -> {
                            holder.userPic.setImageBitmap(bitmap);
                        });
                    }
                    else {
                        Picasso.get()
                                .load(R.drawable.avatar).resize(250, 180)
                                .centerCrop()
                                .into(holder.userPic);
                    }
                }
            });


            if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0 ) {
                Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                    holder.postPic.setImageBitmap(bitmap);
                });
            }
            else {
                Picasso.get()
                        .load(R.drawable.pic1_shirts).resize(250, 180)
                        .centerCrop()
                        .into(holder.postPic);
            }

            if(checkIfInsideWishList(post)){
                holder.addToWishList.setImageResource(R.drawable.ic_full_star);
            }
            else{
                holder.addToWishList.setImageResource(R.drawable.ic_star);
            }

            if(checkIfInsideLikes(post)){
                holder.addToLikes.setImageResource(R.drawable.ic_red_heart);
            }
            else{
                holder.addToLikes.setImageResource(R.drawable.ic_heart);
            }

            if(post.getLikes().size() != 0){
                holder.likesNumberTV.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToLikesFragment(post.getPostId()));
                });
            }
            else {
                holder.likesNumberTV.setOnClickListener(v -> {}); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener((v) -> {
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToCommentFragment(post.getPostId()));
            });
        }

        private void addToLikes(MyViewHolder holder, Post post) {
            String userName = Model.instance.getProfile().getUserName();
            if(checkIfInsideLikes(post)){
                post.getLikes().remove(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if(isSuccess){
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_heart);
                        refresh();
                    }
                    else {
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
            else{
                post.getLikes().add(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if(isSuccess){
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_red_heart);
                        refresh();
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });

                if(!Model.instance.getProfile().getUserName().equals(post.getProfileId())){
                    Notification notification =  new Notification("0", Model.instance.getProfile().getUserName(),
                            post.getProfileId(), Model.instance.getProfile().getUserName() + " liked your post", "10/5/22", post.getPostId(), "false");
                    Model.instance.addNewNotification(notification, notification1 -> {});
                }
            }
        }

        private void addToWishList(MyViewHolder holder, Post post) {

            if(checkIfInsideWishList(post)){
                Model.instance.getProfile().getWishlist().remove(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        holder.addToWishList.setImageResource(R.drawable.ic_star);
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                Model.instance.getProfile().getWishlist().add(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        holder.addToWishList.setImageResource(R.drawable.ic_full_star);
                        System.out.println("the posts added to the list");
                        System.out.println(Model.instance.getProfile().getWishlist());
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData() == null){
                return 0;
            }
            return viewModel.getData().size();
        }

        public boolean checkIfInsideWishList(Post post){
            return Model.instance.getProfile().getWishlist().contains(post.getPostId());
        }

        public boolean checkIfInsideLikes(Post post){
            return post.getLikes().contains(Model.instance.getProfile().getUserName());
        }
    }


//    private void editProfile(View view) {
//        Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());
//    }


}