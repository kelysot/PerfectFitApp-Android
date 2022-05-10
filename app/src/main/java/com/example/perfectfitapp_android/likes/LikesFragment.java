package com.example.perfectfitapp_android.likes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.comment.CommentFragment;
import com.example.perfectfitapp_android.comment.CommentFragmentArgs;
import com.example.perfectfitapp_android.comment.CommentViewModel;
import com.example.perfectfitapp_android.model.Comment;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LikesFragment extends Fragment {

    LikesViewModel viewModel;
    MyAdapter adapter;
    String postId;


    public LikesFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(LikesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_likes, container, false);

        postId = LikesFragmentArgs.fromBundle(getArguments()).getPostId();

        Model.instance.getPostById(postId, post -> {

            Model.instance.getAllProfile(profileList -> {
                Model.instance.setProfiles(profileList);
                List<Profile> list = new ArrayList<>();
                for (int i = 0; i < profileList.size(); i++){
                    Profile profile = profileList.get(i);
                    for (String like: post.getLikes()) {
                        if(profile.getUserName().equals(like)){
                            list.add(profile);
                            break;
                        }
                    }
                }
                viewModel.setData(list);
                adapter.notifyDataSetChanged();
            });

//            Model.instance.getProfilesByUserNames(post.getLikes(), new Model.GetProfilesByUserNamesListener() {
//                @Override
//                public void onComplete(List<Profile> profilesList) {
//                    viewModel.setData(profilesList);
//                    adapter.notifyDataSetChanged();
//                }
//            });
        });

        RecyclerView likesList = view.findViewById(R.id.likes_rv);
        likesList.setHasFixedSize(true);
        likesList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        likesList.setAdapter(adapter);

        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTv;
        ImageView userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.likes_row_user_name);
            userPic = itemView.findViewById(R.id.likes_row_user_img);

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
            View view = getLayoutInflater().inflate(R.layout.likes_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Profile profile = viewModel.getData().get(position);
            holder.userNameTv.setText(profile.getUserName());

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

        @Override
        public int getItemCount() {
            if(viewModel.getData() == null){
                return 0;
            }
            return viewModel.getData().size();
        }
    }
}