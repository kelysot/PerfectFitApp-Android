package com.example.perfectfitapp_android.profile;

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
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;

import java.util.ArrayList;


public class EditProfileStep2Fragment extends Fragment {

    TextView descriptionText, bodyTypeTv;
    Button continueBtn;
    ImageButton leftBtn, rightBtn;
    ImageView bodyImage;
    String[] bodyTypes, bodyDescription;
    int place = 0;
    ArrayList<Integer> resBodyType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_step2, container, false);

        descriptionText = view.findViewById(R.id.edit_profile_step2_downtext);
        descriptionText.setEnabled(false);

        bodyTypes = getResources().getStringArray(R.array.body_types);
        bodyDescription = getResources().getStringArray(R.array.body_type_description);

        bodyImage = view.findViewById(R.id.edit_profile_step2_bodyimage_imv);
        bodyTypeTv = view.findViewById(R.id.edit_profile_step2_bodytype_name_et);

        for (String str:bodyTypes) {
            if(str.equals(ModelProfile.instance.getEditProfile().getBodyType())){
                break;
            }
            else{
                place++;
            }
        }

        setResBodyType();
        setBodyType();


        rightBtn = view.findViewById(R.id.edit_profile_step2_right_img_btn);
        rightBtn.setOnClickListener(v-> goRight());
        leftBtn = view.findViewById(R.id.edit_profile_step2_left_img_btn);
        leftBtn.setOnClickListener(v-> goLeft());

        continueBtn = view.findViewById(R.id.edit_profile_step2_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep3(view));

        return view;
    }

    private void setBodyType() {
        descriptionText.setText(bodyDescription[place]);
        bodyImage.setImageResource(resBodyType.get(place));
        bodyTypeTv.setText(bodyTypes[place]);
    }

    private void setResBodyType() {
        resBodyType = new ArrayList<>();

        if (ModelProfile.instance.getEditProfile().getGender().equals("Female")) {

            resBodyType.add(R.drawable.body_girl_endomorph);
            resBodyType.add(R.drawable.body_girl_mesomorph);
            //TODO: find Pear/Apple picture
            resBodyType.add(R.drawable.body_girl_mesomorph);
            resBodyType.add(R.drawable.body_girl_ectomorph);
        }
        else{

            resBodyType.add(R.drawable.body_boy_endomorph);
            resBodyType.add(R.drawable.body_boy_mesomorph);
            //TODO: find Pear/Apple picture
            resBodyType.add(R.drawable.body_boy_mesomorph);
            resBodyType.add(R.drawable.body_boy_ectomorph);

        }
    }

    private void goLeft() {
        if(place != 0){
            place--;
        }
        setBodyType();
    }

    private void goRight() {
        if(place != (bodyTypes.length-1)){
            place++;
        }
        setBodyType();
    }

    private void continueStep3(View view) {
        ModelProfile.instance.getEditProfile().setBodyType(bodyTypeTv.getText().toString());
        Navigation.findNavController(view).navigate(R.id.action_editProfileStep2Fragment_to_editProfileStep3Fragment);
    }
}