package com.example.meeting.activity.userprofile;

import com.example.meeting.adapter.slider_adapter;

public interface UserProfileView {
    void getProfile(slider_adapter slider_adapter, String nickname, String area, String age, String height, String form,
                    String smoking, String drinking, String hobby, String personality, String idealType,boolean gender);

    void userStatus(String status,String statusNum);
    void chatStart(String num);
}
