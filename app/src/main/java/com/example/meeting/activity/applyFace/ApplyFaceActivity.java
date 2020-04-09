package com.example.meeting.activity.applyFace;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.custom.WebrtcConnect;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyFaceActivity extends AppCompatActivity implements View.OnClickListener,ApplyFaceView {

    @BindView(R.id.profile)
    ImageView profile;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.btnOk)
    ImageButton btnOk;
    @BindView(R.id.btnRe)
    ImageButton btnRe;

    private ApplyFacePresenter presenter;
    private String roomNum;
    private String userNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_face);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("cancelFace"));

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        String profile = intent.getStringExtra("profile");
        roomNum = intent.getStringExtra("roomNum");
        userNum = intent.getStringExtra("userNum");

        this.nickname.setText(nickname);
        Picasso.get().load(profile).into(this.profile);

        btnOk.setOnClickListener(this);
        btnRe.setOnClickListener(this);

        presenter = new ApplyFacePresenter(this);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    @SuppressLint("SetTextI18n")
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOk:
                WebrtcConnect webrtcConnect = new WebrtcConnect(this);
                webrtcConnect.connectToRoom(roomNum,false,false,false,0,userNum);
                finish();
                break;
            case R.id.btnRe:
                presenter.refuseFace(userNum);
                finish();
                break;
        }
    }
}
