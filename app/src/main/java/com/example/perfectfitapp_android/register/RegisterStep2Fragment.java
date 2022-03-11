package com.example.perfectfitapp_android.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;


public class RegisterStep2Fragment extends Fragment {

    TextView upText, downText;
    Button continueBtn;
    ImageButton leftBtn, rightBtn;
    ImageView bodyImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: get the gender and put the relevant lists
        //TODO: array of body types - move with the arrows accordingly to the array position

        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);

        upText = view.findViewById(R.id.register_step2_uptext);
        upText.setEnabled(false);

        downText = view.findViewById(R.id.register_step2_downtext);
//        downText.setHint(getResources().getString(R.string.pear));
        downText.setText("Narrow shoulders and wide hips that give the pear look to the body. There is a tendency in this structure to look lower.");
        downText.setEnabled(false);

        bodyImage = view.findViewById(R.id.register_step2_budyimage_imv);
        bodyImage.setImageResource(R.drawable.body_girl_ectomorph);

        rightBtn = view.findViewById(R.id.register_step2_right_img_btn);
        rightBtn.setOnClickListener(v-> goRight());
        leftBtn = view.findViewById(R.id.register_step2_left_img_btn);
        leftBtn.setOnClickListener(v-> goLeft());

        continueBtn = view.findViewById(R.id.register_step2_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep3(view));

        return view;
    }

    private void goLeft() {
        bodyImage.setImageResource(R.drawable.body_girl_ectomorph);
    }

    private void goRight() {
        bodyImage.setImageResource(R.drawable.body_girl_endomorph);
    }

    private void continueStep3(View view) {
        Navigation.findNavController(view).navigate(R.id.action_registerStep2Fragment_to_registerStep3Fragment);
    }
}