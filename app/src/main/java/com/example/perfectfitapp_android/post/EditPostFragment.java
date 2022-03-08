package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;


public class EditPostFragment extends Fragment {

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    //TODO: date
    SeekBar sizeAdjSk, ratingSk;
    Button editBtn, deleteBtn;
    String postId;
    Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        productNameEt = view.findViewById(R.id.editpost_productname_et);
        skuEt = view.findViewById(R.id.editpost_sku_et);
        sizeEt = view.findViewById(R.id.editpost_size_et);
        companyEt = view.findViewById(R.id.editpost_company_et);
        colorEt = view.findViewById(R.id.editpost_color_et);
        categoryEt = view.findViewById(R.id.editpost_category_et);
        subCategoryEt = view.findViewById(R.id.editpost_subcategory_et);
        descriptionEt = view.findViewById(R.id.editpost_description_et);
        linkEt = view.findViewById(R.id.editpost_link_et);
        priceEt = view.findViewById(R.id.editpost_price_et);

        sizeAdjSk = view.findViewById(R.id.editpost_sizeadjustment_seekbar);
        ratingSk = view.findViewById(R.id.editpost_rating_seekbar);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        post = Model.instance.getPostById(postId);

        productNameEt.setText(post.getProductName());
        skuEt.setText(post.getSKU());
        sizeEt.setText(post.getSize());
        companyEt.setText(post.getCompany());
        colorEt.setText(post.getColor());
        categoryEt.setText(post.getCategoryId());
        subCategoryEt.setText(post.getSubCategoryId());
        descriptionEt.setText(post.getDescription());
        linkEt.setText(post.getLink());
        priceEt.setText(post.getPrice());

        //TODO: sizeAdjSk, ratingSk, price

        editBtn = view.findViewById(R.id.editpost_edit_btn);
        editBtn.setOnClickListener(v -> edit(view));

        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> delete(view));

        return view;
    }

    private void delete(View view) {

        Model.instance.deletePostByPost(post);

        Navigation.findNavController(view)
                .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());

    }

    private void edit(View view) {


        String productName, sku, size, company, color, category, subCategory, description;
        String link, price;
        //TODO: date
        String sizeAdj, rating;

        productName = productNameEt.getText().toString();
        sku = skuEt.getText().toString();
        size = sizeEt.getText().toString();
        company = companyEt.getText().toString();
        color = colorEt.getText().toString();
        category = categoryEt.getText().toString();
        subCategory = subCategoryEt.getText().toString();
        description = descriptionEt.getText().toString();
        link = linkEt.getText().toString();


        //TODO: postId, profileId, date. pictureUrl, sizeadj, rating, price

        price = "1";
        String date = "8/2/2022";
        String pictureUrl = "";
        sizeAdj = "";
        rating = "";

        post.setProductName(productName);
        post.setSKU(sku);
        post.setSize(size);
        post.setCompany(company);
        post.setColor(color);
        post.setCategoryId(category);
        post.setSubCategoryId(subCategory);
        post.setDescription(description);
        post.setLink(link);

//        Post newPost = new Post(postId,"1",  productName, sku, size, company, color, category,
//                subCategory, description, date,link, sizeAdj, rating, pictureUrl, Integer.parseInt(price));


//        Model.instance.addPost(newPost);
        Navigation.findNavController(view)
                .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());


    }



}