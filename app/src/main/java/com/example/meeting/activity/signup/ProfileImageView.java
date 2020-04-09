package com.example.meeting.activity.signup;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.meeting.model.ProfileImageData;

import java.util.ArrayList;
import java.util.List;

public interface ProfileImageView {
    void addImage(Uri uri);
    void removeImage(int position);
    void profileUploadSucces();
    Activity getActivity();
}
