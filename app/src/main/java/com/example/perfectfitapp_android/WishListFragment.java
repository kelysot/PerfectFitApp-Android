package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.util.LinkedList;
import java.util.List;


public class WishListFragment extends Fragment {

    List<Post> data;
    MyAdapter adapter;
    Button getListBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        getListBtn = view.findViewById(R.id.wishlist_get_list_btn);
        getListBtn.setOnClickListener(v -> getWishList(view));

        data = Model.instance.getWishList();

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

        refresh(view);

        return view;
    }

    private void refresh(View view) {
        getWishList(view);
    }

    private void getWishList(View view) {
        System.out.println("--------- wish list btn was clicked ---------");

//        Model.instance.setWishList(new LinkedList<>());
        //TODO: fix the function in the server and call the function below:
        Model.instance.getWishListFromServer(list -> {

            for(int i=0; i<list.size(); i++){
                Model.instance.addPostToWishList(list.get(i));
            }

//            Model.instance.setWishList(new LinkedList<>());
//            for(int i=0; i<list.size(); i++){
//                Model.instance.addPostToWishList(list.get(i));
//            }
//            List<String> idFromServer = new LinkedList<>();
//            for(int j=0; j<list.size(); j++){
//                idFromServer.add(list.get(j).getPostId());
//            }
//            List<String> idList = new LinkedList<>();
//            for(int i=0; i<Model.instance.getWishList().size(); i++){
//                idList.add(Model.instance.getWishList().get(i).getPostId());
//            }
//            for(int i=0; i<list.size(); i++){
//                if(!idList.contains(idFromServer.get(i))){
//                    Model.instance.addPostToWishList(list.get(i));
//                }
//                if(!idFromServer.contains(idList.get(i))){
//                    Model.instance.deleteFromWishList(Model.instance.getWishList().get(idList.indexOf(idList.get(i))));
//                }
//            }
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
            Post post = data.get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.descriptionTv.setText(post.getDescription());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
        }

        @Override
        public int getItemCount() {
            if(data != null)
                return data.size();
            else
                return 0;
        }
    }
}