package com.example.perfectfitapp_android.search;

import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kotlin.collections.ArrayDeque;

public class SearchModel {

    HashMap<String, List<String>> map;
    List<Post> list;
    HashMap<String, List<String>> mapToServer;

    public static final SearchModel instance = new SearchModel();

    public SearchModel(){
        map = new HashMap<>();
        map.put("Categories", new ArrayList<>());
        map.put("Sizes", new ArrayList<>());
        map.put("Companies", new ArrayList<>());
        map.put("Colors", new ArrayList<>());
        map.put("BodyTypes", new ArrayList<>());
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("Female");
        genderList.add("Male");
        map.put("Gender", genderList);
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
