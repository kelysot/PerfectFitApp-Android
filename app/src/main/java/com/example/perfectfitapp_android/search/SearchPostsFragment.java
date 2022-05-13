package com.example.perfectfitapp_android.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.profile.ProfileViewModel;
import com.example.perfectfitapp_android.wishlist.WishListFragment;
import com.example.perfectfitapp_android.wishlist.WishListFragmentDirections;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



public class SearchPostsFragment extends Fragment {

    Button backToHomeBtn;
    RecyclerView rv;
    MyAdapter adapter;
    SearchViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);

        backToHomeBtn = view.findViewById(R.id.searchposts_back_to_home_btn);
        backToHomeBtn.setOnClickListener(v -> {
            Set<String> names = SearchModel.instance.map.keySet();
            String[] arr = new String[names.size()];
            arr = names.toArray(arr);
            for(String a : arr){
                System.out.println("aaa : " + a.toString());
                SearchModel.instance.map.remove(a);
                SearchModel.instance.map.put(a, new ArrayList<>());
            }
            Navigation.findNavController(view).navigate(SearchPostsFragmentDirections.actionGlobalHomePageFragment());
        });

        rv = view.findViewById(R.id.searchposts_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefresh = view.findViewById(R.id.searchposts_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refresh());

        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            Model.instance.getPostById(postId, post -> {
                //TODO: bring the post from appLocalDB
                Model.instance.setPost(post);
                Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionGlobalPostPageFragment(postId,"wishlist" ));
            });
        });

        refresh();

        return view;
    }

    private void refresh() {
        Model.instance.getSearchPosts(SearchModel.instance.map, posts -> {
            if(posts != null){
                viewModel.setData(posts);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionTv,categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ImageButton addToWishList, addToLikes, commentsBtn;
        ShapeableImageView postPic, userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //TODO: change the productName to userName - by the profileID in the mongo
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            addToWishList = itemView.findViewById(R.id.add_to_wish_list_btn);
            addToLikes = itemView.findViewById(R.id.listrow_post_likes_btn);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
            likesNumberTV = itemView.findViewById(R.id.listrow_post_likes_number);
            timeAgoTv = itemView.findViewById(R.id.listrow_time_ago_tv);

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
//            Post post = SearchModel.instance.list.get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));
            holder.addToLikes.setOnClickListener(v-> addToLikes(holder, post));
            Model.instance.timeSince(post.getDate(), timeAgo -> holder.timeAgoTv.setText(timeAgo));

            Model.instance.getProfileByUserName(post.getProfileId(), profile -> {
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

            // Move to different pages from post.

            holder.userNameTv.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionGlobalProfileFragment(post.getProfileId()));
            });

            holder.userPic.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionGlobalProfileFragment(post.getProfileId()));
            });

            if(post.getLikes().size() != 0){
                holder.likesNumberTV.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToLikesFragment(post.getPostId()));
                });
            }
            else {
                holder.likesNumberTV.setOnClickListener(v -> {}); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToCommentFragment(post.getPostId()));
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
            if( viewModel.getData() == null){
                return 0;
            }
            return viewModel.getData().size();
        }
    }



    public boolean checkIfInsideWishList(Post post){
        return Model.instance.getProfile().getWishlist().contains(post.getPostId());
    }

    public boolean checkIfInsideLikes(Post post){
        return post.getLikes().contains(Model.instance.getProfile().getUserName());
    }
}