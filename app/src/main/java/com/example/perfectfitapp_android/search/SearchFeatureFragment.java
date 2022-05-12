package com.example.perfectfitapp_android.search;

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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.sub_category.SubCategoryDetailsPostsFragmentArgs;
import com.example.perfectfitapp_android.wishlist.WishListFragment;
import com.example.perfectfitapp_android.wishlist.WishListFragmentDirections;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SearchFeatureFragment extends Fragment {

    TextView nameTv;
    RecyclerView rv;
    String feature;
    String[] array;
    List<String> list;
    MyAdapter adapter;
    List<String> listToSave = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: need to get the sizes/categories/companies/colors/bodyTypes from the server.

        View view = inflater.inflate(R.layout.fragment_search_feature, container, false);

        feature = SearchFeatureFragmentArgs.fromBundle(getArguments()).getFeature();
        list = new ArrayList<>();
        nameTv = view.findViewById(R.id.searchfeature_name_tv);

        rv = view.findViewById(R.id.searchfeature_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        checkFeature();

        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String aa = list.get(position);
            System.out.println("row " + aa + "was clicked");
//            SearchModel.instance.map.
        });

        System.out.println(listToSave);

        return view;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        CheckBox cb;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.searchrow_row);
            cb = itemView.findViewById(R.id.searchrow_cb);
            itemView.setClickable(false);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });

            cb.setOnClickListener(v -> {
                int position = getAdapterPosition();
                String fr = list.get(position);
                if(listToSave.contains(fr)){
                    listToSave.remove(fr);
                }
                else{
                    listToSave.add(fr);
                }
                System.out.println(listToSave);
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
            View view = getLayoutInflater().inflate(R.layout.search_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            String a = list.get(position);
            holder.nameTv.setText(list.get(position));
            //TODO: set checked to the cb

            if (listToSave.contains(list.get(position))) {
                holder.cb.setChecked(true);
            } else {
                holder.cb.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            if(list == null) {
                return 0;
            }
            else {
                return list.size();
            }
        }
    }


    public void checkFeature(){
        if(feature.equals("category")){
            nameTv.setText("Categories");
            List<Category> categoryList = Model.instance.getCategories();
            for(int i=0; i<categoryList.size(); i++){
                list.add(categoryList.get(i).getName());
            }
            listToSave = SearchModel.instance.map.get("Categories");
        }
        else if(feature.equals("size")){
            nameTv.setText("Sizes");
            array = getResources().getStringArray(R.array.sizes);
            for(int i=0; i<array.length; i++){
                list.add(array[i]);
            }
            listToSave = new ArrayList<>();
            listToSave = SearchModel.instance.map.get("Sizes");
        }
        else if(feature.equals("company")){
            nameTv.setText("Companies");
            array = getResources().getStringArray(R.array.companies);
            for(int i=0; i<array.length; i++){
                list.add(array[i]);
            }
            listToSave = SearchModel.instance.map.get("Companies");
        }
        else if(feature.equals("color")){
            nameTv.setText("Colors");
            array = getResources().getStringArray(R.array.colors);
            for(int i=0; i<array.length; i++){
                list.add(array[i]);
            }
            listToSave = SearchModel.instance.map.get("Colors");
        }
        else if(feature.equals("bodyType")){
            nameTv.setText("Body Types");
            array = getResources().getStringArray(R.array.body_types);
            for(int i=0; i<array.length; i++){
                list.add(array[i]);
            }
            listToSave = SearchModel.instance.map.get("BodyTypes");
        }
    }



}