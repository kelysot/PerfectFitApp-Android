package com.example.perfectfitapp_android.notification;

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
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    NotificationViewModel viewModel;
    MyAdapter adapter;

    public NotificationFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        RecyclerView notificationList = view.findViewById(R.id.notification_rv);
        notificationList.setHasFixedSize(true);
        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        notificationList.setAdapter(adapter);

        //TODO: Add post id to notification if we want to go to post by click on notification.
        adapter.setOnItemClickListener((v, position) -> {
            String notificationId = viewModel.getData().get(position).getNotificationId();
            System.out.println("post " + notificationId + " was clicked");
            Model.instance.getNotificationById(notificationId, notification -> {
                Navigation.findNavController(v).navigate(NotificationFragmentDirections.actionGlobalPostPageFragment(notification.getPostId(), "notification"));
            });
        });

        refresh();

        return view;
    }

    private void refresh() {
        Model.instance.getAllNotification(new Model.GetAllNotificationListener() {
            @Override
            public void onComplete(List<Notification> notificationList) {
                Model.instance.setNotifications(notificationList);
                Model.instance.getProfileByUserName(Model.instance.getProfile().getUserName(), new Model.GetProfileByUserName() {
                    @Override
                    public void onComplete(Profile profile) {
                        List<Notification> list = new ArrayList<>();
                        for (int i = 0; i < Model.instance.getNotifications().size(); i++){
                            Notification n = Model.instance.getNotifications().get(i);
                            for (String notification:profile.getNotifications()) {
                                if(n.getNotificationId().equals(notification)){
                                    list.add(n);
                                    break;
                                }
                            }
                        }
                        viewModel.setData(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTv, typeTV, dateTV;
        ImageView userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.notification_list_row_user_name);
            typeTV = itemView.findViewById(R.id.notification_list_row_type);
            userPic = itemView.findViewById(R.id.notification_list_row_user_img);
            dateTV = itemView.findViewById(R.id.notification_list_row_date);

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
            View view = getLayoutInflater().inflate(R.layout.notification_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Notification notification = viewModel.getData().get(position);
            holder.userNameTv.setText(notification.getProfileIdFrom());
            holder.typeTV.setText(notification.getNotificationType());

            Model.instance.timeSince(notification.getDate(), timeAgo -> holder.dateTV.setText(timeAgo));

            Model.instance.getProfileByUserName(notification.getProfileIdFrom(), new Model.GetProfileByUserName() {
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
}