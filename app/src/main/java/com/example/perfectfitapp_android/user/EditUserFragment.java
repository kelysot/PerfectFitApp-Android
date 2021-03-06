package com.example.perfectfitapp_android.user;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;

import java.util.regex.Pattern;


public class EditUserFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button editBtn;

    public EditUserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        emailEt = view.findViewById(R.id.edit_user_email_et);
        emailEt.setText(Model.instance.getUser().getEmail());

        passwordEt = view.findViewById(R.id.edit_user_password_et);

        editBtn = view.findViewById(R.id.edit_user_edit_btn);
        editBtn.setOnClickListener(v -> edit(v));

        return view;
    }

    private void edit(View view) {

        String previousEmail = Model.instance.getUser().getEmail();
        editBtn.setEnabled(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        /* *************************************** Validations *************************************** */

        if (!isValidEmail(email)) {
            emailEt.setError("Please enter valid email address.");
            emailEt.requestFocus();
            editBtn.setEnabled(true);
            return;
        }

        if (!isValidPassword(password)) {
            passwordEt.requestFocus();
            String s = "Your password needs to contain 8 characters.";
            showOkDialog(s);
            editBtn.setEnabled(true);
            return;
        }

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
                            Model.instance.removeFromRoom(isSuccess1 -> {
                                if (isSuccess1) {
                                    Model.instance.addToRoom(user, isSuccess11 -> {
                                        if (isSuccess11) {
                                            Model.instance.setUser(user);
                                        } else {
                                            Log.d("TAG", "User didn't add to room in edit user.");
                                            editBtn.setEnabled(true);
                                        }
                                    });
                                } else {
                                    Log.d("TAG", "Old user didn't remove from room in edit user.");
                                    editBtn.setEnabled(true);
                                }
                            });
                            Navigation.findNavController(view).navigate(R.id.action_global_userProfilesFragment2);
                        } else {
                            editBtn.setEnabled(true);
                        }
                    });
                } else {
                    String s = "The email addresses already exist, " + "\n" + "please try a different one.";
                    showOkDialog(s);
                    editBtn.setEnabled(true);
                }
            });
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    private void showOkDialog(String text) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}