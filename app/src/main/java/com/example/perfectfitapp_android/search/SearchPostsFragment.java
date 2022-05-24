package com.example.perfectfitapp_android.search;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.generalModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class SearchPostsFragment extends Fragment {

    RecyclerView rv;
    MyAdapter adapter;
    SearchViewModel viewModel;
//    SwipeRefreshLayout swipeRefresh;
    EditText searchEt;
    ImageButton searchBtn;
    String theSearch;
    ProgressBar progressBar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);

        viewModel.setData(SearchModel.instance.list);
        //TODO: check if SearchModel.instance.list.size() == 0
        // if so, we need to popup and build a button that return to search and delete
        // the mapToSend

        rv = view.findViewById(R.id.searchposts_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: turn off the swipeRefresh
//        swipeRefresh = view.findViewById(R.id.searchposts_swiperefresh);
//        swipeRefresh.setOnRefreshListener(() -> refresh());

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

        if(viewModel.getData().size() == 0){
            String msg = "No matching posts found";

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setNegativeButton("OK", (dialog, which) ->{
                setMapToServer();
                //TODO: move to homepage or searchFragment?
                Navigation.findNavController(view).navigate(SearchPostsFragmentDirections.actionGlobalHomePageFragment());
                dialog.cancel();
            });

            AlertDialog alert = builder.create();
            alert.setTitle("Error");
            alert.setMessage("\n" + msg + "\n");
            alert.show();
        }

        progressBar = view.findViewById(R.id.searchposts_progress_bar);
        progressBar.setVisibility(View.GONE);

        searchEt = view.findViewById(R.id.searchposts_text_et);
        if(theSearch != null){
            if(!theSearch.isEmpty()){
                List<Post> posts = searchMap();
                viewModel.setData(posts);
                adapter.notifyDataSetChanged();
            }
        }

        searchBtn = view.findViewById(R.id.searchposts_search_btn);
        searchBtn.setOnClickListener(v -> {
            search();
        });



//        refresh();

        return view;
    }

    public void search(){
        progressBar.setVisibility(View.VISIBLE);
        theSearch = searchEt.getText().toString();
        System.out.println(theSearch);
        if(theSearch.isEmpty()){
            viewModel.setData(SearchModel.instance.list);
        }
        else {
            List<Post> posts = searchMap();
            viewModel.setData(posts);
        }
        progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    public List<Post> searchMap(){
        List<Post> posts = new ArrayList<>();
        for (Post p : viewModel.getData()) {
            if (p.getProfileId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getProductName().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSKU().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSize().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getCompany().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getPrice().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getColor().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getCategoryId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSubCategoryId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getDescription().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getLink().contains(theSearch)) {
                posts.add(p);
                continue;
            }
        }
        return posts;
    }
    private void refresh() {
        Model.instance.getSearchPosts(SearchModel.instance.mapToServer, posts -> {
            if(posts != null){
                viewModel.setData(posts);
                adapter.notifyDataSetChanged();
//                swipeRefresh.setRefreshing(false);
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
                            post.getProfileId(), Model.instance.getProfile().getUserName() + " liked your post.", "10/5/22", post.getPostId(), "false");
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
                        //TODO: dialog
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
                        //TODO: dialog
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

        public void setMapToServer(){
        Set<String> names = generalModel.instance.map.keySet();
        String[] arr = new String[names.size()];
        arr = names.toArray(arr);
        for(String a : arr){
            SearchModel.instance.mapToServer.remove(a);
            SearchModel.instance.mapToServer.put(a, new ArrayList<>());
        }
    }
}