package com.example.perfectfitapp_android.profile.trackers;

import androidx.lifecycle.ViewModel;

import com.example.perfectfitapp_android.model.Profile;

import java.util.List;

public class TrackersViewModel extends ViewModel {

    List<Profile> trackersProfiles;

    public List<Profile> getTrackersProfiles() {
        return trackersProfiles;
    }

    public void setTrackersProfiles(List<Profile> trackersProfiles) {
        this.trackersProfiles = trackersProfiles;
    }
}
