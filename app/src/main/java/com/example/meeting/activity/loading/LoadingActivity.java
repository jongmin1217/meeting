package com.example.meeting.activity.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.example.meeting.R;
import com.example.meeting.activity.login.LoginActivity;
import com.example.meeting.activity.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity implements LoadingView{

    LoadingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new LoadingPresenter(this);

        presenter.loding();

    }

    @Override
    public void pleaseLogin() {
        Intent move_login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(move_login);
        finish();
    }

    @Override
    public void autoLogin(String loadEmail) {
        Intent move_main = new Intent(getApplicationContext(), MainActivity.class);
        move_main.putExtra("type","login");
        startActivity(move_main);
        finish();
    }

    @Override
    public Activity getActivity() {
        Activity activity = this;
        return activity;
    }
}
