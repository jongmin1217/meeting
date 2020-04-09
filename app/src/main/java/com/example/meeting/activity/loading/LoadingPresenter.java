package com.example.meeting.activity.loading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.meeting.activity.login.LoginActivity;
import com.example.meeting.activity.main.MainActivity;
import com.example.meeting.model.userData;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingPresenter {
    LoadingView view;

    public LoadingPresenter(LoadingView view) {
        this.view = view;
    }

    void loding() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String loadEmail;
            SharedPreferences userData = view.getActivity().getSharedPreferences("userData", 0);
            loadEmail = userData.getString("autoLoginEmail", "");
            if (loadEmail.equals("")) {
                view.pleaseLogin();
            } else {
                Toast.makeText(view.getActivity().getApplicationContext(), loadEmail + "로 로그인합니다", Toast.LENGTH_SHORT).show();
                view.autoLogin(loadEmail);
            }
        }, 1000);
    }


}
