package com.example.meeting.activity.profileedit.fragment;

import android.app.Activity;
import android.net.Uri;

import java.util.ArrayList;

public interface ProfileEditImageView {
    void addImage(String url);
    void getImage(ArrayList<String> url);
    void removeImage(int position);
    Activity getActivityFragment();
}
