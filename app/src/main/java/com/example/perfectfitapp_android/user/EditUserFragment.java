package com.example.perfectfitapp_android.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.likes.LikesFragmentDirections;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;


public class EditUserFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button editBtn;

    public EditUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        emailEt = view.findViewById(R.id.edit_user_email_et);
        passwordEt = view.findViewById(R.id.edit_user_password_et);
        editBtn = view.findViewById(R.id.edit_user_edit_btn);
        editBtn.setOnClickListener(v-> edit(v));

        return view;
    }

    private void edit(View view) {

        String previousEmail = Model.instance.getUser().getEmail();
        editBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (previousEmail.equals(email)) {
            Model.instance.getUser().setPassword(password);
            Model.instance.editUser(previousEmail, Model.instance.getUser(), user -> {
                if (user != null) {
                    Model.instance.setUser(user);
                    Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);
                } else {
                    editBtn.setEnabled(true);
                }
            });
        } else {
            Model.instance.checkIfEmailExist(email, isSuccess -> {
                if (isSuccess) {
                    Model.instance.getUser().setPassword(password);
                    Model.instance.getUser().setEmail(email);
                    Model.instance.editUser(previousEmail, Model.instance.getUser(), user -> {
                        if (user != null) {
                            Model.instance.setUser(user);
                            Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);

                        } else {
                            editBtn.setEnabled(true);
                        }
                    });
                } else {
                    Toast.makeText(MyApplication.getContext(), "Your email addresses already exist in our database, " +
                            "please try a different one", Toast.LENGTH_LONG).show();
                    editBtn.setEnabled(true);
                }
            });
        }


    }
}