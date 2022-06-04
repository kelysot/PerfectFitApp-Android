package com.example.perfectfitapp_android.home;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.AppLocalDb;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;

import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomePageFragment extends Fragment {

    HomePageViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Button checkDate, makeGeneral;
    LottieAnimationView progressBar;
    int likesSize = 0;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        /*******************/

//        makeGeneral = view.findViewById(R.id.general);
//        makeGeneral.setOnClickListener(v -> {
//            Model.instance.general(isSuccess -> {
//                System.out.println("generallll");
//            });
//        });

        /*******************/

        progressBar = view.findViewById(R.id.home_page_progress_bar);
        progressBar.setVisibility(View.GONE);

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

//            System.out.println("the post is: " + viewModel.getData().getValue().get(position).getPostId());

            Model.instance.setPost(viewModel.getData().getValue().get(position));
            Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToPostPageFragment2(postId, "home"));

            /////////////
//            Model.instance.getPostById(postId, post -> {
//                if(post != null){
//                    System.out.println("the posts id after server: " + post.getPostId());
//                    Model.instance.setPost(post);
//                    Navigation.findNavController(v).navigate(HomePageFragmentDirections.actionHomePageFragmentToPostPageFragment2(postId, "home"));
//                }
//                else{
//                    //TODO: dialog
////                    errorDialog("Please try later");
//                }
//            });
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


//        checkDate = view.findViewById(R.id.check_date);
//        checkDate.setOnClickListener(v -> {
//            Model.instance.getDates("Sun Apr 17 2022 14:54:53 GMT+0300",isSuccess -> {
//                //TODO:
//            });
//        });

        Model.instance.checkNotification();
        Model.instance.refreshPostsList();
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
            Post post = viewModel.getData().getValue().get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));
            holder.addToLikes.setOnClickListener(v-> addToLikes(holder, post));
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
                if (profile != null){
                    String userImg = profile.getUserImageUrl();
                    if(userImg != null && !userImg.equals("")){
                        Model.instance.getImages(profile.getUserImageUrl(), bitmap -> {
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
                else{
                    errorDialog(getResources().getString(R.string.connectionError));
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
                        holder.addToLikes.setImageResource(R.drawable.ic_heart1);
                        refresh();
                    }
                    else {
                        //TODO: dialog
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
                        holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
                        refresh();
                    }
                    else{
                        //TODO: dialog
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
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
                    }
                    else{
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                        //TODO: dialog
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
                        //TODO: dialog
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

    public boolean checkIfInsideWishList(Post post){
        return Model.instance.getProfile().getWishlist().contains(post.getPostId());
    }

    public boolean checkIfInsideLikes(Post post){
        return post.getLikes().contains(Model.instance.getProfile().getUserName());
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
//            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionHomePageFragmentToAddNewPostStep1Fragment());
            NavHostFragment.findNavController(this).navigate(HomePageFragmentDirections.actionHomePageFragmentToAddNewPostStep2());

            return true;
        }
        else if(item.getItemId() == R.id.UserProfileFragment){
            startActivity(new Intent(getContext(), UserProfilesActivity.class));
            getActivity().finish();
            return true;
        }
        else if(item.getItemId() == R.id.logout){
            showDialog();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText("Do you want to log out of PerfectFit?");

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnYes.setEnabled(false);
            btnNo.setEnabled(false);
            logout();
        });
//        btnYes.setOnClickListener(v -> logout());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
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

    private void logout() {
        Model.instance.logout(isSuccess -> {
            if(isSuccess){
                Model.instance.getProfile().setStatus("false");
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess1 -> {
                    if(isSuccess1){
                        Model.instance.logoutFromAppLocalDB(isSuccess2 -> {
                            if(isSuccess2){
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Model.instance.getProfile().setStatus("true");
                        //TODO: dialog
                        Toast.makeText(MyApplication.getContext(), "Can't change to false - to logout",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                progressBar.setVisibility(View.GONE);
                //TODO: dialog
                Toast.makeText(MyApplication.getContext(), "Can't logout",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}