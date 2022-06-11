package com.example.perfectfitapp_android.notification;

import android.view.View;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;

public class NotificationCounter {

    TextView notificationNumber;

    final int MAX_NUMBER = 99;

    public NotificationCounter(View view) {
        notificationNumber = view.findViewById(R.id.notificationNumber);
    }

    public void setNotificationNumber(int notificationNumber) {
        if (notificationNumber < MAX_NUMBER)
            this.notificationNumber.setText(String.valueOf(notificationNumber));
        else
            this.notificationNumber.setText(String.valueOf(MAX_NUMBER));
    }

}
