package com.example.perfectfitapp_android.model;

import java.util.ArrayList;

public class Post {

    // TODO: need to fix postId
    String profileId, productName, SKU, size, company, color, categoryId, subCategoryId;
    String description,date, link, sizeAdjustment, rating, picturesUrl;
    String postId;
    int price;
    ArrayList<Integer> likes;
    ArrayList<Comment> comments;

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

    public String getPicturesUrl() {
        return picturesUrl;
    }

    public void setPicturesUrl(String picturesUrl) {
        this.picturesUrl = picturesUrl;
    }

    /*------------------------------------------------------*/

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /*------------------------------------------------------*/

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Integer> likes) {
        this.likes = likes;
    }

    /*------------------------------------------------------*/

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
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
        this.color = "";
        this.categoryId = "";
        this.subCategoryId = "";
        this.description = "";
        this.date = "";
        this.link = "";
        this.sizeAdjustment = "";
        this.rating = "";
        this.picturesUrl = "";
        this.price = 0;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(String postId, String profileId, String productName, String sku, String size, String company,
                String color, String categoryId, String subCategoryId, String description,
                String date, String link, String sizeAdjustment, String rating, String picturesUrl,
                int price) {

        //TODO: postId
        this.postId = postId;

        this.profileId = profileId;
        this.productName = productName;
        this.SKU = sku;
        this.size = size;
        this.company = company;
        this.color = color;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.date = date;
        this.link = link;
        this.sizeAdjustment = sizeAdjustment;
        this.rating = rating;
        this.picturesUrl = picturesUrl;
        this.price = price;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}
