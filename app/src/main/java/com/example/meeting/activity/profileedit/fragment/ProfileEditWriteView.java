package com.example.meeting.activity.profileedit.fragment;

import android.app.Activity;

public interface ProfileEditWriteView {
    Activity activity();
    void selectDialog(String type, String text, String items);
    void getUserInfo(String nickname,String hobby,String personality,String idealType,String hobbyJson,String personalityJson,String idealTypeJson,boolean nicknameCheck,int nicknameChecknum);
    void nicknameCheckSucces(String nickname);
    void nicknameCheckFail();
    void noInsert();
}
