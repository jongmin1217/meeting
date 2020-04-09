package com.example.meeting.activity.chatroom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meeting.R;
import com.example.meeting.custom.Util;
import com.example.meeting.custom.WebrtcConnect;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.ChatRoomAdapter;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity implements ChatRoomView, View.OnClickListener {
    public static String chatRoomNum;
    public static String chatWaitNum;
    public static String chatUserNum;
    private ArrayList<Integer> imageNum;

    @BindView(R.id.chatRecyclerview)
    RecyclerView chatRecyclerview;
    @BindView(R.id.editMsg)
    EditText editMsg;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.btnSend)
    ImageButton btnSend;
    @BindView(R.id.btnAdd)
    ImageButton btnAdd;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    @BindView(R.id.newMsgLayout)
    LinearLayout newMsgLayout;
    @BindView(R.id.newMsgProfile)
    ImageView newMsgProfile;
    @BindView(R.id.newMsgText)
    TextView newMsgText;
    @BindView(R.id.btnBack)
    ImageButton btnBack;

    private String num;
    private String type;
    private ChatRoomAdapter adapter;
    private ChatRoomPresenter presenter;
    private String profileImage;
    private String userNickname;
    private String userNum;
    private userData userdata;
    private ProgressDialog progressDialog;
    private int basicPixel = 0;
    private int keyboardPixel = 0;
    private int resultPixel = 0;
    private int overallXScroll = 0;
    private boolean keyboard = false;
    private boolean setScroll = true;
    private boolean firstStart;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.selectImage(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatRoomNum = "";
        chatWaitNum = "";
        chatUserNum = "";
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatRoomNum = "";
        chatUserNum = "";
        chatWaitNum = num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("message"));
        firstStart = false;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        imageNum = new ArrayList<>();
        userdata = new userData(this);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

        type = intent.getStringExtra("type");
        if (type.equals("fcm")) {

            fcmGetMessage(num);
        } else {

            profileImage = intent.getStringExtra("profileImage");
            userNickname = intent.getStringExtra("nickname");
            userNum = intent.getStringExtra("userNum");
            chatUserNum = userNum;
            initActivity();
        }

    }

    private void initActivity() {
        nickname.setPaintFlags(nickname.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        nickname.setText(userNickname);
        btnSend.setColorFilter(Color.parseColor("#ff9999"));
        btnAdd.setColorFilter(Color.parseColor("#ff9999"));
        btnAdd.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        newMsgLayout.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        init();
        presenter = new ChatRoomPresenter(this, adapter, this, userNum);
        presenter.getMessage(num);
        if(type.equals("fcm")){
            presenter.chatRead(num, userNum);
        }

        layout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            int height = layout.getRootView().getHeight();
            int wrapperHeight = layout.getHeight();
            int mDiff = height - wrapperHeight;

            if (mDiff > Util.convertDpToPixel(ChatRoomActivity.this, 200)) {
                if(!keyboard){
                    keyboard = true;
                    setScroll = false;
                    keyboardPixel = mDiff;
                    resultPixel = keyboardPixel - basicPixel;
                    chatRecyclerview.scrollBy(0, resultPixel);

                    setScroll = true;
                }


            } else {
                basicPixel = mDiff;
                if (keyboard) {
                    keyboard = false;
                    setScroll = false;

                    if (overallXScroll >= -resultPixel) {
                        chatRecyclerview.scrollBy(0, overallXScroll);

                    } else {
                        chatRecyclerview.scrollBy(0, -resultPixel);
                    }
                    setScroll = true;

                }
            }


        });
    }

    public void init() {
        chatRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoomAdapter(this, profileImage, userNickname, this);
        chatRecyclerview.setAdapter(adapter);

        chatRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (setScroll) {
                    overallXScroll = overallXScroll + dy;
                    if(newMsgLayout.getVisibility()==View.VISIBLE){
                        if(overallXScroll>=-500){
                            newMsgLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                if (!editMsg.getText().toString().equals("")) {
                    presenter.sendMessage(num, editMsg.getText().toString());
                    editMsg.setText("");
                }
                break;
            case R.id.btnAdd:
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("사진");
                ListItems.add("동영상");
                ListItems.add("영상통화");
                final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(items, (dialog, pos) -> {
                    String selectedText = items[pos].toString();
                    switch (selectedText) {
                        case "사진":
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 0);
                            break;
                        case "동영상":
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.setType("video/*");
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(i, 1);
                            break;
                        case "영상통화":
                            int roomNum = (new Random()).nextInt(100000000);
                            presenter.applyFace(Integer.toString(roomNum));
                            WebrtcConnect webrtcConnect = new WebrtcConnect(this);
                            webrtcConnect.connectToRoom(Integer.toString(roomNum),false,false,false,0,userNum);
                            break;
                    }
                });
                builder.show();

                break;
            case R.id.newMsgLayout:
                newMsgLayout.setVisibility(View.INVISIBLE);
                scroll();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatWaitNum = "";
        chatRoomNum = num;
        userdata.resetMsg(num);
        if(!type.equals("fcm")){
            presenter.chatRead(num, userNum);

        }else{
            if(firstStart){
                presenter.chatRead(num, userNum);
            }else{
                firstStart = true;
            }
        }
    }



    @SuppressLint("SetTextI18n")
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            assert type != null;
            if (type.equals("chatRead")) {
                adapter.readMessage();
            } else if(type.equals("disconnect")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                builder.setTitle("상대방이 연결을 해제했습니다");
                builder.setNegativeButton("확인",
                        (dialog, which) -> {finish();});
                builder.show();
            } else {
                Log.d("checkLog", "receivemessage");
                String msg = intent.getStringExtra("msg");
                String msgTime = intent.getStringExtra("msgTime");
                Log.d("chatRoomLog", type + "  " + msg + "  " + msgTime);
                presenter.receiveMessage(type, msg, msgTime);
                if (!chatRoomNum.equals("")) {
                    presenter.chatRead(num, userNum);
                }

            }

        }
    };

    @Override
    public void scroll() {
        chatRecyclerview.scrollToPosition(Objects.requireNonNull(chatRecyclerview.getAdapter()).getItemCount() - 1);
        overallXScroll = 0;
    }

    @Override
    public void getScroll(String profile,String msg) {
        if(-500<=overallXScroll){
            scroll();
        }else{
            newMsgText.setText(msg);
            Glide.with(this).load(profile).into(newMsgProfile);
            if(newMsgLayout.getVisibility()==View.INVISIBLE){
                newMsgLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }


    public void fcmGetMessage(String num) {

        Call<ResponseBody> res = NetRetrofit.getInstance().getService().fcmChatRoom(num, userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject Object = new JSONObject(result);

                    profileImage = Object.getString("profileImage");
                    userNickname = Object.getString("nickname");
                    userNum = Object.getString("userNum");
                    chatUserNum = userNum;
                    initActivity();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());

            }
        });
    }
}
