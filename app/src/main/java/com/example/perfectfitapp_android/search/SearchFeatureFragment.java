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
import com.example.perfectfitapp_android.model.generalModel;
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
    List<String> theList,listToSave;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: need to get the sizes/categories/companies/colors/bodyTypes from the server.

        View view = inflater.inflate(R.layout.fragment_search_feature, container, false);

        feature = SearchFeatureFragmentArgs.fromBundle(getArguments()).getFeature();
        theList = new ArrayList<>();
        nameTv = view.findViewById(R.id.searchfeature_name_tv);

        rv = view.findViewById(R.id.searchfeature_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        checkFeature();

        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        if(SearchModel.instance.mapToServer.get(feature) != null){
            listToSave = SearchModel.instance.mapToServer.get(feature);
        }
        else{
            listToSave = new ArrayList<>();
        }

        adapter.setOnItemClickListener((v, position) -> {
            String aa = theList.get(position);
            System.out.println("row " + aa + "was clicked");
//            SearchModel.instance.map.
        });

        System.out.println("list to save --------- " + listToSave);

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
                String fr = theList.get(position);
                if(listToSave.contains(fr)){
                    listToSave.remove(fr);
                }
                else{
                    listToSave.add(fr);
                }
                SearchModel.instance.mapToServer.put(feature, listToSave);
//                System.out.println(listToSave);
                System.out.println(SearchModel.instance.mapToServer);
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

            String a = theList.get(position);
            holder.nameTv.setText(theList.get(position));
            //TODO: set checked to the cb

            if(SearchModel.instance.mapToServer.get(feature) != null){
                if (SearchModel.instance.mapToServer.get(feature).contains(theList.get(position))) {
                    holder.cb.setChecked(true);
                } else {
                    holder.cb.setChecked(false);
                }
            }
            else{
                holder.cb.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            if(theList == null) {
                return 0;
            }
            else {
                return theList.size();
            }
        }
    }


    public void checkFeature(){
        if(feature.equals("Categories")){
            nameTv.setText("Categories");
            theList = generalModel.instance.map.get("Categories");
//            theList = SearchModel.instance.map.get("Categories");
        }
        else if(feature.equals("Sizes")){
            nameTv.setText("Sizes");
//            theList = SearchModel.instance.map.get("Sizes");
            theList = generalModel.instance.map.get("Sizes");
        }
        else if(feature.equals("Companies")){
            nameTv.setText("Companies");
//            theList = SearchModel.instance.map.get("Companies");
            theList = generalModel.instance.map.get("Companies");
        }
        else if(feature.equals("Colors")){
            nameTv.setText("Colors");
//            theList = SearchModel.instance.map.get("Colors");
            theList = generalModel.instance.map.get("Colors");
        }
        else if(feature.equals("BodyTypes")){
            nameTv.setText("Body Types");
//            theList = SearchModel.instance.map.get("BodyTypes");
            theList = generalModel.instance.map.get("BodyTypes");
        }
        else if(feature.equals("Gender")){
            nameTv.setText("Gender");
//            theList = SearchModel.instance.map.get("Gender");
            theList = generalModel.instance.map.get("Gender");
        }
    }
}