package com.example.meeting.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.applyFace.ApplyFaceActivity;
import com.example.meeting.activity.chatlist.ChatListActivity;
import com.example.meeting.activity.chatroom.ChatRoomActivity;
import com.example.meeting.activity.login.LoginActivity;
import com.example.meeting.activity.main.fragment.MoveFragment;
import com.example.meeting.activity.main.fragment.HomeFragment;
import com.example.meeting.activity.main.fragment.MyinfoFragment;
import com.example.meeting.activity.main.fragment.PostFragment;
import com.example.meeting.activity.notification.NotificationActivity;
import com.example.meeting.activity.post.PostActivity;
import com.example.meeting.activity.postwrite.PostWriteActivity;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.custom.BusProvider;
import com.example.meeting.event.ActivityResultEvent;
import com.example.meeting.model.userData;
import com.example.meeting.service.socketServer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatRoomNum;
import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatWaitNum;
import static com.example.meeting.activity.userprofile.UserProfileActivity.userProfileNum;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView {

    @BindView(R.id.fab_main)
    FloatingActionButton fab_main;
    @BindView(R.id.fab_message)
    FloatingActionButton fab_message;
    @BindView(R.id.fab_noti)
    FloatingActionButton fab_noti;
    @BindView(R.id.fab_logout)
    FloatingActionButton fab_logout;
    @BindView(R.id.fab_post_add)
    FloatingActionButton fab_post_add;
    @BindView(R.id.fab_noti_num)
    TextView fab_noti_num;
    @BindView(R.id.fab_message_num)
    TextView fab_message_num;
    @BindView(R.id.result_num)
    TextView result_num;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.layout)
    ConstraintLayout layout;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    private Intent socketServerIntent;
    MainPresenter presenter;

    private FragmentManager fragmentManager;
    private MoveFragment MoveFragment;
    private HomeFragment HomeFragment;
    private PostFragment PostFragment;
    private MyinfoFragment MyinfoFragment;
    private FragmentTransaction transaction;

    private userData userdata;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("post")){
            Intent postIntent = new Intent(this, PostActivity.class);
            postIntent.putExtra("num",intent.getStringExtra("num"));
            postIntent.putExtra("type","fcm");
            startActivity(postIntent);
        }else if(intent.getStringExtra("type").equals("userLike")){
            Intent userIntent = new Intent(this, UserProfileActivity.class);
            userIntent.putExtra("num",intent.getStringExtra("num"));
            userIntent.putExtra("type","fcm");
            startActivity(userIntent);
        }else if(intent.getStringExtra("type").equals("message")){
            Intent chatIntent = new Intent(this, ChatRoomActivity.class);
            chatIntent.putExtra("num",intent.getStringExtra("num"));
            chatIntent.putExtra("type","fcm");
            startActivity(chatIntent);
        }else if(intent.getStringExtra("type").equals("applyFace")){
            Intent faceIntent = new Intent(this, ApplyFaceActivity.class);
            faceIntent.putExtra("roomNum",intent.getStringExtra("roomNum"));
            faceIntent.putExtra("nickname",intent.getStringExtra("nickname"));
            faceIntent.putExtra("profile",intent.getStringExtra("profile"));
            faceIntent.putExtra("userNum",intent.getStringExtra("userNum"));
            startActivity(faceIntent);
        }
        chatRoomNum = "";
        chatWaitNum = "";
        userProfileNum = "";
        userdata = new userData(this);

        fab_open = AnimationUtils.loadAnimation(this, R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.animator.fab_close);

        fragmentManager = getSupportFragmentManager();

        HomeFragment = new HomeFragment();
        PostFragment = new PostFragment();
        MoveFragment = new MoveFragment();
        MyinfoFragment = new MyinfoFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, HomeFragment).commitAllowingStateLoss();

        fab_main.setOnClickListener(this);
        fab_message.setOnClickListener(this);
        fab_noti.setOnClickListener(this);
        fab_logout.setOnClickListener(this);
        fab_post_add.setOnClickListener(this);
        fab_noti_num.setOnClickListener(this);

        presenter = new MainPresenter(this);

        if(userdata.getNotification()!=0){
            result_num.setVisibility(View.VISIBLE);
            result_num.setText(userdata.getNotification()+"");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.home: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment).commitAllowingStateLoss();
                    return true;
                }
                case R.id.post: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, PostFragment).commitAllowingStateLoss();
                    return true;
                }
                case R.id.game: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, MoveFragment).commitAllowingStateLoss();
                    return true;
                }
                case R.id.myinfo: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, MyinfoFragment).commitAllowingStateLoss();
                    return true;
                }
                default:
                    return false;
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.d("firebaseLog","getInstanceId failed",task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.d("firebaseLog","FCM token"+token);
                    presenter.setToken(token);
                });
        userData userdata = new userData(this);
        socketServerIntent = new Intent(this, socketServer.class);
        socketServerIntent.putExtra("num",userdata.getNum());
        startService(socketServerIntent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("notification"));
        if(userdata.getAllBadge()==0) {
            result_num.setVisibility(View.INVISIBLE);
            fab_message_num.setVisibility(View.INVISIBLE);
            fab_noti_num.setVisibility(View.INVISIBLE);
        }else{
            result_num.setVisibility(View.VISIBLE);
            result_num.setText(userdata.getAllBadge()+"");
            if(isFabOpen){
                if(userdata.getNotification()!=0){
                    fab_noti_num.setVisibility(View.VISIBLE);
                    fab_noti_num.setText(userdata.getNotification()+"");
                }else{
                    fab_noti_num.setVisibility(View.INVISIBLE);
                }
                if(userdata.getAllMsg()!=0){
                    fab_message_num.setVisibility(View.VISIBLE);
                    fab_message_num.setText(userdata.getAllMsg()+"");
                }else{
                    fab_message_num.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            result_num.setVisibility(View.VISIBLE);
            result_num.setText(userdata.getAllBadge()+"");
            if(Objects.equals(intent.getStringExtra("type"), "notice")){
                if(isFabOpen){
                    fab_noti_num.setVisibility(View.VISIBLE);
                    fab_noti_num.setText(userdata.getNotification()+"");
                }
            }else if(Objects.equals(intent.getStringExtra("type"), "message")){
                if(isFabOpen){
                    if(userdata.getAllMsg()==0){
                        fab_message_num.setVisibility(View.INVISIBLE);
                    }else{
                        fab_message_num.setVisibility(View.VISIBLE);
                        fab_message_num.setText(userdata.getAllMsg()+"");
                    }

                }
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_main:
                toggleFab();
                break;
            case R.id.fab_message:
                Intent chatIntent = new Intent(this, ChatListActivity.class);
                chatIntent.putExtra("num",userdata.getNum());
                startActivity(chatIntent);
                break;
            case R.id.fab_noti:
                userdata.resetNotification();
                fab_noti_num.setVisibility(View.INVISIBLE);
                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
                break;
            case R.id.fab_post_add:
                Intent intent = new Intent(this, PostWriteActivity.class);
                startActivityForResult(intent,2000);
                break;
            case R.id.fab_logout:
                presenter.logout();
                break;
        }
    }



    @SuppressLint("SetTextI18n")
    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.add);
            fab_message.startAnimation(fab_close);
            fab_noti.startAnimation(fab_close);
            fab_logout.startAnimation(fab_close);
            fab_post_add.startAnimation(fab_close);
            fab_noti_num.setVisibility(View.INVISIBLE);
            fab_message_num.setVisibility(View.INVISIBLE);
            fab_post_add.setClickable(false);
            fab_message.setClickable(false);
            fab_noti.setClickable(false);
            fab_logout.setClickable(false);
            isFabOpen = false;
        } else {

            fab_main.setImageResource(R.drawable.cancel);
            fab_message.startAnimation(fab_open);
            fab_noti.startAnimation(fab_open);
            fab_logout.startAnimation(fab_open);
            fab_post_add.startAnimation(fab_open);
            if(userdata.getNotification()!=0){
                fab_noti_num.setVisibility(View.VISIBLE);
                fab_noti_num.setText(userdata.getNotification()+"");
            }if(userdata.getAllMsg()!=0){
                fab_message_num.setVisibility(View.VISIBLE);
                fab_message_num.setText(userdata.getAllMsg()+"");
            }

            fab_post_add.setClickable(true);
            fab_message.setClickable(true);
            fab_noti.setClickable(true);
            fab_logout.setClickable(true);
            isFabOpen = true;
        }
    }

    public Fragment getVisibleFragment() {
        for (Fragment fragment: getSupportFragmentManager().getFragments()) {
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }


    @Override
    public void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Activity getActivity() {
        Activity activity = this;
        return activity;
    }

    public boolean getFragment(){
        return getVisibleFragment() == MyinfoFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(socketServerIntent);
    }
}