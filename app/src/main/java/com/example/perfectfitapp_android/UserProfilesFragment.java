package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfilesFragment extends Fragment {

    ImageButton addProfile;
    Button homepageBtn;
    Button user1Btn, user2Btn, user3Btn, user4Btn, user5Btn;
    ArrayList<Button> buttonList;
    Model model;
    String LongClickUserName;
    int posInArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);

        model = Model.instance;

        addProfile = view.findViewById(R.id.user_profiles_addprofile_btn);
        addProfile.setOnClickListener(v-> addProfile(view));

        homepageBtn = view.findViewById(R.id.move_to_home_page);

        buttonList = new ArrayList<>();

        user1Btn = view.findViewById(R.id.user_profiles_profile1_btn);
        user2Btn = view.findViewById(R.id.user_profiles_profile2_btn);
        user3Btn = view.findViewById(R.id.user_profiles_profile3_btn);
        user4Btn = view.findViewById(R.id.user_profiles_profile4_btn);
        user5Btn = view.findViewById(R.id.user_profiles_profile5_btn);

        buttonList.add(user1Btn);
        buttonList.add(user2Btn);
        buttonList.add(user3Btn);
        buttonList.add(user4Btn);
        buttonList.add(user5Btn);

        for(int j=0; j<buttonList.size(); j++){
            buttonList.get(j).setVisibility(View.GONE);
            registerForContextMenu(buttonList.get(j));
        }

        for(int i=0; i < Model.instance.getUser().getProfilesArray().size(); i++){
            buttonList.get(i).setVisibility(View.VISIBLE);
            buttonList.get(i).setText(Model.instance.getUser().getProfilesArray().get(i));
            int finalI = i;
            buttonList.get(i).setOnClickListener(v-> moveToHomePageWithProfile(view, model.getUser().getProfilesArray().get(finalI)));
            buttonList.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LongClickUserName = Model.instance.getUser().getProfilesArray().get(finalI);
                    posInArray = finalI;
                    return false;
                }
            });
        }

        return view;
    }


    private void moveToHomePageWithProfile(View view, String userName) {

        Model.instance.getProfileFromServer(model.getUser().getEmail(), userName, profile -> {
            if(profile != null){
                model.setProfile(profile);
                profile.setStatus("true");
                Model.instance.editProfile(profile, new Model.EditProfile() {
                    @Override
                    public void onComplete(Boolean isSuccess) {
                        if(isSuccess){
                            Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
                        }
                        else{
                            Log.d("TAG", "failed in UserProfileFragment 1");
                            Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else{
                Log.d("TAG", "failed in UserProfileFragment 1");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addProfile(View view) {

        //TODO: open dialog about the amount of profiles

        if( Model.instance.getUser().getProfilesArray().size() == 5){
            Toast.makeText(MyApplication.getContext(), "Sorry, you can only have 5 profiles",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment_to_registerFragment2);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
    }

    //TODO: Add refresh to profile array + change delete case after the refresh will work
    //TODO: Edit functionality + move edit profile page + crate the fragment

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile_edit_menuItem:{
                return true;
            }
            case R.id.profile_delete_menuItem:{
                Model.instance.deleteProfile(LongClickUserName,isSuccess -> {
                    if(isSuccess){
                        Model.instance.getUser().getProfilesArray().remove(posInArray); //current user
                        buttonList.get(posInArray).setVisibility(View.GONE);
                        buttonList.remove(posInArray);
                    }else{
                        Log.d("TAG","not work");
                    }
                });
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }
}