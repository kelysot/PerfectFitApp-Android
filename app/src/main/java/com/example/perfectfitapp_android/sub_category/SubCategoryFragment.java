package com.example.perfectfitapp_android.sub_category;
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

import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.category.CategoryFragment;
import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryFragment extends Fragment {

    Button getSubCategories;
    MyAdapter adapter;
    List<SubCategory> data;
    String categoryId;

    public SubCategoryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        categoryId = SubCategoryFragmentArgs.fromBundle(getArguments()).getCategoryId();
//        Log.d("TAG",categoryId);
//        Log.d("TAG",Model.instance.getProfile().getGender());

        getSubCategories = view.findViewById(R.id.get_subCategiries);
        getSubCategories.setOnClickListener(v -> {
            getSubCategory();
        });

        data = Model.instance.getAllSubCategories();

        RecyclerView subCategoryList = view.findViewById(R.id.subCategory_rv);
        subCategoryList.setHasFixedSize(true);
        subCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SubCategoryFragment.MyAdapter();
        subCategoryList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            System.out.println(data.get(position).getName());
        });

        return  view;
    }
    //TODO: change the func
    private void getSubCategory() {
//        Model.instance.getAllSubCategories(subCategoryList -> {
//            for(int i=0;i<subCategoryList.size();i++){
//                Model.instance.addSubCategory(subCategoryList.get(i));
//            }
//            //TODO: refresh function
//            adapter.notifyDataSetChanged();
//        });
//         display all the subCategorise according to the gender of profile and Category
        Model.instance.getSubCategoriesByCategoryId(categoryId,Model.instance.getProfile().getGender(),subCategoryList -> {
            for(int i=0;i<subCategoryList.size();i++){
                Model.instance.addSubCategory(subCategoryList.get(i));
            }
            adapter.notifyDataSetChanged();
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subCategoryNameTv;
        ImageView subCategoryPictureUrl;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            subCategoryNameTv = itemView.findViewById(R.id.subCategory_name);
            subCategoryPictureUrl = itemView.findViewById(R.id.subCategory_name_image);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<SubCategoryFragment.MyViewHolder>{

        SubCategoryFragment.OnItemClickListener listener;
        public void setOnItemClickListener(SubCategoryFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.sub_category_list_row,parent,false);
            SubCategoryFragment.MyViewHolder holder = new SubCategoryFragment.MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            SubCategory subCategory = data.get(position);
            holder.subCategoryNameTv.setText(subCategory.getName());
            if (subCategory.getPictureUrl() != null) {
                Picasso.get().load(subCategory.getPictureUrl()).into(holder.subCategoryPictureUrl);
            }
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