package com.example.perfectfitapp_android.search;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.sub_category.SubCategoryDetailsPostsFragmentDirections;


public class SearchFragment extends Fragment {

    Button categoryBtn, sizeBtn, companyBtn, colorBtn, bodyTypeBtn, showMapBtn;
    EditText priceFrom, priceTo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        categoryBtn = view.findViewById(R.id.search_category_btn);
        sizeBtn = view.findViewById(R.id.search_size_btn);
        companyBtn = view.findViewById(R.id.search_company_btn);
        colorBtn = view.findViewById(R.id.search_color_btn);
        bodyTypeBtn = view.findViewById(R.id.search_bodytype_btn);
        priceFrom = view.findViewById(R.id.search_price_from_et);
        priceTo = view.findViewById(R.id.search_price_to_et);

        if(!SearchModel.instance.map.get("Categories").isEmpty()){
            categoryBtn.setTextColor(Color.BLUE);
        }
        if(!SearchModel.instance.map.get("Sizes").isEmpty()){
            sizeBtn.setTextColor(Color.BLUE);
        }
        if(!SearchModel.instance.map.get("Companies").isEmpty()){
            companyBtn.setTextColor(Color.BLUE);
        }
        if(!SearchModel.instance.map.get("Colors").isEmpty()){
            colorBtn.setTextColor(Color.BLUE);
        }
        if(!SearchModel.instance.map.get("BodyTypes").isEmpty()){
            bodyTypeBtn.setTextColor(Color.BLUE);
        }

        categoryBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("category"));
        });
        sizeBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("size"));
        });
        companyBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("company"));
        });
        colorBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("color"));
        });
        bodyTypeBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("bodyType"));
        });


        showMapBtn = view.findViewById(R.id.showmap_btn);

        showMapBtn.setOnClickListener(v -> {
            System.out.println(SearchModel.instance.map);
        });

        return view;
    }
}