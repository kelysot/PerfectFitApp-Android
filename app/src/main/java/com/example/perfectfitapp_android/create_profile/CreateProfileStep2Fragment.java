package com.example.perfectfitapp_android.create_profile;

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
import com.example.perfectfitapp_android.model.generalModel;

import java.util.ArrayList;
import java.util.List;


public class CreateProfileStep2Fragment extends Fragment {

    TextView descriptionText, bodyTypeTv;
    Button continueBtn;
    ImageButton leftBtn, rightBtn;
    ImageView bodyImage;
    List<String> bodyTypesList, bodyDescriptionList;
    int place = 0;
    ArrayList<Integer> resBodyType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_profile_step2, container, false);

        descriptionText = view.findViewById(R.id.register_step2_downtext);
        descriptionText.setEnabled(false);

        bodyImage = view.findViewById(R.id.register_step2_budyimage_imv);
        bodyImage.setImageResource(R.drawable.body_girl_ectomorph);

        bodyTypeTv = view.findViewById(R.id.register_step2_bodytype_name_et);

        rightBtn = view.findViewById(R.id.register_step2_right_img_btn);
        rightBtn.setOnClickListener(v-> goRight());
        leftBtn = view.findViewById(R.id.register_step2_left_img_btn);
        leftBtn.setOnClickListener(v-> goLeft());

        continueBtn = view.findViewById(R.id.register_step2_continue_btn);
        continueBtn.setOnClickListener(v-> continueStep3(view));

        bodyTypesList = generalModel.instance.map.get("BodyTypes");
        bodyDescriptionList = generalModel.instance.map.get("BodyTypeDescription");

        setResBodyType();
        setBodyType();

        //TODO bring the images of bodyTypes from the db

        return view;
    }

    private void goLeft() {
        if(place != 0){
            place--;
        }
        setBodyType();
    }

    private void goRight() {
        if(place != bodyTypesList.size() - 1){
            place++;
        }
        setBodyType();
    }

    private void continueStep3(View view) {
        CreateProfileModel.instance.profile.setBodyType(bodyTypeTv.getText().toString());
        Navigation.findNavController(view).navigate(R.id.action_createProfileStep2Fragment2_to_createProfileStep3Fragment2);
    }


    public void setBodyType(){
        System.out.println("------- " + bodyDescriptionList);
        System.out.println("------- " + bodyTypesList);
        descriptionText.setText(bodyDescriptionList.get(place));
        bodyImage.setImageResource(resBodyType.get(place));
        bodyTypeTv.setText(bodyTypesList.get(place));
    }

    public void setResBodyType(){

        resBodyType = new ArrayList<>();

        if (CreateProfileModel.instance.profile.getGender().equals("Female")) {

            // if its a female:
            resBodyType.add(R.drawable.body_hourglass_female);
            resBodyType.add(R.drawable.body_pear_female);
            resBodyType.add(R.drawable.body_round_female);
            resBodyType.add(R.drawable.body_rectangle_female);
        }
        else{

            resBodyType.add(R.drawable.body_inverted_triangle_men);
            resBodyType.add(R.drawable.body_trapezoid_men);
            resBodyType.add(R.drawable.body_round_men);
            resBodyType.add(R.drawable.body_rectangle_man);
        }
    }
}