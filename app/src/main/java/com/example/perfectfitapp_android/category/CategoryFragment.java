package com.example.perfectfitapp_android.category;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryFragment extends Fragment {

    CategoryViewModel viewModel;
    MyAdapter adapter;
    Button filterBtn;


    public CategoryFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView categoryList = view.findViewById(R.id.category_rv);
        categoryList.setHasFixedSize(true);
        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        filterBtn = view.findViewById(R.id.category_filter_btn);
        filterBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchFragment());

        });

        adapter = new CategoryFragment.MyAdapter();
        categoryList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String categoryId = viewModel.getData().get(position).getCategoryId();
            Navigation.findNavController(v).navigate(CategoryFragmentDirections.actionCategoryFragmentToSubCategoryFragment(categoryId));
        });
        refresh();
        Model.instance.checkNotification();

        return view;
    }

    private void refresh() {
        Model.instance.getAllCategoriesListener(list -> {
            Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            viewModel.setData(list);
            adapter.notifyDataSetChanged();
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
            Category category = viewModel.getData().get(position);
            holder.categoryNameTv.setText(category.getName());
            if (category.getPictureUrl() != null) {
                Picasso.get().load(category.getPictureUrl()).into(holder.categoryImage);
            }
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData() == null)
                return 0;
            else
                return viewModel.getData().size();
        }
    }
}