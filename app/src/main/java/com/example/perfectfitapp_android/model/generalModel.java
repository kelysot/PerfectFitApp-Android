package com.example.perfectfitapp_android.model;

import android.widget.TextView;

import com.example.perfectfitapp_android.search.SearchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class generalModel {


    public HashMap<String, List<String>> map;

    public static final generalModel instance = new generalModel();

    public generalModel() {
        map = new HashMap<>();
        map.put("Categories", new ArrayList<>());
        map.put("Sizes", new ArrayList<>());
        map.put("Companies", new ArrayList<>());
        map.put("Colors", new ArrayList<>());
        map.put("BodyTypes", new ArrayList<>());
        map.put("BodyTypesDescription", new ArrayList<>());
        map.put("Gender", new ArrayList<>());
    }
}
