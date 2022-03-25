package com.example.perfectfitapp_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.perfectfitapp_android.model.Model;

import java.sql.Time;
import java.util.ArrayList;

import okio.Timeout;


public class UserProfilesFragment extends Fragment {

    ImageButton addProfile;
    Button homepageBtn;
    Button user1Btn, user2Btn, user3Btn, user4Btn, user5Btn;
    ArrayList<Button> buttonList;
    Model model;
    String longClickUserName;
    int posInArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);

        model = Model.instance;

        addProfile = view.findViewById(R.id.user_profiles_addprofile_btn);
        addProfile.setOnClickListener(v-> addProfile(view));

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

        setButtons();

        return view;
    }

    public void setButtonsEnable(boolean flag){
        if(flag){
            for (Button b: buttonList) {
                b.setEnabled(true);
            }
        }
        else{
            for (Button b: buttonList) {
                b.setEnabled(false);
            }
        }
    }

    public void setButtons(){

        setButtonsEnable(true);

        for(int j=0; j<buttonList.size(); j++){
            buttonList.get(j).setVisibility(View.GONE);
            registerForContextMenu(buttonList.get(j));
        }

        for(int i=0; i < Model.instance.getUser().getProfilesArray().size(); i++){
            buttonList.get(i).setVisibility(View.VISIBLE);
            buttonList.get(i).setText(Model.instance.getUser().getProfilesArray().get(i));
            int finalI = i;
            buttonList.get(i).setOnClickListener(v-> moveToHomePageWithProfile(model.getUser().getProfilesArray().get(finalI)));
            // addProfile instead of view
            buttonList.get(i).setOnLongClickListener(v -> {
                editProfileByLongClick(finalI);
                return false;
            });
        }
    }


    private void moveToHomePageWithProfile(String userName) {

        setButtonsEnable(false);
        if(!Model.instance.getProfile().getUserName().isEmpty()){
            Model.instance.getProfile().setStatus("false");
            Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                if(isSuccess){
                    changeProfile(userName);
                }
                else{
                    // TODO
                    Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                            Toast.LENGTH_LONG).show();
                    Log.d("TAG", "failed in UserProfile in editProfile - change status to false");
                    setButtonsEnable(true);
                }
            });
        }
        else{
            changeProfile(userName);
        }
    }


    public void changeProfile(String userName){

        Model.instance.getProfileFromServer(model.getUser().getEmail(), userName, profile -> {
            if(profile != null){
                model.setProfile(profile);
                profile.setStatus("true");
                Model.instance.editProfile(null,profile, new Model.EditProfile() {
                    @Override
                    public void onComplete(Boolean isSuccess) {
                        if(isSuccess){
                            Navigation.findNavController(addProfile).navigate(R.id.action_userProfilesFragment_to_homePageFragment);
                        }
                        else{
                            Log.d("TAG", "failed in UserProfileFragment 1");
                            Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                    Toast.LENGTH_LONG).show();
                            setButtonsEnable(true);
                        }
                    }
                });
            }
            else{
                Log.d("TAG", "failed in UserProfileFragment 1");
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
                setButtonsEnable(true);
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

    public void editProfileByLongClick(int finalI){
        longClickUserName = Model.instance.getUser().getProfilesArray().get(finalI);
        posInArray = finalI;
        Model.instance.getProfileFromServer(Model.instance.getUser().getEmail(),longClickUserName,profile -> {
            Model.instance.setProfile(profile);
        });
    }

    //TODO: Add refresh to profile array + change delete case after the refresh will work
    //TODO: Edit functionality + move edit profile page + crate the fragment

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile_edit_menuItem:{
                Navigation.findNavController(addProfile).navigate(UserProfilesFragmentDirections.actionUserProfilesFragmentToEditProfileFragment2());
                return true;
            }
            case R.id.profile_delete_menuItem:{
                Model.instance.deleteProfile(longClickUserName,isSuccess -> {
                    if(isSuccess){
                        Model.instance.getUser().getProfilesArray().remove(posInArray); //current user
//                        buttonList.get(posInArray).setVisibility(View.GONE);
//                        buttonList.remove(posInArray);
                        setButtons();

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