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
    String gender;
    ArrayList<String> subCategory;

    public Category(String categoryId, String name, String pictureUrl, String gender, ArrayList<String> subCategory) {
        this.categoryId = categoryId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.gender = gender;
        this.subCategory = subCategory;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ArrayList<String> subCategory) {
        this.subCategory = subCategory;
    }

    /*------------------------------------------------------*/

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", subCategory=" + subCategory +
                '}';
    }

    public static Category jsonObjectToCategory(JsonElement categoriesJson){
        String categoryId = categoriesJson.getAsJsonObject().get("_id").getAsString();
        String name = categoriesJson.getAsJsonObject().get("name").getAsString();
        String pictureUrl = categoriesJson.getAsJsonObject().get("pictureUrl").getAsString();
        String gender = categoriesJson.getAsJsonObject().get("gender").getAsString();


        JsonElement subCategoryJson = categoriesJson.getAsJsonObject().get("subCategory");
        ArrayList<String> subCategory = new ArrayList<>();
        if(!subCategoryJson.toString().equals("null") || !subCategoryJson.isJsonNull()){
            for (JsonElement similarProfile : subCategoryJson.getAsJsonArray()) {
                subCategory.add(similarProfile.getAsString());
            }
        }

        Category category = new Category(categoryId, name, pictureUrl, gender, subCategory);

        return category;
    }

    public HashMap<String, Object> toJson(){

        HashMap<String, Object> map = new HashMap<>();

        map.put("categoryId", this.getCategoryId());
        map.put("name", this.getName());
        map.put("pictureUrl", this.getPictureUrl());
        map.put("gender", this.getGender());
        map.put("subCategory", this.getSubCategory());

        return map;
    }

    public static List<Category> jsonArrayToCategory(JsonArray categoriesJson){
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < categoriesJson.size(); i++) {
            categoryList.add(Category.jsonObjectToCategory(categoriesJson.get(i)));
        }
        return categoryList;
    }

}
