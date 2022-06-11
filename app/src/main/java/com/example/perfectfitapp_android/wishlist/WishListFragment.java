package com.example.perfectfitapp_android.wishlist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;


public class WishListFragment extends Fragment {

    WishListViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    int likesSize = 0;
    LottieAnimationView noPostImg;
    TextView noPostTv, titleTv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        RecyclerView myWishList = view.findViewById(R.id.wishlist_rv);
        myWishList.setHasFixedSize(true);
        myWishList.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefresh = view.findViewById(R.id.wishlist_postlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> refresh());

        titleTv = view.findViewById(R.id.wishlist_title_tv);
        titleTv.setVisibility(View.VISIBLE);

        noPostImg = view.findViewById(R.id.wishlist_no_post_img);
        noPostTv = view.findViewById(R.id.wishlist_no_post_tv);
        noPostImg.setVisibility(View.GONE);
        noPostTv.setVisibility(View.GONE);

        adapter = new MyAdapter();
        myWishList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            Model.instance.getPostById(postId, post -> {
                if(post != null){
                    Model.instance.setPost(post);
                    Navigation.findNavController(v).navigate(WishListFragmentDirections.actionGlobalPostPageFragment(postId,"wishlist" ));
                }
            });
        });

        Model.instance.checkNotification();
        refresh();

        return view;
    }

    private void refresh() {
        Model.instance.getWishListFromServer(list -> {
            if(list != null){
                if( list.isEmpty()){
                    noPostImg.setVisibility(View.VISIBLE);
                    noPostTv.setVisibility(View.VISIBLE);
                    titleTv.setVisibility(View.GONE);
                }
                viewModel.setData(list);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
            else{
                noPostImg.setVisibility(View.VISIBLE);
                noPostTv.setVisibility(View.VISIBLE);
                errorDialog(getResources().getString(R.string.connectionError));
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTv, categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ImageButton addToWishListBtn, commentsBtn, addToLikes;
        ShapeableImageView postPic, userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
        //    descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            addToWishListBtn = itemView.findViewById(R.id.add_to_wish_list_btn);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
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
            holder.addToWishListBtn.setImageResource(R.drawable.ic_addtowishlistfill);
            holder.addToWishListBtn.setOnClickListener(v -> removeFromList(holder, post));
            holder.addToLikes.setOnClickListener(v-> addToLikes(holder, post));
            Model.instance.timeSince(post.getDate(), timeAgo -> holder.timeAgoTv.setText(timeAgo));

            Model.instance.getProfileByUserName(post.getProfileId(), profile -> {
                if(profile != null){
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
                else{
                    errorDialog(getResources().getString(R.string.connectionError));
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
                    Navigation.findNavController(v).navigate(WishListFragmentDirections.actionWishListFragmentToLikesFragment(post.getPostId()));
                });
            }
            else {
                holder.likesNumberTV.setOnClickListener(v -> {}); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener((v) -> {
                Navigation.findNavController(v).navigate(WishListFragmentDirections.actionWishListFragmentToCommentFragment(post.getPostId()));
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
                        errorDialog(getResources().getString(R.string.outError));
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
                            post.getProfileId(), Model.instance.getProfile().getUserName() + " liked your post.", "10/5/22", post.getPostId(), "false");
                    Model.instance.addNewNotification(notification, notification1 -> {});
                }
            }
        }

        public void removeFromList(MyViewHolder holder, Post post){
            Model.instance.getProfile().getWishlist().remove(post.getPostId());
            Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                if(isSuccess){
                    holder.addToWishListBtn.setImageResource(R.drawable.ic_addtowishlist);
                    refresh();
                }
                else{
                    showOkDialog(getResources().getString(R.string.outError));
                }
            });

        }

        public boolean checkIfInsideLikes(Post post){
            return post.getLikes().contains(Model.instance.getProfile().getUserName());
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
    }


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
        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setVisibility(View.GONE);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setVisibility(View.GONE);
        dialog.show();
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