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

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    Button categoryBtn, sizeBtn, companyBtn, colorBtn, bodyTypeBtn, genderBtn, showMapBtn, searchBtn;
    EditText priceFromEt, priceToEt;

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
        if(SearchModel.instance.mapToServer.get("Gender") != null){
            if(!SearchModel.instance.mapToServer.get("Gender").isEmpty()){
                genderBtn.setTextColor(Color.BLUE);
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

        showMapBtn = view.findViewById(R.id.showmap_btn);
        showMapBtn.setOnClickListener(v -> {
            System.out.println(SearchModel.instance.mapToServer);
        });

        searchBtn = view.findViewById(R.id.search_search_btn);
        searchBtn.setOnClickListener(v -> {

            searchBtn.setEnabled(false);
            int count = 0;

            //TODO: check if isEmpty
            if(SearchModel.instance.mapToServer.get("Sizes").isEmpty()){
                // need to: SearchModel.instance.map == all sizes
                SearchModel.instance.mapToServer.put("Sizes", SearchModel.instance.map.get("Sizes"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Categories").isEmpty()){
                SearchModel.instance.mapToServer.put("Categories", SearchModel.instance.map.get("Categories"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Colors").isEmpty()){
                SearchModel.instance.mapToServer.put("Colors", SearchModel.instance.map.get("Colors"));
                count++;
            }if(SearchModel.instance.mapToServer.get("Companies").isEmpty()){
                SearchModel.instance.mapToServer.put("Companies", SearchModel.instance.map.get("Companies"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("BodyTypes").isEmpty()){
                SearchModel.instance.mapToServer.put("BodyTypes", SearchModel.instance.map.get("BodyTypes"));
                count++;
            }
            if(SearchModel.instance.mapToServer.get("Gender").isEmpty()){
                SearchModel.instance.mapToServer.put("Gender", SearchModel.instance.map.get("Gender"));
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

            System.out.println("the map: *************************************************");
            System.out.println(SearchModel.instance.mapToServer);

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



//const getSearchPosts = async (req, res) =>{
//
//        const map = req.body
//
//        const sizes = map.Sizes
//        const categories = map.Categories
//        const colors = map.Colors
//        const companies = map.Companies
//        const bodyTypes = map.BodyTypes
//        const gender = map.Gender
//        const count = map.Count
//        const price = map.Price
//        let priceFrom, priceTo
//
//        console.log("------------------------------------------------------------------")
//        console.log("the body types: " + bodyTypes)
//        console.log("the sizes: " + sizes)
//        console.log("the categories: " + categories)
//        console.log("the companies: " + companies)
//        console.log("the price: " + price)
//        console.log("the gender: " + gender)
//
//        if(price[0] != "false"){
//        priceFrom = parseInt(price[0])
//        }
//        else{
//        priceFrom = price[0] // = false
//        }
//        if(price[1] != "false"){
//        priceTo = parseInt(price[1])
//        }
//        else{
//        priceTo = price[1] // = false
//        }
//
//        // // BodyType
//
//        const profiles = await Profile.find({'bodyType': { $in: bodyTypes}})
//        let profilesId = []
//
//        for(let j = 0; j<profiles.length; j++){
//        profilesId.push(profiles[j].userName)
//        }
//
//        // Sizes:
//
//        let posts
//
//        if(count == "true"){ // it means that no category was choosen - we need to send all the posts.
//        posts = await Post.find({})
//        }
//        else{
//        posts = await Post.find({'size': { $in: sizes}, 'categoryId': { $in: categories}, 'color': { $in: colors}, 'company': { $in: companies}, 'profileId': {$in: profilesId}})
//        // posts = await Post.find({'size': { $in: sizes}})
//        }
//
//        console.log("the size after sizes: " + posts.length)
//
//        //Categories:
//
//        // let posts1 = []
//
//        // for(let i=0; i<posts.length; i++){
//        //     if(categories.includes(posts[i].categoryId)){
//        //         posts1.push(posts[i])
//        //     }
//        // }
//
//        // console.log("the size after sizes and categories: " + posts1.length)
//
//        // // Colors
//
//        // let posts2 = []
//
//        // for(let i=0; i<posts1.length; i++){
//        //     if(colors.includes(posts1[i].color)){
//        //         posts2.push(posts1[i])
//        //     }
//        // }
//
//        // console.log("the size after sizes and categories and colors: " + posts2.length)
//
//        // // Companies
//
//        // let posts3 = []
//
//        // for(let i=0; i<posts2.length; i++){
//        //     if(companies.includes(posts2[i].company)){
//        //         posts3.push(posts2[i])
//        //     }
//        //     else{
//        //         console.log("the post = " + posts[i].profileId + " and " + posts[i].productName )
//        //     }
//        // }
//
//        // console.log("the size after sizes and categories and colors and companies: " + posts3.length)
//
//        // console.log(posts3)
//
//        // let posts4 = []
//
//        // for(let i=0; i<posts3.length; i++){
//        //     if(profilesId.includes(posts3[i].profileId)){
//        //         posts4.push(posts3[i])
//        //     }
//        // }
//
//        // Price
//
//        let postsToSend = []
//
//        if(priceFrom != "false" &&  priceTo != "false"){
//        for(let i=0; i < posts.length; i++){
//        if(posts[i].price >= priceFrom && posts[i].price <= priceTo){
//        postsToSend.push(posts[i])
//        }
//        }
//        }
//        else if(priceFrom == "false" && priceTo != "false"){
//        console.log("we are here now 1")
//        for(let i=0; i < posts.length; i++){
//        if(posts[i].price <= priceTo){
//        postsToSend.push(posts[i])
//        }
//        }
//        }
//        else if(priceFrom != "false" &&  priceTo == "false"){
//        console.log("we are here now 2")
//        for(let i=0; i < posts.length; i++){
//        if(posts[i].price >= priceFrom){
//        postsToSend.push(posts[i])
//        }
//        }
//        }
//        else if(priceFrom == "false" &&  priceTo == "false"){
//        postsToSend = posts
//        }
//
//
//        postsToSend = postsToSend.reverse()
//
//        console.log(postsToSend)
//
//        try {
//        res.status(200).send(postsToSend)
//        } catch (err) {
//        res.status(400).send({
//        'status': 'failure',
//        'error': err.message
//        })
//        }
//        }
