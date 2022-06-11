package com.example.perfectfitapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.AppLocalDb;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.User;
import com.example.perfectfitapp_android.user_profiles.UserProfilesActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Model.instance.isSignIn()){
                Model.instance.mainThread.post(() -> {
                    Model.instance.getUserFromRoom(user -> {
                        Model.instance.getUserFromServer(user.getEmail(), user1 -> {
                            if(user1 != null){
                                Model.instance.setUser(user1);
                                toFeedActivity();
                            }
                            else{
                                toLoginActivity();
                            }

                        });
                    });

                });
            }else{
                Model.instance.mainThread.post(() -> {
                    toLoginActivity();
                });
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, UserProfilesActivity.class);
        startActivity(intent);
        finish();
    }
}