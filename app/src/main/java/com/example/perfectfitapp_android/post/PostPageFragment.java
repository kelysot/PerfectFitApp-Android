package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.perfectfitapp_android.R;

public class PostPageFragment extends Fragment {

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    //TODO: date
    SeekBar sizeAdj, rating;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        productNameEt = view.findViewById(R.id.postpage_productname_et);
        skuEt = view.findViewById(R.id.postpage_sku_et);
        sizeEt = view.findViewById(R.id.postpage_size_et);
        companyEt = view.findViewById(R.id.postpage_company_et);
        colorEt = view.findViewById(R.id.postpage_color_et);
        categoryEt = view.findViewById(R.id.postpage_category_tv);
        subCategoryEt = view.findViewById(R.id.postpage_subcategort_tv);
        descriptionEt = view.findViewById(R.id.postpage_description_et);
        priceEt = view.findViewById(R.id.postpage_price_et);
        linkEt = view.findViewById(R.id.postpage_link_et);
        sizeAdj = view.findViewById(R.id.postpage_sizeadjestment_seekBar);
        rating = view.findViewById(R.id.postpage_rating_seekBar);
        image = view.findViewById(R.id.postpage_image_imv);

        return view;
    }
}