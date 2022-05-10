package com.example.perfectfitapp_android.notification;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Notification;

import java.util.List;

public class NotificationViewModel extends ViewModel {

    List<Notification> data;

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
