package com.example.perfectfitapp_android.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class Post {

    // TODO: need to fix postId
    String profileId, productName, SKU, size, company, color, categoryId, subCategoryId;
    String description,date, link, sizeAdjustment, rating;
    String postId;
    String price;
    ArrayList<String> likes, picturesUrl;
    ArrayList<String> comments;
    Long updateDate = new Long(0);

    /*--------------------------------- Getters & Setters -------------------------------*/

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /*------------------------------------------------------*/

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /*------------------------------------------------------*/

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    /*------------------------------------------------------*/

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /*------------------------------------------------------*/

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /*------------------------------------------------------*/

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /*------------------------------------------------------*/

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /*------------------------------------------------------*/

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /*------------------------------------------------------*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*------------------------------------------------------*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*------------------------------------------------------*/

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /*------------------------------------------------------*/

    public String getSizeAdjustment() {
        return sizeAdjustment;
    }

    public void setSizeAdjustment(String sizeAdjustment) {
        this.sizeAdjustment = sizeAdjustment;
    }

    /*------------------------------------------------------*/

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getPicturesUrl() {
        return picturesUrl;
    }

    public void setPicturesUrl(ArrayList<String> picturesUrl) {
        this.picturesUrl = picturesUrl;
    }

    /*------------------------------------------------------*/

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    /*------------------------------------------------------*/

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    /*------------------------------------------------------*/

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    /*--------------------------------- Constructors -------------------------------*/

    public Post(){
        //TODO: postId
        this.postId = "";

        this.profileId = "";
        this.productName = "";
        this.SKU = "";
        this.size = "";
        this.company = "";
        this.price = "0";
        this.color = "";
        this.categoryId = "";
        this.subCategoryId = "";
        this.description = "";
        this.date = "";
        this.link = "";
        this.sizeAdjustment = "";
        this.rating = "";
        this.picturesUrl = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(String postId, String profileId, String productName, String sku, String size, String company,
                String color, String categoryId, String subCategoryId, String description,
                String date, String link, String sizeAdjustment, String rating,
                String price) {

        //TODO: postId
        this.postId = postId;
        this.profileId = profileId;
        this.productName = productName;
        this.SKU = sku;
        this.size = size;
        this.company = company;
        this.price = price;
        this.color = color;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.date = date;
        this.link = link;
        this.sizeAdjustment = sizeAdjustment;
        this.rating = rating;
        this.picturesUrl = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(String postId, String profileId, String productName, String sku, String size, String company,
                String color, String categoryId, String subCategoryId, String description,
                String date, String link, String sizeAdjustment, String rating, ArrayList<String> picturesUrl,
                String price, ArrayList<String> likes, ArrayList<String> comments) {

        //TODO: postId, likes, comments
        this.postId = postId;
        this.profileId = profileId;
        this.productName = productName;
        this.SKU = sku;
        this.size = size;
        this.company = company;
        this.price = price;
        this.color = color;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.date = date;
        this.link = link;
        this.sizeAdjustment = sizeAdjustment;
        this.rating = rating;
        this.picturesUrl = picturesUrl;
        this.likes = likes;
        this.comments = comments;
    }

/************************************ Json ************************************/

    public static Post fromJson(Map<String, Object> json) {
        String postId = (String) json.get("_id").toString();
        String profileId = (String) json.get("profileId");
        String productName = (String) json.get("productName");
        String sku = (String) json.get("sku");
        String size = (String) json.get("size");
        String company = (String) json.get("company");
        String price = (String) json.get("price");
        String color = (String) json.get("color");
        String categoryId = (String) json.get("categoryId");
        String subCategoryId = (String) json.get("subCategoryId");
        String description = (String) json.get("description");
        String date = (String) json.get("date");
        String link = (String) json.get("link");
        String sizeAdjustment = (String) json.get("sizeAdjustment");
        String rating = (String) json.get("rating");
        ArrayList<String> picturesUrl = (ArrayList<String>) json.get("picturesUrl");
        ArrayList<String> likes = (ArrayList<String>) json.get("likes");
        ArrayList<String> comments = (ArrayList<String>) json.get("comments");

//        Timestamp ts = (Timestamp) json.get("updateDate");
//        Long updateDate = ts.getSeconds();

//        Post post = new Post(id, dishName, restaurant, address, category, description,
//                review, image, rate, userEmail, display);
//        post.setUpdateDate(updateDate);

       Post post = new Post(postId, profileId, productName, sku, size, company, color, categoryId, subCategoryId,
               description, date, link, sizeAdjustment, rating, picturesUrl, price, likes, comments);

        return post;
    }

    public static List<Post> getPostsFromJson(List<Map<String, Object>> map){
        ArrayList<Post> list = new ArrayList<>();
        for (Map<String, Object> a: map) {

           Post post =  fromJson(a);
           list.add(post);

        }
        return list;
    }

    /*------------------------------------------------------*/

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("postId",postId );
        json.put("profileId", profileId);
        json.put("productName", productName);
        json.put("sku", SKU);
        json.put("size", size);
        json.put("company", company);
        json.put("price", price);
        json.put("color", color);
        json.put("categoryId", categoryId);
        json.put("subCategoryId", subCategoryId);
        json.put("description", description);
        json.put("date", date);
        json.put("link", link);
        json.put("sizeAdjustment", sizeAdjustment);
        json.put("rating", rating);
        json.put("picturesUrl", picturesUrl);
        json.put("date", date);
        json.put("likes", likes);
        json.put("comments", comments);

        Date currentTime = Calendar.getInstance().getTime();

        System.out.println(currentTime);
        json.put("updateDate", currentTime);

        return json;
    }

    public static Post jsonObjectToPost(JsonObject postsJson) {
        String id = postsJson.get("_id").getAsString();
        String profileId = postsJson.get("profileId").getAsString();
        String productName = postsJson.get("productName").getAsString();
        String sku = postsJson.get("sku").getAsString();
        String size = postsJson.get("size").getAsString();
        String company = postsJson.get("company").getAsString();
        String price = postsJson.get("price").getAsString();
        String color = postsJson.get("color").getAsString();
        String categoryId = postsJson.get("categoryId").getAsString();
        String subCategoryId = postsJson.get("subCategoryId").getAsString();
        String description = postsJson.get("description").getAsString();
        String date = postsJson.get("date").getAsString();
        String link = postsJson.get("link").getAsString();
        String sizeAdjustment = postsJson.get("sizeAdjustment").getAsString();
        String rating = postsJson.get("rating").getAsString();

        JsonElement picturesJson = postsJson.get("picturesUrl");
        ArrayList<String> picturesUrl = new ArrayList<>();
        if(!picturesJson.toString().equals("null") || !picturesJson.isJsonNull()){
            for (JsonElement pic : picturesJson.getAsJsonArray()) {
                picturesUrl.add(pic.getAsString());
            }
        }

        JsonElement likesJson = postsJson.get("likes");
        ArrayList<String> likes = new ArrayList<>();
        if(!likesJson.toString().equals("null") || !likesJson.isJsonNull()){
            for (JsonElement like : likesJson.getAsJsonArray()) {
                likes.add(like.getAsString());
            }
        }

        JsonElement commentsJson = postsJson.get("comments");
        ArrayList<String> comments = new ArrayList<>();
        if(!commentsJson.toString().equals("null") || !commentsJson.isJsonNull()){
            for (JsonElement comment : commentsJson.getAsJsonArray()) {
                comments.add(comment.getAsString());
            }
        }

        Post post = new Post(id, profileId, productName, sku, size, company,
                color, categoryId, subCategoryId, description,
                date, link, sizeAdjustment, rating, picturesUrl,
                price, likes, comments);
        return post;
    }

    public static Post jsonElementToPost(JsonElement postsJson){
        String id = postsJson.getAsJsonObject().get("_id").getAsString();
        String profileId = postsJson.getAsJsonObject().get("profileId").getAsString();
        String productName = postsJson.getAsJsonObject().get("productName").getAsString();
        String sku = postsJson.getAsJsonObject().get("sku").getAsString();
        String size = postsJson.getAsJsonObject().get("size").getAsString();
        String company = postsJson.getAsJsonObject().get("company").getAsString();
        String price = postsJson.getAsJsonObject().get("price").getAsString();
        String color = postsJson.getAsJsonObject().get("color").getAsString();
        String categoryId = postsJson.getAsJsonObject().get("categoryId").getAsString();
        String subCategoryId = postsJson.getAsJsonObject().get("subCategoryId").getAsString();
        String description = postsJson.getAsJsonObject().get("description").getAsString();
        String date = postsJson.getAsJsonObject().get("date").getAsString();
        String link = postsJson.getAsJsonObject().get("link").getAsString();
        String sizeAdjustment = postsJson.getAsJsonObject().get("sizeAdjustment").getAsString();
        String rating = postsJson.getAsJsonObject().get("rating").getAsString();

        JsonElement picturesJson = postsJson.getAsJsonObject().get("picturesUrl");
        ArrayList<String> picturesUrl = new ArrayList<>();
        if(!picturesJson.toString().equals("null") || !picturesJson.isJsonNull()){
            for (JsonElement pic : picturesJson.getAsJsonArray()) {
                picturesUrl.add(pic.getAsString());
            }
        }

        JsonElement likesJson = postsJson.getAsJsonObject().get("likes");
        ArrayList<String> likes = new ArrayList<>();
        if(!likesJson.toString().equals("null") || !likesJson.isJsonNull()){
            for (JsonElement like : likesJson.getAsJsonArray()) {
                likes.add(like.getAsString());
            }
        }

        JsonElement commentsJson = postsJson.getAsJsonObject().get("comments");
        ArrayList<String> comments = new ArrayList<>();
        if(!commentsJson.toString().equals("null") || !commentsJson.isJsonNull()){
            for (JsonElement comment : commentsJson.getAsJsonArray()) {
                comments.add(comment.getAsString());
            }
        }

        Post post = new Post(id, profileId, productName, sku, size, company,
                color, categoryId, subCategoryId, description,
                date, link, sizeAdjustment, rating, picturesUrl,
                price, likes, comments);
        return post;
    }

    public static List<Post> jsonArrayToPost(JsonArray postsJson){
        List<Post> postsList = new ArrayList<>();
        for (int i = 0; i < postsJson.size(); i++) {
            postsList.add(Post.jsonElementToPost(postsJson.get(i)));
        }
        return postsList;
    }


    //TODO: ...
    public Long getUpdateDate() {
        return updateDate;
    }
}
