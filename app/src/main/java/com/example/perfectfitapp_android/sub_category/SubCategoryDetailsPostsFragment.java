package com.example.perfectfitapp_android.sub_category;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class SubCategoryDetailsPostsFragment extends Fragment {

    SubCategoryDetailsPostsViewModel viewModel;
    MyAdapter adapter;
    String subCategoryId;
    TextView subCategoryName;
    SwipeRefreshLayout swipeRefresh;
    int likesSize = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(SubCategoryDetailsPostsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sub_category_details_posts, container, false);

        subCategoryId = SubCategoryDetailsPostsFragmentArgs.fromBundle(getArguments()).getSubCategoryId();

        Model.instance.getSubCategoryById(subCategoryId, subCategory -> {
            if(subCategory != null){
                viewModel.setSubCategory(subCategory);
                subCategoryName.setText(subCategory.getName());
            }
            else{
                showOkDialog(getResources().getString(R.string.outError));
            }
        });

        subCategoryName = view.findViewById(R.id.sub_category_name);

        RecyclerView mySubCategoryDetailsPosts = view.findViewById(R.id.subCategory_posts_rv);
        mySubCategoryDetailsPosts.setHasFixedSize(true);
        mySubCategoryDetailsPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefresh = view.findViewById(R.id.subCategory_posts_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refresh());

        adapter = new MyAdapter();
        mySubCategoryDetailsPosts.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            Model.instance.getPostById(postId, post -> {
                //TODO: bring the post from appLocalDB
                Model.instance.setPost(post);
                Navigation.findNavController(v).navigate(SubCategoryDetailsPostsFragmentDirections.actionGlobalPostPageFragment(postId,"subCategoryDetailsPostsFragment" ));
            });
        });

        refresh();


        return view;
    }

    private void refresh() {

        Model.instance.getPostsBySubCategoryId(subCategoryId, posts -> {
            if(posts != null){
                Collections.reverse(posts);
                viewModel.setData(posts);
                swipeRefresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTv, categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ShapeableImageView postPic, userPic ;
        ImageButton addToWishList, commentsBtn, addToLikes;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
           // descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
            addToWishList = itemView.findViewById(R.id.add_to_wish_list_btn);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            likesNumberTV = itemView.findViewById(R.id.listrow_post_likes_number);
            addToLikes = itemView.findViewById(R.id.listrow_post_likes_btn);
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
            holder.userNameTv.setText(post.getProfileId());
//            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
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
                                .load(R.drawable.user_default).resize(250, 180)
                                .centerCrop()
                                .into(holder.userPic);
                    }
                }
            });

            Model.instance.getProfilesByUserNames(post.getLikes(), profilesList -> {
                likesSize = 0;
                for(int i = 0; i< profilesList.size(); i++){
                    if(profilesList.get(i).getIsDeleted().equals("false")){
                        likesSize++;
                    }
                }
                holder.likesNumberTV.setText(String.valueOf(likesSize) + " likes");
            });


            if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0 ) {
                Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                    holder.postPic.setImageBitmap(bitmap);
                });
            }
            else {
                Picasso.get()
                        .load(R.drawable.coverphotoprofile).resize(250, 180)
                        .centerCrop()
                        .into(holder.postPic);
            }

            if(checkIfInsideWishList(post)){
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
            }
            else{
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
            }

            if(checkIfInsideLikes(post)){
                holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
            }
            else{
                holder.addToLikes.setImageResource(R.drawable.ic_heart1);
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
                    Navigation.findNavController(v).navigate(SubCategoryDetailsPostsFragmentDirections.actionSubCategoryDetailsPostsFragmentToLikesFragment(post.getPostId()));
                });
            }
            else {
                holder.likesNumberTV.setOnClickListener(v -> {}); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener((v) -> {
                Navigation.findNavController(v).navigate(SubCategoryDetailsPostsFragmentDirections.actionSubCategoryDetailsPostsFragmentToCommentFragment(post.getPostId()));
            });
        }

        private void addToLikes(MyViewHolder holder, Post post) {
            String userName = Model.instance.getProfile().getUserName();
            if(checkIfInsideLikes(post)){
                post.getLikes().remove(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if(isSuccess){
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_heart1);
                        refresh();
                    }
                    else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });

            }
            else{
                post.getLikes().add(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if(isSuccess){
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
                        refresh();
                    }
                    else{
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });

                if(!Model.instance.getProfile().getUserName().equals(post.getProfileId())){
                    Notification notification =  new Notification("0", Model.instance.getProfile().getUserName(),
                            post.getProfileId(), "Liked your post.", "10/5/22", post.getPostId(), "false");
                    Model.instance.addNewNotification(notification, notification1 -> {});
                }
            }
        }

        private void addToWishList(MyViewHolder holder, Post post) {

            if(checkIfInsideWishList(post)){
                Model.instance.getProfile().getWishlist().remove(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
                    }
                    else{
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            }
            else{
                Model.instance.getProfile().getWishlist().add(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
                        System.out.println("the posts added to the list");
                        System.out.println(Model.instance.getProfile().getWishlist());
                    }
                    else{
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData() == null) {
                return 0;
            }
            else {
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

    private void showOkDialog(String text){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}