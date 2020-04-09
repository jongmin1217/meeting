package com.example.meeting.activity.login;

import android.app.Activity;

public interface LoginView {

    void loginTryFail(String result,String email);
    void loginTrySucces(String email);
    Activity getActivity();
}
