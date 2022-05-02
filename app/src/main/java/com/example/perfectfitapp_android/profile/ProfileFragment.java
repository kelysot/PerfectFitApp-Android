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

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.HomePageViewModel;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.post.PostPageFragmentDirections;
import com.example.perfectfitapp_android.sub_category.SubCategoryDetailsPostsFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProfileFragment extends Fragment {

    ProfileViewModel viewModel;
    ImageView userPic;
    TextView userNameTv;
    TextView numOfPosts;
//    ImageButton editProfileBtn;
    MyAdapter adapter;


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
//        editProfileBtn = view.findViewById(R.id.profile_edit_profile_btn);
//        editProfileBtn.setOnClickListener(v-> editProfile(view));

        Profile profile = Model.instance.getProfile();
        userNameTv.setText(profile.getUserName());
        numOfPosts.setText(String.valueOf(profile.getMyPostsListId().size()));
        String userImg = profile.getUserImageUrl();
        if(userImg != null && !userImg.equals("")){
            Model.instance.getImages(userImg, bitmap -> {
                userPic.setImageBitmap(bitmap);
            });
        }


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

        Model.instance.getProfilePosts(Model.instance.getProfile().getUserName(),postList -> {
            viewModel.setData(postList);
            adapter.notifyDataSetChanged();
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv, descriptionTv,categoryTv, subCategoryTv, userNameTv;
        ShapeableImageView postPic, userPic;
        ImageButton addToWishList;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //TODO: change the productName to userName - by the profileID in the mongo
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);

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
            if(viewModel.getData() == null){
                return 0;
            }
            return viewModel.getData().size();
        }

        public boolean checkIfInsideWishList(MyViewHolder holder, Post post){
            if(Model.instance.getProfile().getWishlist().contains(post.getPostId())){
                return true;
            }
            else{
                return false;
            }
        }
    }


//    private void editProfile(View view) {
//        Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());
//    }


}