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

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;


public class ResetPassword1EmailFragment extends Fragment {

    EditText emailEt;
    Button sendEmailBtn;
    String email;
    LottieAnimationView progressBar;

    public ResetPassword1EmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password1_email, container, false);

        emailEt = view.findViewById(R.id.reset_password_code_et);
        progressBar = view.findViewById(R.id.reset_password_email_progressBar);
        progressBar.setVisibility(View.GONE);

        sendEmailBtn = view.findViewById(R.id.reset_password_send_email_btn);
        sendEmailBtn.setOnClickListener(v -> sendEmail(v));

        return view;
    }

    private void sendEmail(View view) {
        progressBar.setVisibility(View.VISIBLE);
        sendEmailBtn.setEnabled(false);

        email = emailEt.getText().toString().trim();
        Model.instance.checkIfEmailExist(email, isSuccess -> {
            if (!isSuccess) {
                Model.instance.resetPassword(email, code -> {
                    if (code != null) {
                        Navigation.findNavController(view).navigate(ResetPassword1EmailFragmentDirections.actionResetPassword1EmailFragmentToResetPasswordFragment(code, email));
                    }
                });
            } else {
                String s = "Incorrect email" + "\n" + " please try again.";
                showOkDialog(s);
                progressBar.setVisibility(View.GONE);
                sendEmailBtn.setEnabled(true);
            }
        });
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