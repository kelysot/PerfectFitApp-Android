package com.example.perfectfitapp_android.search;

import android.app.AlertDialog;

import androidx.navigation.Navigation;

import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.User;
import com.example.perfectfitapp_android.model.generalModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.collections.ArrayDeque;

public class SearchModel {

    List<Post> list;
    HashMap<String, List<String>> mapToServer;

    public static final SearchModel instance = new SearchModel();

    public SearchModel(){
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
