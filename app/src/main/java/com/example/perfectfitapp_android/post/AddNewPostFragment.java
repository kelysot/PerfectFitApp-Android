package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

import java.util.Locale;


public class AddNewPostFragment extends Fragment {

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    //TODO: date
    SeekBar sizeAdjSk, ratingSk;
    Button postBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);

        productNameEt = view.findViewById(R.id.addnewpost_productname_et);
        skuEt = view.findViewById(R.id.addnewpost_sku_et);
        sizeEt = view.findViewById(R.id.addnewpost_size_et);
        companyEt = view.findViewById(R.id.addnewpost_company_et);
        colorEt = view.findViewById(R.id.addnewpost_color_et);
        categoryEt = view.findViewById(R.id.addnewpost_category_et);
        subCategoryEt = view.findViewById(R.id.addnewpost_subcategory_et);
        descriptionEt = view.findViewById(R.id.addnewpost_description_et);
        linkEt = view.findViewById(R.id.addnewpost_link_et);
        priceEt = view.findViewById(R.id.addnewpost_price_et);

        sizeAdjSk = view.findViewById(R.id.addnewpost_sizeadjustment_seekbar);
        ratingSk = view.findViewById(R.id.addnewpost_rating_seekbar);

        postBtn = view.findViewById(R.id.addnewpost_post_btn);
        postBtn.setOnClickListener(v -> post(view));
        
        return view;
    }

    private void post(View view) {

        String productName, sku, size, company, color, category, subCategory, description;
        String link, price;
        //TODO: date
        String sizeAdj, rating;

        productName = productNameEt.toString().trim();
        sku = skuEt.toString().trim();
        size = sizeEt.toString().trim();
        company = companyEt.toString().trim();
        color = colorEt.toString().trim();
        category = categoryEt.toString().trim();
        subCategory = subCategoryEt.toString().trim();
        description = descriptionEt.toString().trim();
        link = linkEt.toString().trim();


        //TODO: postId, profileId, date. pictureUrl, sizeadj, rating, price

        price = "1";
        String date = "8/2/2022";
        String pictureUrl = "";
        sizeAdj = "";
        rating = "";

        Post newPost = new Post("1","1",  productName, sku, size, company, color, category,
                subCategory, description, date,link, sizeAdj, rating, pictureUrl, Integer.parseInt(price));


        Model.instance.addPost(newPost);
        Navigation.findNavController(view)
                .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());


    }
}