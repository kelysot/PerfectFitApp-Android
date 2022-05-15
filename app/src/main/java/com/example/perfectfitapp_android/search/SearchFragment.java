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
import com.example.perfectfitapp_android.model.Category;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.sub_category.SubCategoryDetailsPostsFragmentDirections;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchFragment extends Fragment {

    Button categoryBtn, sizeBtn, companyBtn, colorBtn, bodyTypeBtn, showMapBtn, searchBtn;
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

        setMap();

        //TODO: 1

        if(SearchModel.instance.mapToServer.get("Categories") != null){
            if(!SearchModel.instance.mapToServer.get("Categories").isEmpty()) {
                categoryBtn.setTextColor(Color.BLUE);
            }
        }
        if(SearchModel.instance.mapToServer.get("Sizes") != null){
            if(!SearchModel.instance.mapToServer.get("Sizes").isEmpty()){
                sizeBtn.setTextColor(Color.BLUE);
            }
        }
        if(SearchModel.instance.mapToServer.get("Companies") != null){
            if(!SearchModel.instance.mapToServer.get("Companies").isEmpty()){
                companyBtn.setTextColor(Color.BLUE);
            }
        }
        if(SearchModel.instance.mapToServer.get("Colors") != null){
            if(!SearchModel.instance.mapToServer.get("Colors").isEmpty()){
                colorBtn.setTextColor(Color.BLUE);
            }
        }
        if(SearchModel.instance.mapToServer.get("BodyTypes") != null){
            if(!SearchModel.instance.mapToServer.get("BodyTypes").isEmpty()){
                bodyTypeBtn.setTextColor(Color.BLUE);
            }
        }

        categoryBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("Categories"));
        });
        sizeBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("Sizes"));
        });
        companyBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("Companies"));
        });
        colorBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("Colors"));
        });
        bodyTypeBtn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("BodyTypes"));
        });

        showMapBtn = view.findViewById(R.id.showmap_btn);
        showMapBtn.setOnClickListener(v -> {
            System.out.println(SearchModel.instance.mapToServer);
        });

        searchBtn = view.findViewById(R.id.search_search_btn);
        searchBtn.setOnClickListener(v -> {
            //TODO: check if isEmpty
            if(SearchModel.instance.mapToServer.get("Sizes").isEmpty()){
                // need to: SearchModel.instance.map == all sizes
                SearchModel.instance.mapToServer.put("Sizes", SearchModel.instance.map.get("Sizes"));
            }
            if(SearchModel.instance.mapToServer.get("Categories").isEmpty()){
                SearchModel.instance.mapToServer.put("Categories", SearchModel.instance.map.get("Categories"));
            }
            if(SearchModel.instance.mapToServer.get("Colors").isEmpty()){
                SearchModel.instance.mapToServer.put("Colors", SearchModel.instance.map.get("Colors"));

            }if(SearchModel.instance.mapToServer.get("Companies").isEmpty()){
                SearchModel.instance.mapToServer.put("Companies", SearchModel.instance.map.get("Companies"));
            }
            if(SearchModel.instance.mapToServer.get("BodyTypes").isEmpty()){
                SearchModel.instance.mapToServer.put("BodyTypes", SearchModel.instance.map.get("BodyTypes"));
            }

            System.out.println("the map: *************************************************");
            System.out.println(SearchModel.instance.mapToServer);
            Model.instance.getSearchPosts(SearchModel.instance.mapToServer, posts -> {
                if(posts != null){
                    SearchModel.instance.list = posts;
                    Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchPostsFragment());
                }
                else{
                    System.out.println("problem at searchFragment 1");
                }
            });
        });

        return view;
    }

    //TODO: this function need to be called when we press on "filter" button.
    private void setMap() {
        Model.instance.getAllCategoriesListener(categoryList -> {
            List<String> list = new ArrayList<>();
            for(int i=0; i< categoryList.size(); i++){
                list.add(categoryList.get(i).getName());
            }
//            SearchModel.instance.map.remove("Categories");
            SearchModel.instance.map.put("Categories", list);

            //set others:

            Model.instance.getGeneral(map -> {
                SearchModel.instance.map.put("Sizes", map.get("Sizes"));
                SearchModel.instance.map.put("Companies", map.get("Companies"));
                SearchModel.instance.map.put("Colors", map.get("Colors"));
                SearchModel.instance.map.put("BodyTypes", map.get("BodyTypes"));

//                System.out.println("*******************************");
//                System.out.println(SearchModel.instance.map);
            });
        });
    }
}