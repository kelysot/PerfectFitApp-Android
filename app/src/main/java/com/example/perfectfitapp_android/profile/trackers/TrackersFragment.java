package com.example.perfectfitapp_android.profile.trackers;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.profile.followers.FollowersFragment;
import com.example.perfectfitapp_android.profile.followers.FollowersFragmentArgs;
import com.example.perfectfitapp_android.profile.followers.FollowersFragmentDirections;
import com.example.perfectfitapp_android.profile.followers.FollowersViewModel;
import com.squareup.picasso.Picasso;

public class TrackersFragment extends Fragment {

    TrackersViewModel viewModel;
    MyAdapter adapter;
    String profileId;

    public TrackersFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(TrackersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trackers, container, false);

        profileId = TrackersFragmentArgs.fromBundle(getArguments()).getProfileId();

        Model.instance.getProfileByUserName(profileId, profile -> {
            Model.instance.getProfilesByUserNames(profile.getTrackers(), profilesList -> {
                viewModel.setTrackersProfiles(profilesList);
                adapter.notifyDataSetChanged();
            });
        });

        RecyclerView trackersList = view.findViewById(R.id.trackers_rv);
        trackersList.setHasFixedSize(true);
        trackersList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        trackersList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String userName = viewModel.getTrackersProfiles().get(position).getUserName();
            Navigation.findNavController(v).navigate(FollowersFragmentDirections.actionGlobalProfileFragment(userName));
        });

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
            Profile profile = viewModel.getTrackersProfiles().get(position);
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
            if(viewModel.getTrackersProfiles() == null){
                return 0;
            }
            return viewModel.getTrackersProfiles().size();
        }
    }
}