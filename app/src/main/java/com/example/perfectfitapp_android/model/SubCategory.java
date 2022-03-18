package com.example.perfectfitapp_android.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SubCategory {

    String subCategoryId;
    String categoryId;
    String name;

    public SubCategory(String subCategoryId, String categoryId, String name) {
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
        this.name = name;
    }

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /*------------------------------------------------------*/

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


    public static SubCategory jsonObjectToSubCategory(JsonElement subCategoriesJson){
        String subCategoryId = subCategoriesJson.getAsJsonObject().get("_id").getAsString();
        String categoryId = subCategoriesJson.getAsJsonObject().get("categoryId").getAsString();
        String name = subCategoriesJson.getAsJsonObject().get("name").getAsString();

        SubCategory subCategory = new SubCategory(subCategoryId,categoryId,name);
        return subCategory;
    }

    public HashMap<String,Object> toJson(){

        HashMap<String,Object> map = new HashMap<>();
        map.put("subCategoryId",this.subCategoryId);
        map.put("categoryId",this.categoryId);
        map.put("name",this.name);

        return map;
    }

    public static List<SubCategory> jsonArrayToSubCategory(JsonArray subCategoriesJson){
        List<SubCategory> subCategoryList = new ArrayList<>();
        for (int i=0;i<subCategoriesJson.size();i++){
            subCategoryList.add(SubCategory.jsonObjectToSubCategory(subCategoriesJson.get(i)));
        }
        return subCategoryList;
    }
}
