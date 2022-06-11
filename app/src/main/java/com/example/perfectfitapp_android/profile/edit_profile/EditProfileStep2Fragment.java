package com.example.perfectfitapp_android.profile.edit_profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.create_profile.CreateProfileModel;
import com.example.perfectfitapp_android.model.generalModel;
import com.example.perfectfitapp_android.profile.ModelProfile;

import java.util.ArrayList;
import java.util.List;


public class EditProfileStep2Fragment extends Fragment {

    TextView descriptionText, bodyTypeTv;
    Button continueBtn;
    ImageButton leftBtn, rightBtn;
    ImageView bodyImage;
    int place = 0;
    ArrayList<Integer> resBodyType;
    List<String> bodyTypesList, bodyDescriptionList;
    LottieAnimationView progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_step2, container, false);

        place = 0;
        descriptionText = view.findViewById(R.id.edit_profile_step2_downtext);
        descriptionText.setEnabled(false);

        bodyTypesList = generalModel.instance.map.get("BodyTypes");
        bodyDescriptionList = generalModel.instance.map.get("BodyTypeDescription");

        bodyImage = view.findViewById(R.id.edit_profile_step2_bodyimage_imv);
        bodyTypeTv = view.findViewById(R.id.edit_profile_step2_bodytype_name_et);

        progressBar = view.findViewById(R.id.edit_profile_step2_progress_bar);
        progressBar.setVisibility(View.GONE);

        for (String str : bodyTypesList) {
            if (str.equals(ModelProfile.instance.getEditProfile().getBodyType())) {
                break;
            } else {
                place++;
            }
        }

        setResBodyType();
        setBodyType();

        rightBtn = view.findViewById(R.id.edit_profile_step2_right_img_btn);
        rightBtn.setOnClickListener(v -> goRight());
        leftBtn = view.findViewById(R.id.edit_profile_step2_left_img_btn);
        leftBtn.setOnClickListener(v -> goLeft());

        continueBtn = view.findViewById(R.id.edit_profile_step2_continue_btn);
        continueBtn.setOnClickListener(v -> continueStep3(view));

        return view;
    }

    private void setBodyType() {
        System.out.println("the place = " + place);
        descriptionText.setText(bodyDescriptionList.get(place));
        bodyImage.setImageResource(resBodyType.get(place));
        bodyTypeTv.setText(bodyTypesList.get(place));
    }

    public void setResBodyType() {

        resBodyType = new ArrayList<>();

        if (ModelProfile.instance.getEditProfile().getGender().equals("Female")) {

            // if its a female:
            resBodyType.add(R.drawable.body_hourglass_female);
            resBodyType.add(R.drawable.body_pear_female);
            resBodyType.add(R.drawable.body_round_female);
            resBodyType.add(R.drawable.body_rectangle_female);
        } else {
            resBodyType.add(R.drawable.body_inverted_triangle_men);
            resBodyType.add(R.drawable.body_trapezoid_men);
            resBodyType.add(R.drawable.body_round_men);
            resBodyType.add(R.drawable.body_rectangle_man);
        }
    }

    private void goLeft() {
        if (place != 0) {
            place--;
        }
        setBodyType();
    }

    private void goRight() {
        if (place != (bodyTypesList.size() - 1)) {
            place++;
        }
        setBodyType();
    }

    private void continueStep3(View view) {
        progressBar.setVisibility(View.VISIBLE);
        ModelProfile.instance.getEditProfile().setBodyType(bodyTypeTv.getText().toString());
        String des = Navigation.findNavController(view).getGraph().getDisplayName();
        if (des.equals("com.example.perfectfitapp_android:id/user_profiles_graph")) {
            Navigation.findNavController(view).navigate(R.id.action_editProfileStep2Fragment2_to_editProfileStep3Fragment2);
        } else {
            Navigation.findNavController(view).navigate(R.id.action_editProfileStep2Fragment_to_editProfileStep3Fragment);
        }
    }
}