package com.example.meeting.activity.userprofile;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.activity.chatroom.ChatRoomActivity;
import com.example.meeting.adapter.slider_adapter;
import com.example.meeting.custom.WebrtcConnect;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.PostImageAdapter;

import java.util.Objects;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity implements UserProfileView, View.OnClickListener {
    public static String userProfileNum;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageview)
    ViewPager viewPager;
    @BindView(R.id.areaInput)
    TextView areaInput;
    @BindView(R.id.ageInput)
    TextView ageInput;
    @BindView(R.id.heightInput)
    TextView heightInput;
    @BindView(R.id.formInput)
    TextView formInput;
    @BindView(R.id.smokingInput)
    TextView smokingInput;
    @BindView(R.id.drinkingInput)
    TextView drinkingInput;
    @BindView(R.id.hobbyInput)
    TextView hobbyInput;
    @BindView(R.id.personalityInput)
    TextView personalityInput;
    @BindView(R.id.idealTypeInput)
    TextView idealTypeInput;
    @BindView(R.id.btnProfileEdit)
    ImageButton btnProfileEdit;
    @BindView(R.id.postImageRecyclerview)
    RecyclerView postImageRecyclerview;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.likeText)
    TextView likeText;
    @BindView(R.id.btnFaceTry)
    ImageButton btnFaceTry;
    @BindView(R.id.faceText)
    TextView faceText;

    private String num;
    private UserProfilePresenter presenter;
    private PostImageAdapter adapter;
    private String statusNum,status;
    private userData userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        userdata = new userData(this);
        Intent intent = getIntent();
        num = Objects.requireNonNull(intent.getExtras()).getString("num");
        userProfileNum = num;
        if(intent.getExtras().getString("type")!=null){
            if(Objects.requireNonNull(intent.getExtras().getString("type")).equals("fcm")){
                userdata.removeNotification();
            }
        }
        init();
        presenter = new UserProfilePresenter(this,adapter,this);

        btnProfileEdit.setOnClickListener(this);
        btnFaceTry.setOnClickListener(this);

        likeText.setVisibility(View.VISIBLE);
        presenter.getProfile(num);
        presenter.getUserStatus(num);
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("userStatus"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.getUserStatus(num);
            userdata.removeNotification();
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        userProfileNum = "";
    }

    private void init(){
        postImageRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PostImageAdapter();
        postImageRecyclerview.setAdapter(adapter);
    }

    @Override
    public void getProfile(slider_adapter slider_adapter, String nickname, String area, String age, String height, String form, String smoking, String drinking, String hobby, String personality, String idealType,boolean gender) {
        viewPager.setAdapter(slider_adapter);
        viewPager.setCurrentItem(0);
        if(gender){
            btnProfileEdit.setVisibility(View.INVISIBLE);
            likeText.setVisibility(View.INVISIBLE);
        }
        toolbar.setTitle(nickname);areaInput.setText(area);ageInput.setText(age);heightInput.setText(height);
        formInput.setText(form);smokingInput.setText(smoking);drinkingInput.setText(drinking);hobbyInput.setText(hobby);
        personalityInput.setText(personality);idealTypeInput.setText(idealType);
        text.setText(nickname+" 님의 게시글");
    }

    @Override
    public void userStatus(String status,String statusNum) {
        this.statusNum = statusNum;
        this.status = status;
        switch (status) {
            case "no":
                btnProfileEdit.setImageResource(R.drawable.non_favorite);
                btnProfileEdit.setColorFilter(Color.parseColor("#ff2222"));
                btnProfileEdit.setPadding(10, 10, 10, 10);
                btnProfileEdit.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case "send":
                btnProfileEdit.setImageResource(R.drawable.favorite);
                btnProfileEdit.setColorFilter(Color.parseColor("#ff2222"));
                btnProfileEdit.setPadding(10, 10, 10, 10);
                btnProfileEdit.setScaleType(ImageView.ScaleType.FIT_XY);
                likeText.setText("좋아요를 보냈습니다");
                break;
            case "receive":
                btnProfileEdit.setImageResource(R.drawable.favorite);
                btnProfileEdit.setColorFilter(Color.parseColor("#ff2222"));
                btnProfileEdit.setPadding(10, 10, 10, 10);
                btnProfileEdit.setScaleType(ImageView.ScaleType.FIT_XY);
                likeText.setText("좋아요를 받았습니다");
                break;
            case "connect":
                btnProfileEdit.setImageResource(R.drawable.sms);
                btnProfileEdit.setColorFilter(Color.parseColor("#ff9999"));
                btnProfileEdit.setPadding(20, 20, 20, 20);
                btnProfileEdit.setScaleType(ImageView.ScaleType.FIT_XY);
                likeText.setText("채팅");
                btnFaceTry.setVisibility(View.VISIBLE);
                btnFaceTry.setColorFilter(Color.parseColor("#ff9999"));
                faceText.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void chatStart(String num) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("num",num);
        intent.putExtra("type","fcm");
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnProfileEdit) {
            switch (status) {
                case "no":
                    dialog("no");
                    break;
                case "send":
                    Toast.makeText(this, "이미 좋아요를 보냈습니다", Toast.LENGTH_SHORT).show();
                    break;
                case "receive":
                    dialog("receive");
                    break;
                case "connect":
                    presenter.chatClick(num);
                    break;
            }
        }else if(view.getId() == R.id.btnFaceTry){
            int roomNum = (new Random()).nextInt(100000000);
            presenter.applyFace(Integer.toString(roomNum),num);
            WebrtcConnect webrtcConnect = new WebrtcConnect(this);
            webrtcConnect.connectToRoom(Integer.toString(roomNum),false,false,false,0,num);
        }
    }

    private void dialog(String type){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(type.equals("no")){
            builder.setTitle("좋아요를 보내겠습니까?");
        }else if(type.equals("receive")){
            builder.setTitle("좋아요를 수락하시겠습니까?");
        }
        builder.setPositiveButton("취소",
                (dialog, which) -> {});
        builder.setNegativeButton("확인",
                (dialog, which) -> {
                    if(type.equals("no")){
                        presenter.editUserStatus(num,"insert","0");
                    }else if(type.equals("receive")){
                        presenter.editUserStatus(num,"update",statusNum);
                    }
                });
        builder.show();
    }
}
