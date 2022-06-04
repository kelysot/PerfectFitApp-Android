package com.example.perfectfitapp_android.search;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.generalModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SearchFragment extends Fragment {

    Button categoryBtn, sizeBtn, companyBtn, colorBtn, bodyTypeBtn, genderBtn, searchBtn;
    EditText priceFromEt, priceToEt;
    LottieAnimationView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        categoryBtn = view.findViewById(R.id.search_category_btn);
        sizeBtn = view.findViewById(R.id.search_size_btn);
        companyBtn = view.findViewById(R.id.search_company_btn);
        colorBtn = view.findViewById(R.id.search_color_btn);
        bodyTypeBtn = view.findViewById(R.id.search_bodytype_btn);
        genderBtn = view.findViewById(R.id.search_gender_btn);
        priceFromEt = view.findViewById(R.id.search_price_from_et);
        priceToEt = view.findViewById(R.id.search_price_to_et);
        progressBar = view.findViewById(R.id.search_progress_bar);
        progressBar.setVisibility(View.GONE);

        setMap();

        //TODO: 1

        if(SearchModel.instance.mapToServer.get("Categories") != null){
            if(!SearchModel.instance.mapToServer.get("Categories").isEmpty()) {
                categoryBtn.setTextColor(Color.GRAY);
            }
        }
        if(SearchModel.instance.mapToServer.get("Sizes") != null){
            if(!SearchModel.instance.mapToServer.get("Sizes").isEmpty()){
                sizeBtn.setTextColor(Color.GRAY);
            }
        }
        if(SearchModel.instance.mapToServer.get("Companies") != null){
            if(!SearchModel.instance.mapToServer.get("Companies").isEmpty()){
                companyBtn.setTextColor(Color.GRAY);
            }
        }
        if(SearchModel.instance.mapToServer.get("Colors") != null){
            if(!SearchModel.instance.mapToServer.get("Colors").isEmpty()){
                colorBtn.setTextColor(Color.GRAY);
            }
        }
        if(SearchModel.instance.mapToServer.get("BodyTypes") != null){
            if(!SearchModel.instance.mapToServer.get("BodyTypes").isEmpty()){
                bodyTypeBtn.setTextColor(Color.GRAY);
            }
        }
        if(SearchModel.instance.mapToServer.get("Gender") != null){
            if(!SearchModel.instance.mapToServer.get("Gender").isEmpty()){
                genderBtn.setTextColor(Color.GRAY);
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
        genderBtn.setOnClickListener(V -> {
            Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchFeatureFragment("Gender"));
        });

        searchBtn = view.findViewById(R.id.search_search_btn);
        searchBtn.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            searchBtn.setEnabled(false);
            int count = 0;

            //TODO: check if isEmpty
            if(SearchModel.instance.mapToServer.get("Sizes").isEmpty()){
                SearchModel.instance.mapToServer.put("Sizes", generalModel.instance.map.get("Sizes"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Categories").isEmpty()){
                SearchModel.instance.mapToServer.put("Categories", generalModel.instance.map.get("Categories"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Colors").isEmpty()){
                SearchModel.instance.mapToServer.put("Colors", generalModel.instance.map.get("Colors"));
                count++;
            }if(SearchModel.instance.mapToServer.get("Companies").isEmpty()){
                SearchModel.instance.mapToServer.put("Companies", generalModel.instance.map.get("Companies"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("BodyTypes").isEmpty()){
                SearchModel.instance.mapToServer.put("BodyTypes", generalModel.instance.map.get("BodyTypes"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Gender").isEmpty()){
                SearchModel.instance.mapToServer.put("Gender", generalModel.instance.map.get("Gender"));
                count++;
            }

            List<String> countList = new ArrayList<>();
            if(count == 6){
                countList.add("true");
            }
            else{
                countList.add("false");
            }
            SearchModel.instance.mapToServer.put("Count", countList);

            List<String> priceList = new ArrayList<>();
            String priceFrom = priceFromEt.getText().toString().trim();
            if(priceFrom.isEmpty()){
                priceFrom = "false";
            }
            String priceTo = priceToEt.getText().toString().trim();
            if(priceTo.isEmpty()){
                priceTo = "false";
            }
            priceList.add(priceFrom);
            priceList.add(priceTo);
            SearchModel.instance.mapToServer.put("Price", priceList);

            Model.instance.getSearchPosts(SearchModel.instance.mapToServer, posts -> {
                if(posts != null){
                    setMapToServer();
                    SearchModel.instance.list = posts;
                    Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToSearchPostsFragment());
                }
                else{
                    System.out.println("problem at searchFragment 1");
                    progressBar.setVisibility(View.GONE);
                    searchBtn.setEnabled(true);
                    //TODO: dialog
                    showOkDialog();
                }
            });
        });

        return view;
    }

    //TODO: this function need to be called when we press on "filter" button.
    private void setMap() {
        Model.instance.getGeneral(map -> {
            if(map != null){
                generalModel.instance.map = map;
                Model.instance.getAllCategoriesListener(categoryList -> {
                    List<String> list = new ArrayList<>();
                    for(int i=0; i< categoryList.size(); i++){
                        list.add(categoryList.get(i).getName());
                    }
                    generalModel.instance.map.put("Categories", list);
                });
            }
            else{

                //TODO: dialog
            }
        });
//        Model.instance.getAllCategoriesListener(categoryList -> {
//            List<String> list = new ArrayList<>();
//            for(int i=0; i< categoryList.size(); i++){
//                list.add(categoryList.get(i).getName());
//            }
//            generalModel.instance.map.put("Categories", list);
//        });
    }

    public void setMapToServer(){
        Set keys = SearchModel.instance.mapToServer.keySet();
        Object[] arr = keys.toArray();
        List<String> names = new ArrayList<>();
        for(int i=0; i<keys.size(); i++){
            names.add(arr[i].toString());
        }
        for(int i=0; i<names.size(); i++){
            SearchModel.instance.mapToServer.remove(names.get(i));
            SearchModel.instance.mapToServer.put(names.get(i), new ArrayList<>());
        }
    }

    private void showOkDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(getResources().getString(R.string.outError));

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}