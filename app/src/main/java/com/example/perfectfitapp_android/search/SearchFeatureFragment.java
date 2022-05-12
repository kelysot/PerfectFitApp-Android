package com.example.perfectfitapp_android.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.sub_category.SubCategoryDetailsPostsFragmentArgs;


public class SearchFeatureFragment extends Fragment {

    TextView nameTv;
    RecyclerView rv;
    String feature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_feature, container, false);

        feature = SearchFeatureFragmentArgs.fromBundle(getArguments()).getFeature();

        nameTv = view.findViewById(R.id.searchfeature_name_tv);
        rv = view.findViewById(R.id.searchfeature_rv);

        if(feature.equals("category")){
            System.out.println("1");
        }
        else if(feature.equals("size")){
            System.out.println("2");
        }
        else if(feature.equals("company")){
            System.out.println("3");
        }
        else if(feature.equals("color")){
            System.out.println("4");
        }
        else if(feature.equals("bodyType")){
            System.out.println("5");
        }
        return view;
    }
}