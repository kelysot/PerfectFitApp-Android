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
    String pictureUrl;
    ArrayList<String> posts;

    public SubCategory(String subCategoryId, String categoryId, String name, String pictureUrl, ArrayList<String> postsArray) {
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.posts = postsArray;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    /*------------------------------------------------------*/

    @Override
    public String toString() {
        return "SubCategory{" +
                "subCategoryId='" + subCategoryId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", posts=" + posts +
                '}';
    }

    public static SubCategory jsonElementToSubCategory(JsonElement subCategoriesJson){
        String subCategoryId = subCategoriesJson.getAsJsonObject().get("_id").getAsString();
        String categoryId = subCategoriesJson.getAsJsonObject().get("categoryId").getAsString();
        String name = subCategoriesJson.getAsJsonObject().get("name").getAsString();
        String pictureUrl = subCategoriesJson.getAsJsonObject().get("pictureUrl").getAsString();

        JsonElement postsJson = subCategoriesJson.getAsJsonObject().get("posts");
        ArrayList<String> postsArray = new ArrayList<>();
        if(!postsJson.toString().equals("null") || !postsJson.isJsonNull()){
            for (JsonElement similarProfile : postsJson.getAsJsonArray()) {
                postsArray.add(similarProfile.getAsString());
            }
        }

        SubCategory subCategory = new SubCategory(subCategoryId, categoryId, name, pictureUrl, postsArray);
        return subCategory;
    }

    public HashMap<String,Object> toJson(){

        HashMap<String,Object> map = new HashMap<>();
        map.put("subCategoryId",this.subCategoryId);
        map.put("categoryId",this.categoryId);
        map.put("name",this.name);
        map.put("pictureUrl",this.pictureUrl);
        map.put("posts",this.getPosts());

        return map;
    }

    public static List<SubCategory> jsonArrayToSubCategory(JsonArray subCategoriesJson){
        List<SubCategory> subCategoryList = new ArrayList<>();
        for (int i=0;i<subCategoriesJson.size();i++){
            subCategoryList.add(SubCategory.jsonElementToSubCategory(subCategoriesJson.get(i)));
        }
        return subCategoryList;
    }
}
