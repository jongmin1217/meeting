package com.example.meeting.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageData {

    @Expose
    @SerializedName("image") private Uri image;

    public Uri getImage(){
        return image;
    }
    public void setImage(Uri image){
        this.image = image;
    }
}
