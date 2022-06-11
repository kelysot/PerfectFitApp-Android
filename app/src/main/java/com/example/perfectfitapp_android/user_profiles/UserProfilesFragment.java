package com.example.perfectfitapp_android.user_profiles;

import static java.lang.System.out;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MainActivity;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.SubCategory;
import com.example.perfectfitapp_android.model.generalModel;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import pl.droidsonroids.gif.GifImageButton;


public class UserProfilesFragment extends Fragment {

    Button profile1Btn, profile2Btn, profile3Btn, profile4Btn, profile5Btn;
    ArrayList<Button> profilesButtonsList;
    Model model;
    String longClickUserName;
    LottieAnimationView progressBar;
    TextView text;
    int posInArray;
    LottieAnimationView greenGif;
    MultiWaveHeader waveHeader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profiles, container, false);
        model = Model.instance;

        /************* initialize general *************/
        Model.instance.getGeneral(map -> {
            if (map != null) {
                generalModel.instance.map = map;
            }
        });

        /************* initilize categories *************/
        Model.instance.setCategories(new LinkedList<>());
        Model.instance.setSubCategories(new LinkedList<>());
        Model.instance.setCategoriesAndSubCategories(new HashMap<>());

        progressBar = view.findViewById(R.id.user_profiles_progress_bar);
        progressBar.setVisibility(View.GONE);

        text = view.findViewById(R.id.user_profiles_text);
        greenGif = view.findViewById(R.id.gif_green);
        greenGif.setOnClickListener(v -> {
            addProfile();
        });

        text.setEnabled(false);

        if (Model.instance.getUser().getProfilesArray().size() > 0) {
            text.setVisibility(View.GONE);
            greenGif.setVisibility(View.GONE);
            greenGif.setEnabled(false);
        }

        profilesButtonsList = new ArrayList<>();

        profile1Btn = view.findViewById(R.id.user_profiles_profile1_btn);
        profile2Btn = view.findViewById(R.id.user_profiles_profile2_btn);
        profile3Btn = view.findViewById(R.id.user_profiles_profile3_btn);
        profile4Btn = view.findViewById(R.id.user_profiles_profile4_btn);
        profile5Btn = view.findViewById(R.id.user_profiles_profile5_btn);

        profilesButtonsList.add(profile1Btn);
        profilesButtonsList.add(profile2Btn);
        profilesButtonsList.add(profile3Btn);
        profilesButtonsList.add(profile4Btn);
        profilesButtonsList.add(profile5Btn);

        //btn animation
        profile1Btn.setAlpha(0f);
        profile1Btn.setTranslationY(50);
        profile1Btn.animate().alpha(1f).translationYBy(-50).setDuration(500);

        profile2Btn.setAlpha(0f);
        profile2Btn.setTranslationY(50);
        profile2Btn.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        profile3Btn.setAlpha(0f);
        profile3Btn.setTranslationY(50);
        profile3Btn.animate().alpha(1f).translationYBy(-50).setDuration(1500);

        profile4Btn.setAlpha(0f);
        profile4Btn.setTranslationY(50);
        profile4Btn.animate().alpha(1f).translationYBy(-50).setDuration(2000);

        profile5Btn.setAlpha(0f);
        profile5Btn.setTranslationY(50);
        profile5Btn.animate().alpha(1f).translationYBy(-50).setDuration(2500);


        waveHeader = view.findViewById(R.id.MultiWave);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(0XFFDEC1);
        waveHeader.setCloseColor(0Xfff0d3);

        setButtons();
        setHasOptionsMenu(true);

        return view;
    }

    public void setButtonsEnable(boolean flag) {
        if (flag) {
            for (Button b : profilesButtonsList) {
                b.setEnabled(true);
            }
        } else {
            for (Button b : profilesButtonsList) {
                b.setEnabled(false);
            }
        }
    }

    public void setButtons() {

        setButtonsEnable(true);

        for (int j = 0; j < profilesButtonsList.size(); j++) {
            profilesButtonsList.get(j).setVisibility(View.GONE);
            registerForContextMenu(profilesButtonsList.get(j));
        }

        for (int i = 0; i < Model.instance.getUser().getProfilesArray().size(); i++) {
            profilesButtonsList.get(i).setVisibility(View.VISIBLE);
            profilesButtonsList.get(i).setText(Model.instance.getUser().getProfilesArray().get(i));
            int finalI1 = i;

            profilesButtonsList.get(i).setOnClickListener(v -> moveToHomePageWithProfile(model.getUser().getProfilesArray().get(finalI1)));
            profilesButtonsList.get(i).setOnLongClickListener(v -> {
                editProfileByLongClick(finalI1);
                return false;
            });

            if (Model.instance.getUser().getProfilesArray().size() < 5) {
                Button b = profilesButtonsList.get(Model.instance.getUser().getProfilesArray().size());
                b.setText("Create New Profile");
                b.setVisibility(View.VISIBLE);
                b.setBackgroundColor(Color.parseColor("#191919"));
                b.setTextColor(Color.parseColor("#FFFFFF"));
                b.getLayoutParams().height = 150;
                b.getLayoutParams().width = 620;
                b.setTextSize(16);
                b.setOnClickListener(v -> {
                    b.setEnabled(false);
                    addProfile();
                });
            }
        }
    }


    private void moveToHomePageWithProfile(String userName) {
        setButtonsEnable(false);
        progressBar.setVisibility(View.VISIBLE);
        if (!Model.instance.getProfile().getUserName().isEmpty()) {
            if (profilesButtonsList.contains(Model.instance.getProfile().getUserName())) {
                Model.instance.getProfile().setStatus("false");
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if (isSuccess) {
                        changeProfile(userName);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        showOkDialog(getResources().getString(R.string.outError));
                        setButtonsEnable(true);
                    }
                });
            } else {
                changeProfile(userName);
            }
        } else {
            changeProfile(userName);
        }
    }

    public void changeProfile(String userName) {
        Model.instance.getProfileFromServer(model.getUser().getEmail(), userName, profile -> {
            if (profile != null) {
                model.setProfile(profile);
                profile.setStatus("true");
                Model.instance.editProfile(null, profile, isSuccess -> {
                    if (isSuccess) {
                        Model.instance.getAllCategoriesListener(categoryList -> {
                            for (int i = 0; i < categoryList.size(); i++) {
                                int finalI = i;
                                Model.instance.getSubCategoriesByCategoryId(categoryList.get(i).getCategoryId(), profile.getGender(), new Model.GetSubCategoriesByCategoryIdListener() {
                                    @Override
                                    public void onComplete(List<SubCategory> subCategoryList) {
                                        ArrayList<String> subCategoryNames = new ArrayList<>();
                                        for (int j = 0; j < subCategoryList.size(); j++) {
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
                    } else {
                        progressBar.setVisibility(View.GONE);
                        showOkDialog(getResources().getString(R.string.outError));
                        setButtonsEnable(true);
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                setButtonsEnable(true);
            }
        });
    }

    private void addProfile() {

        greenGif.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        if (Model.instance.getUser().getProfilesArray().size() == 5) {
            showOkDialog("Sorry, you can only have 5 profiles.");
            progressBar.setVisibility(View.GONE);
        } else {
            Navigation.findNavController(greenGif).navigate(R.id.action_userProfilesFragment2_to_createProfileStep1Fragment2);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
    }

    public void editProfileByLongClick(int finalI) {
        longClickUserName = Model.instance.getUser().getProfilesArray().get(finalI);
        posInArray = finalI;
        Model.instance.getProfileFromServer(Model.instance.getUser().getEmail(), longClickUserName, profile -> {
            if (profile != null) {
                Model.instance.setProfile(profile);
            } else {
                showOkDialog(getResources().getString(R.string.outError));
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_edit_menuItem: {
                progressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(this.getView()).navigate(R.id.action_userProfilesFragment2_to_editProfileFragment2);
                return true;
            }
            case R.id.profile_delete_menuItem: {
                String s = "Are you sure you want to delete " + longClickUserName + " profile?";
                showDialog(s);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void delete() {
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.deleteProfile(longClickUserName, isSuccess -> {
            if (isSuccess) {
                Model.instance.getUser().getProfilesArray().remove(posInArray); //current user
                setButtons();
                progressBar.setVisibility(View.GONE);
                Navigation.findNavController(this.getView()).navigate(R.id.action_global_userProfilesFragment2);

            } else {
                showOkDialog(getResources().getString(R.string.outError));
            }
        });
    }

    private void showDialog(String text) {
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

    private void showOkDialog(String str) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(str);

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
            addProfile();
            return true;
        } else if (item.getItemId() == R.id.EditUserFragment) {
            NavHostFragment.findNavController(this).navigate(UserProfilesFragmentDirections.actionUserProfilesFragment2ToEditUserFragment());
            return true;
        } else if (item.getItemId() == R.id.logout) {
            String s = "Do you want to log out of PerfectFit?";
            showDialogLogout(s);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Model.instance.logout(isSuccess -> {
            if (isSuccess) {
                Model.instance.logoutFromAppLocalDB(isSuccess1 -> {
                    if (isSuccess1) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    } else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            } else {

                if (Model.instance.getRefreshFlag()) {
                    Model.instance.setRefreshFlag(false);
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
                showOkDialog(getResources().getString(R.string.outError));
            }
        });
    }

    private void showDialogLogout(String text) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
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