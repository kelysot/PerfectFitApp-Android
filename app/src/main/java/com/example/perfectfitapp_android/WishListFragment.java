package com.example.perfectfitapp_android;

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
import android.widget.TextView;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.profile.ProfileFragmentDirections;

import java.util.LinkedList;
import java.util.List;


public class WishListFragment extends Fragment {

    WishListViewModel viewModel;
    MyAdapter adapter;

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

        adapter = new MyAdapter();
        myWishList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            System.out.println("post " + postId + " was clicked");
            Model.instance.getPostById(postId, post -> {
                //TODO: bring the post from appLocalDB
                Model.instance.setPost(post);
                Navigation.findNavController(v).navigate(WishListFragmentDirections.actionGlobalPostPageFragment(postId,"wishlist" ));
            });
        });

        refresh();

        return view;
    }

    private void refresh() {
        Model.instance.getWishListFromServer(list -> {
            viewModel.setData(list);
            adapter.notifyDataSetChanged();
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv, descriptionTv, categoryTv, subCategoryTv, userNameTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            descriptionTv = itemView.findViewById(R.id.listrow_description_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);

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