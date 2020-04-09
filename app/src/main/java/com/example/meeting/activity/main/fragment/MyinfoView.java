package com.example.meeting.activity.main.fragment;

import android.app.Activity;

import com.example.meeting.adapter.slider_adapter;

public interface MyinfoView {
    void getProfile(slider_adapter slider_adapter,String nickname,String area,String age,String height,String form,
                    String smoking,String drinking,String hobby,String personality,String idealType);
    Activity getFragmentActivity();

}
