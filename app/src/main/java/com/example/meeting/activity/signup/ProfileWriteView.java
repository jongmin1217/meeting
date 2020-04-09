package com.example.meeting.activity.signup;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public interface ProfileWriteView {
    void nicknameCheckSucces(String nickname);
    void nicknameCheckFail();
    void selectDialog(String type, String text, String items);
    void noVerificationNickname();
    void noInsert();
    void profileWriteSucces(String email);
    Context getContext();

}
