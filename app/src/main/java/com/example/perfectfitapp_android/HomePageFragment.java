package com.example.perfectfitapp_android;

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
import android.widget.TextView;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.ArrayList;
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
    Button getPostBtn, goUserBtn;
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

        goUserBtn = view.findViewById(R.id.home_get_profile_btn);
        goUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToProfileFragment());

            }
        });

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

        return view;
    }

    private void getPost(View view) {

        System.out.println("button get post clicked");

        Model.instance.getAllPostsFromServer(postList -> {

            //TODO: check if postsList is null

            for(int i=0; i<postList.size(); i++){
                Model.instance.addPost(postList.get(i));
            }
            //TODO: refresh function
            Navigation.findNavController(view).navigate(HomePageFragmentDirections.actionGlobalHomePageFragment());
        });

//        Model.instance.getAllPosts();
//        restClient.setCallback(new RestClient.ResultReadyCallback() {
//            @Override
//            public void resultReady(List<Post> posts) {
//                Log.d("TAG123", posts + "11111111111111111111");
//                Log.d("TAG123", posts.get(0).getPostId() + "11111111111111111111");
//
//                for(int i = 0; i < posts.size(); i++){
//                    Model.instance.addPost(posts.get(i));
//                }
//                //TODO: refresh function
//                Navigation.findNavController(view)
//                        .navigate(HomePageFragmentDirections.actionGlobalHomePageFragment());
//            }
//        });
//        restClient.getPosts();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv;
        TextView descriptionTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
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
            holder.productNameTv.setText(post.getProductName());
            holder.descriptionTv.setText(post.getDescription());
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