package com.example.meeting.activity.chatlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.example.meeting.R;
import com.example.meeting.custom.ItemSwipeHelperCallback;
import com.example.meeting.recyclerview.ChatListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListActivity extends AppCompatActivity implements ChatListView{

    @BindView(R.id.chatListRecyclerview)
    RecyclerView chatListRecyclerview;

    private String num;
    private ChatListPresenter presenter;
    private ChatListAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("chatList"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("exit..");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

    }

    @Override
    protected void onResume(){
        super.onResume();
        init();
        presenter = new ChatListPresenter(this,adapter,this);
        presenter.getChatList(num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @SuppressLint("SetTextI18n")
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("chatlistlog","onreceive");
            presenter.getChatList(num);
        }
    };

    private void init() {
        chatListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatListAdapter(this,this);
        chatListRecyclerview.setAdapter(adapter);

        ItemSwipeHelperCallback callback = new ItemSwipeHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(chatListRecyclerview);

    }

    @Override
    public void removeChatList(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("채팅방을 나가시겠습니까?");
        builder.setPositiveButton("취소",
                (dialog, which) -> adapter.returnItem());
        builder.setNegativeButton("확인",
                (dialog, which) -> adapter.removeItem(position, presenter));
        builder.show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
}
