package com.example.perfectfitapp_android.search;

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

    Button categoryBtn, sizeBtn, companyBtn, colorBtn, bodyTypeBtn;
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





        return view;
    }
}