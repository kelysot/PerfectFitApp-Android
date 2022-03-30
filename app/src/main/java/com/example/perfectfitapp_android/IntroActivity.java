package com.example.perfectfitapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.perfectfitapp_android.login.LoginActivity;
import com.example.perfectfitapp_android.model.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //TODO: Func that check if the user is sign in to the app - after localDB.
        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(0); // Change to 3000 later
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("TAG2", Model.instance.getUser().getIsConnected());
            if (Model.instance.getUser().getIsConnected().equals("true")){
                Model.instance.mainThread.post(() -> {
                    toFeedActivity();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}