package com.example.perfectfitapp_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.post.AddNewPostFragmentDirections;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageFragment extends Fragment {

    HomePageViewModel viewModel;
    MyAdapter adapter;
    TextView userName;
    SwipeRefreshLayout swipeRefresh;
    Button checkDate;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        userName = view.findViewById(R.id.home_page_name_tv);
        userName.setText(Model.instance.getProfile().getUserName());

        swipeRefresh = view.findViewById(R.id.postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshPostsList());

        RecyclerView postsList = view.findViewById(R.id.postlist_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        postsList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().getValue().get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            //TODO: bring the post from appLocalDB
            Model.instance.getPostById(postId, post -> {
                Model.instance.setPost(post);
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToPostPageFragment2(postId, "home"));
            });
        });

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), posts -> { refresh(); });
        swipeRefresh.setRefreshing(Model.instance.getPostListLoadingState().getValue() == Model.PostListLoadingState.loading);
        Model.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {

            if(postListLoadingState == Model.PostListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }
            else{
                swipeRefresh.setRefreshing(false);
            }
        });


        checkDate = view.findViewById(R.id.check_date);
        checkDate.setOnClickListener(v -> {
            Model.instance.getDates("Sun Apr 17 2022 14:54:53 GMT+0300",isSuccess -> {
                //TODO:
            });
        });


        return view;
    }

    private void refresh() {
    adapter.notifyDataSetChanged();
    swipeRefresh.setRefreshing(false);
//        Model.instance.getAllPostsFromServer(postList -> {
//            viewModel.setData(postList);
//            adapter.notifyDataSetChanged();
//            swipeRefresh.setRefreshing(false);
//        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTv,categoryTv, subCategoryTv, userNameTv;

        ImageButton addToWishList;
        
        Button commentsBtn;
        ShapeableImageView postPic, userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //TODO: change the productName to userName - by the profileID in the mongo
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            addToWishList = itemView.findViewById(R.id.add_to_wish_list_btn);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
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
            Post post = viewModel.getData().getValue().get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));

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

            if(checkIfInsideWishList(holder, post)){
                holder.addToWishList.setImageResource(R.drawable.ic_full_star);
            }
            else{
                holder.addToWishList.setImageResource(R.drawable.ic_star);
            }

            holder.commentsBtn.setOnClickListener((v) -> {
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToCommentFragment(post.getPostId()));
            });
        }

        private void addToWishList(MyViewHolder holder, Post post) {

            if(checkIfInsideWishList(holder, post)){
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
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }

    public boolean checkIfInsideWishList(MyViewHolder holder, Post post){
        if(Model.instance.getProfile().getWishlist().contains(post.getPostId())){
            return true;
        }
        else{
            return false;
        }
    }


    /* *************************************** Menu Functions *************************************** */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.NewPostFragment) {
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionHomePageFragmentToAddNewPostStep1Fragment());
            return true;
        }
        else if(item.getItemId() == R.id.UserProfileFragment){
            startActivity(new Intent(getContext(), UserProfilesActivity.class));
            getActivity().finish();
            return true;
        }
        else if(item.getItemId() == R.id.logout){
            Model.instance.logout(isSuccess -> {
                if(isSuccess){
                    Model.instance.getProfile().setStatus("false");
                    Model.instance.editProfile(null, Model.instance.getProfile(), new Model.EditProfile() {
                        @Override
                        public void onComplete(Boolean isSuccess) {
                            if(isSuccess){
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                            else {
                                Toast.makeText(MyApplication.getContext(), "Can't change to false",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MyApplication.getContext(), "Can't logout",
                            Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
//        else if(item.getItemId() == R.id.Exit){
//            Model.instance.UserLogout();
//            getActivity().finish();
//            return true;
//        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }





}