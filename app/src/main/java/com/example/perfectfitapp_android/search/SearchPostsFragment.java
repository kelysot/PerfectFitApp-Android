package com.example.perfectfitapp_android.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.perfectfitapp_android.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class SearchPostsFragment extends Fragment {

    Button backToHomeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);

        backToHomeBtn = view.findViewById(R.id.searchposts_back_to_home_btn);
        backToHomeBtn.setOnClickListener(v -> {
            Set<String> names = SearchModel.instance.map.keySet();
            String[] arr = new String[names.size()];
            arr = names.toArray(arr);

            for(String a : arr){
                System.out.println("aaa : " + a.toString());
                SearchModel.instance.map.remove(a);
                SearchModel.instance.map.put(a, new ArrayList<>());
            }

            Navigation.findNavController(view).navigate(SearchPostsFragmentDirections.actionGlobalHomePageFragment());
        });

        return view;
    }
}