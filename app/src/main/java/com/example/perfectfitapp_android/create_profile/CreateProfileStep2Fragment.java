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

import java.util.ArrayList;


public class CreateProfileStep2Fragment extends Fragment {

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

        //TODO: get the gender and put the relevant lists
        //TODO: array of body types - move with the arrows accordingly to the array position

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

        bodyTypes = getResources().getStringArray(R.array.body_types);
        bodyDescription = getResources().getStringArray(R.array.body_type_description);

        setResBodyType();
        setBodyType();

        return view;
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
        CreateProfileModel.instance.profile.setBodyType(bodyTypeTv.getText().toString());
        Navigation.findNavController(view).navigate(R.id.action_registerStep2Fragment2_to_registerStep3Fragment2);
    }


    public void setBodyType(){
        descriptionText.setText(bodyDescription[place]);
        bodyImage.setImageResource(resBodyType.get(place));
        bodyTypeTv.setText(bodyTypes[place]);
    }

    public void setResBodyType(){

        resBodyType = new ArrayList<>();

        if (CreateProfileModel.instance.profile.getGender().equals("Female")) {

            // if its a female:
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
}