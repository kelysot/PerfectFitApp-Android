package com.example.perfectfitapp_android.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comment {

    String commentId, profileId, postId, date, text;

    public Comment() {
        this.commentId = "";
        this.profileId = "";
        this.postId = "";
        this.date = "";
        this.text = "";
    }

    public Comment(String commentId, String profileId, String postId, String date, String text) {
        this.commentId = commentId;
        this.profileId = profileId;
        this.postId = postId;
        this.date = date;
        this.text = text;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", postId='" + postId + '\'' +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("_id", commentId);
        json.put("profileId", profileId);
        json.put("postId", postId);
        json.put("date", date);
        json.put("text", text);

        return json;
    }

    public static Comment jsonElementToComment(JsonElement commentJson) {
        String id = commentJson.getAsJsonObject().get("_id").getAsString();
        String profileId = commentJson.getAsJsonObject().get("profileId").getAsString();
        String postId = commentJson.getAsJsonObject().get("postId").getAsString();
        String date = commentJson.getAsJsonObject().get("date").getAsString();
        String text = commentJson.getAsJsonObject().get("text").getAsString();

        Comment comment = new Comment(id, profileId, postId, date, text);

        return comment;
    }

    public static Comment jsonObjectToComment(JsonElement commentsJson) {
        String commentId = commentsJson.getAsJsonObject().get("_id").getAsString();
        String profileId = commentsJson.getAsJsonObject().get("profileId").getAsString();
        String postId = commentsJson.getAsJsonObject().get("postId").getAsString();
        String date = commentsJson.getAsJsonObject().get("date").getAsString();
        String text = commentsJson.getAsJsonObject().get("text").getAsString();

        Comment comment = new Comment(commentId, profileId, postId, date, text);

        return comment;
    }

    public static List<Comment> jsonArrayToCategory(JsonArray commentsJson) {
        List<Comment> commentsList = new ArrayList<>();
        for (int i = 0; i < commentsJson.size(); i++) {
            commentsList.add(Comment.jsonObjectToComment(commentsJson.get(i)));
        }
        return commentsList;
    }
}
