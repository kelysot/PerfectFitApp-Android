package com.example.perfectfitapp_android.login;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;
import com.example.perfectfitapp_android.post.PostPageFragmentArgs;


public class ResetPasswordFragment extends Fragment {

    EditText codeEt, passEt;
    Button resetPassBtn;
    String code, email;
    ProgressBar progressBar;

    public ResetPasswordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        code = ResetPasswordFragmentArgs.fromBundle(getArguments()).getCode();
        email = ResetPasswordFragmentArgs.fromBundle(getArguments()).getEmail();

        codeEt = view.findViewById(R.id.reset_password_code_et);
        passEt = view.findViewById(R.id.reset_password_new_password_et);
        progressBar = view.findViewById(R.id.reset_password_progressBar);
        progressBar.setVisibility(View.GONE);

        resetPassBtn = view.findViewById(R.id.reset_password_reset_password_btn);
        resetPassBtn.setOnClickListener(v -> resetPass(v));

        return view;
    }

    private void resetPass(View v) {
        progressBar.setVisibility(View.VISIBLE);
        String userCode = codeEt.getText().toString().trim();
        resetPassBtn.setEnabled(false);

        if(code.equals(userCode)){
            Model.instance.getUserFromServer(email, user -> {
                user.setPassword(passEt.getText().toString().trim());
                Model.instance.changePassword(user, isSuccess -> {
                    if (isSuccess)
                        Navigation.findNavController(v).navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment());
                });
            });
        } else {
            String s = "Incorrect code" + "\n" + " please try again.";
            showOkDialog(s);
            progressBar.setVisibility(View.GONE);
            resetPassBtn.setEnabled(true);
        }
    }

    private void showOkDialog(String text){
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