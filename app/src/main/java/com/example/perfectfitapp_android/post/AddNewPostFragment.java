package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.perfectfitapp_android.R;


public class AddNewPostFragment extends Fragment {

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, price;
    //TODO: date
    SeekBar sizeAdjSk, rating;

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
        price = view.findViewById(R.id.addnewpost_price_et);

        sizeAdjSk = view.findViewById(R.id.addnewpost_sizeadjustment_seekbar);
        rating = view.findViewById(R.id.addnewpost_rating_seekbar);





        return view;
    }
}