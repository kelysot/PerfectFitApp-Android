package com.example.perfectfitapp_android.category;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryFragment extends Fragment {

    List<Category> data;
    MyAdapter adapter;
    Button getCategories;


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);


        getCategories = view.findViewById(R.id.get_categories);
        getCategories.setOnClickListener(v -> getCategory(view));

        data = Model.instance.getAllCategories();

        RecyclerView categoryList = view.findViewById(R.id.category_rv);
        categoryList.setHasFixedSize(true);
        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CategoryFragment.MyAdapter();
        categoryList.setAdapter(adapter);


        adapter.setOnItemClickListener((v, position) -> {
            String categoryId = data.get(position).getCategoryId();
            System.out.println(categoryId);
            //TODO:Move to sub category page.
        });


        return view;
    }

    private void getCategory(View view) {
        Model.instance.getAllCategoriesListener(new Model.GetAllCategoriesListener() {
            @Override
            public void onComplete(List<Category> categoryList) {
                for (int i = 0; i < categoryList.size(); i++){
                    Model.instance.addCategory(categoryList.get(i));
                }
                Navigation.findNavController(view).navigate(CategoryFragmentDirections.actionGlobalCategoryFragment());
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTv;
        ImageView categoryImage;

        public MyViewHolder(@NonNull View itemView, CategoryFragment.OnItemClickListener listener) {
            super(itemView);
            categoryNameTv = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_pic_url);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });

        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<CategoryFragment.MyViewHolder> {

        CategoryFragment.OnItemClickListener listener;

        public void setOnItemClickListener(CategoryFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public CategoryFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.category_list_row, parent, false);
            CategoryFragment.MyViewHolder holder = new CategoryFragment.MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryFragment.MyViewHolder holder, int position) {
            Category category = data.get(position);
            holder.categoryNameTv.setText(category.getName());
            //TODO: Add category pic in category view.
//            if (category.getPictureUrl() != null) {
//                Picasso.get().load(R.drawable.ic_profile).into(holder.categoryImage);
////                Picasso.get()
////                        .load(category.getPictureUrl()).resize(250, 180)
////                        .centerCrop()
////                        .into(holder.categoryImage);
//            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}