package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.List;


public class WishListFragment extends Fragment {

    List<Post> data;
    MyAdapter adapter;
    Button getListBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        //TODO: make a function of getting all the posts of the profile's wishlist
        data = Model.instance.getAllPosts();

        getListBtn = view.findViewById(R.id.wishlist_get_list_btn);
        getListBtn.setOnClickListener(v -> getWishList());

        RecyclerView myWishList = view.findViewById(R.id.wishlist_rv);
        myWishList.setHasFixedSize(true);
        myWishList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        myWishList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = data.get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            Navigation.findNavController(v).navigate(WishListFragmentDirections.actionGlobalPostPageFragment(postId));
        });
        return view;
    }

    private void getWishList() {
        System.out.println("--------- wish list btn was clicked ---------");

        //TODO: fix the function in the server and call the function below:
//        Model.instance.getWishList(Model.instance.getProfile().getWishlist(), list -> {
//            System.out.println("the list is: " + list);
//        });
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
}