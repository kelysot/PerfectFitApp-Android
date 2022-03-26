package com.example.perfectfitapp_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.post.AddNewPostFragmentDirections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePageFragment extends Fragment {

    List<Post> data;
    MyAdapter adapter;
    Button getPostBtn;

    TextView userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        userName = view.findViewById(R.id.home_page_name_tv);
        userName.setText(Model.instance.getProfile().getUserName());

        getPostBtn = view.findViewById(R.id.homepage_getpost_btn);
        getPostBtn.setOnClickListener(v -> getPost(view));
        data = Model.instance.getAllPosts();

        RecyclerView postsList = view.findViewById(R.id.postlist_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        postsList.setAdapter(adapter);


        adapter.setOnItemClickListener((v, position) -> {
            String postId = data.get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToPostPageFragment2(postId));
        });

        //TODO: menu
        setHasOptionsMenu(true);

        refresh(view);

        return view;
    }

    private void refresh(View view) {
        //TODO: refresh function
        getPost(view);
    }

    private void getPost(View view) {

        System.out.println("button get post clicked");

        Model.instance.getAllPostsFromServer(postList -> {
            if(postList != null){
                List<String> idFromServer = new LinkedList<>();
                List<Post> allPost = Model.instance.getAllPosts();
                List<String> postIdListModel = new LinkedList<>();
                for(int j=0; j<postList.size(); j++){
                    idFromServer.add(postList.get(j).getPostId());
                }
                for (Post p:allPost) {
                    postIdListModel.add(p.getPostId());
                }
                for(int i=0; i<postList.size(); i++){
                    if(!postIdListModel.contains(idFromServer.get(i))){
                        Model.instance.addPost(postList.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                Log.d("TAG", "failed in getAllPosts - getPost in HomePageFragment");
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv, descriptionTv,categoryTv, subCategoryTv, userNameTv;

        ImageButton addToWishList;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //TODO: change the productName to userName - by the profileID in the mongo
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);

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
            Post post = data.get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.addToWishList.setOnClickListener(v -> addToWishList(post));
        }

        private void addToWishList(Post post) {

            if(Model.instance.getProfile().getWishlist().contains(post.getPostId())){
                Model.instance.getProfile().getWishlist().remove(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        //TODO: change the color of the heart
                        System.out.println("after remove " + Model.instance.getProfile().getWishlist());
                    }
                    else{
                        //TODO: toast
                    }
                });

            }
            else{
                Model.instance.getProfile().getWishlist().add(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        System.out.println("the posts added to the list");
                        System.out.println(Model.instance.getProfile().getWishlist());
                    }
                    else{
                        //TODO: toast
                    }
                });
                //TODO: if post is already in the wishlist - we need to remove it
            }
        }


        @Override
        public int getItemCount() {
            return data.size();
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
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionGlobalAddNewPostFragment());
            return true;
        }
        else if(item.getItemId() == R.id.UserProfileFragment){
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionGlobalUserProfilesFragment());
            return true;
        }
        else if(item.getItemId() == R.id.logout){
            Model.instance.logout(new Model.LogoutListener() {
                @Override
                public void onComplete(Boolean isSuccess) {
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
//                    NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.lo);
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