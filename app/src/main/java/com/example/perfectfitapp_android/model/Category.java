package com.example.perfectfitapp_android.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category {

    String categoryId;
    String name;
    String pictureUrl;
    ArrayList<String> womenSubCategory;
    ArrayList<String> menSubCategory;

    public Category(String categoryId, String name, String pictureUrl, ArrayList<String> womenSubCategory, ArrayList<String> menSubCategory) {
        this.categoryId = categoryId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.womenSubCategory = womenSubCategory;
        this.menSubCategory = menSubCategory;
    }

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /*------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*------------------------------------------------------*/

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getWomenSubCategory() {
        return womenSubCategory;
    }

    /*------------------------------------------------------*/

    public void setWomenSubCategory(ArrayList<String> womenSubCategory) {
        this.womenSubCategory = womenSubCategory;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getMenSubCategory() {
        return menSubCategory;
    }

    public void setMenSubCategory(ArrayList<String> menSubCategory) {
        this.menSubCategory = menSubCategory;
    }

    /*------------------------------------------------------*/

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", womenSubCategory=" + womenSubCategory +
                ", menSubCategory=" + menSubCategory +
                '}';
    }

    public static Category jsonObjectToCategory(JsonElement categoriesJson){
        String categoryId = categoriesJson.getAsJsonObject().get("_id").getAsString();
        String name = categoriesJson.getAsJsonObject().get("name").getAsString();
        String pictureUrl = categoriesJson.getAsJsonObject().get("pictureUrl").getAsString();


        JsonElement womenSubCategoryJson = categoriesJson.getAsJsonObject().get("womenSubCategory");
        ArrayList<String> womenSubCategory = new ArrayList<>();
        if(!womenSubCategoryJson.toString().equals("null") || !womenSubCategoryJson.isJsonNull()){
            for (JsonElement similarProfile : womenSubCategoryJson.getAsJsonArray()) {
                womenSubCategory.add(similarProfile.getAsString());
            }
        }

        JsonElement menSubCategoryJson = categoriesJson.getAsJsonObject().get("menSubCategory");
        ArrayList<String> menSubCategory = new ArrayList<>();
        if(!menSubCategoryJson.toString().equals("null") || !menSubCategoryJson.isJsonNull()){
            for (JsonElement similarProfile : menSubCategoryJson.getAsJsonArray()) {
                menSubCategory.add(similarProfile.getAsString());
            }
        }

        Category category = new Category(categoryId, name, pictureUrl, womenSubCategory, menSubCategory);

        return category;
    }

    public HashMap<String, Object> toJson(){

        HashMap<String, Object> map = new HashMap<>();

        map.put("categoryId", this.getCategoryId());
        map.put("name", this.getName());
        map.put("pictureUrl", this.getPictureUrl());
        map.put("womenSubCategory", this.getWomenSubCategory());
        map.put("menSubCategory", this.getMenSubCategory());

        return map;
    }

    public static List<Category> jsonArrayToCategory(JsonArray categoriesJson){
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < categoriesJson.size(); i++) {
            categoryList.add(Category.jsonObjectToCategory(categoriesJson.get(i)));
        }
        Log.d("TAG12", categoryList.get(0).name +"111111");
        return categoryList;
    }

}
