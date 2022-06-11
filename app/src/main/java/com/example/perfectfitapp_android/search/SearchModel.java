package com.example.perfectfitapp_android.search;

import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchModel {

    List<Post> list;
    HashMap<String, List<String>> mapToServer;

    public static final SearchModel instance = new SearchModel();

    public SearchModel() {
        list = new ArrayList<>();
        mapToServer = new HashMap<>();
        mapToServer.put("Categories", new ArrayList<>());
        mapToServer.put("Sizes", new ArrayList<>());
        mapToServer.put("Companies", new ArrayList<>());
        mapToServer.put("Colors", new ArrayList<>());
        mapToServer.put("BodyTypes", new ArrayList<>());
        mapToServer.put("Count", new ArrayList<>());
        mapToServer.put("Price", new ArrayList<>());
        mapToServer.put("Gender", new ArrayList<>());
    }
}
