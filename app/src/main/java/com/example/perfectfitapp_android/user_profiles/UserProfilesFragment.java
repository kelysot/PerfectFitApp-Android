package com.example.perfectfitapp_android.user_profiles;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.UserProfilesGraphDirections;
import com.example.perfectfitapp_android.category.CategoryFragmentDirections;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Profile;
import com.example.perfectfitapp_android.model.SubCategory;
import com.example.perfectfitapp_android.model.generalModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okio.Timeout;
import pl.droidsonroids.gif.GifImageButton;


public class UserProfilesFragment extends Fragment {

    Button user1Btn, user2Btn, user3Btn, user4Btn, user5Btn;
    ArrayList<Button> buttonList;
    Model model;
    String longClickUserName;
    ProgressBar progressBar;
    TextView text;
    int posInArray;
    GifImageButton greenGif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);

        model = Model.instance;


        /************* initialize general *************/
        Model.instance.getGeneral(map -> {
            if(map != null){
                generalModel.instance.map = map;
            }
        });

        /************* initilize categories *************/
        Model.instance.setCategories(new LinkedList<>());
        Model.instance.setSubCategories(new LinkedList<>());
        Model.instance.setCategoriesAndSubCategories(new HashMap<>());

        progressBar = view.findViewById(R.id.user_profiles_progress_bar);
        progressBar.setVisibility(View.GONE);

        text = view.findViewById(R.id.user_ptofiles_text);
        greenGif = view.findViewById(R.id.gif_green);
        greenGif.setOnClickListener(v -> {
            addProfile(view);
        });

        text.setEnabled(false);
        //TODO: ADD animation of legs lead to the addProfile button
        if(Model.instance.getUser().getProfilesArray().size() > 0){
            text.setVisibility(View.GONE);
            greenGif.setVisibility(View.GONE);
            greenGif.setEnabled(false);
        }


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
        setHasOptionsMenu(true);

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
        progressBar.setVisibility(View.VISIBLE);
        if(!Model.instance.getProfile().getUserName().isEmpty()){
            if(buttonList.contains(Model.instance.getProfile().getUserName())){
                Model.instance.getProfile().setStatus("false");
                System.out.println("the profile: " + Model.instance.getProfile());
                System.out.println("----------------" + Model.instance.getProfile().getUserName());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if(isSuccess){
                        changeProfile(userName);
                    }
                    else{
                        // TODO dialog
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                        Log.d("TAG", "failed in UserProfile in editProfile - change status to false");
                        setButtonsEnable(true);
                    }
                });
            }
            else{ // no need to change the profile because it's deleted.
                changeProfile(userName);
            }
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
                Model.instance.editProfile(null,profile, isSuccess -> {
                    if(isSuccess){
                        Model.instance.getAllCategoriesListener(categoryList -> {
                            for(int i = 0; i< categoryList.size(); i++){
                                int finalI = i;
                                Model.instance.getSubCategoriesByCategoryId(categoryList.get(i).getCategoryId(), profile.getGender(), new Model.GetSubCategoriesByCategoryIdListener() {
                                    @Override
                                    public void onComplete(List<SubCategory> subCategoryList) {
                                        ArrayList<String> subCategoryNames = new ArrayList<>();
                                        for(int j = 0; j < subCategoryList.size(); j++){
                                            subCategoryNames.add(subCategoryList.get(j).getName());
                                        }
                                        Model.instance.putCategoriesAndSubCategories(categoryList.get(finalI).getName(), subCategoryNames);

                                    }
                                });
                            }
                            Model.instance.setCategories(categoryList);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        });
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        //TODO: dialog
                        Log.d("TAG", "failed in UserProfileFragment 1");
                        Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                                Toast.LENGTH_LONG).show();
                        setButtonsEnable(true);
                    }
                });
            }
            else{
                progressBar.setVisibility(View.GONE);
                setButtonsEnable(true);
//                Log.d("TAG", "failed in UserProfileFragment 1");
//                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
//                        Toast.LENGTH_LONG).show();
//                setButtonsEnable(true);
//                progressBar.setVisibility(View.GONE);
//
//                System.out.println("maybe forbidden - need to check it");
//                startActivity(new Intent(getContext(), LoginActivity.class));
//                getActivity().finish();
            }
        });
    }

    private void addProfile(View view) {

        greenGif.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        //TODO: open dialog about the amount of profiles

        if( Model.instance.getUser().getProfilesArray().size() == 5){
            showOkDialog();
            progressBar.setVisibility(View.GONE);
        }
        else{
            Navigation.findNavController(view).navigate(R.id.action_userProfilesFragment2_to_createProfileStep1Fragment2);
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
            if(profile != null){
                Model.instance.setProfile(profile);
            }
            else{
                //TODO: dialog
            }
        });
    }

    //TODO: Add refresh to profile array + change delete case after the refresh will work
    //TODO: Edit functionality + move edit profile page + crate the fragment

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile_edit_menuItem:{
                // TODO: add the edit profile here
                progressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(this.getView()).navigate(R.id.action_userProfilesFragment2_to_editProfileFragment2);
                return true;
            }
            case R.id.profile_delete_menuItem:{
                String s = "Are you sure you want to delete " + longClickUserName + " profile?";
                showDialog(s);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void delete() {
        //TODO: check delete
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.deleteProfile(longClickUserName,isSuccess -> {
            if(isSuccess){
                Model.instance.getUser().getProfilesArray().remove(posInArray); //current user
//                        buttonList.get(posInArray).setVisibility(View.GONE);
//                        buttonList.remove(posInArray);
                setButtons();
                progressBar.setVisibility(View.GONE);
                Navigation.findNavController(this.getView()).navigate(R.id.action_global_userProfilesFragment2);

            }else{
                //TODO: dialog
                Log.d("TAG","not work");
            }
        });
    }

    private void showDialog(String text){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            delete();
            dialog.dismiss();
        });

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void showOkDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText("Sorry, you can only have 5 profiles.");

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }


    /* *************************************** Menu Functions *************************************** */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_profiles_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.CreateProfileStep1Fragment) {
            addProfile(this.getView());
            return true;
        }
        else if(item.getItemId() == R.id.EditUserFragment){
            NavHostFragment.findNavController(this).navigate(UserProfilesFragmentDirections.actionUserProfilesFragment2ToEditUserFragment());
            return true;
        }
        else if(item.getItemId() == R.id.logout){
            String s = "Do you want to log out of PerfectFit?";
            showDialogLogout(s);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Model.instance.logout(isSuccess -> {
            if(isSuccess){
                Model.instance.getProfile().setStatus("false");
                Model.instance.editProfile(null, Model.instance.getProfile(), new Model.EditProfile() {
                    @Override
                    public void onComplete(Boolean isSuccess) {
                        if(isSuccess){
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                        else {
                            Toast.makeText(MyApplication.getContext(), "Can't change to false",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(MyApplication.getContext(), "Can't logout",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialogLogout(String text){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
//        btnYes.setOnClickListener(v -> logout());
        btnYes.setOnClickListener(v -> {
            btnYes.setEnabled(false);
            btnNo.setEnabled(false);
            logout();
        });

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}