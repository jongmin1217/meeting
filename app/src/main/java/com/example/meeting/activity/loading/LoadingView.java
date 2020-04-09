package com.example.meeting.activity.loading;

import android.app.Activity;

public interface LoadingView {
    void pleaseLogin();
    void autoLogin(String loadEmail);
    Activity getActivity();
}
