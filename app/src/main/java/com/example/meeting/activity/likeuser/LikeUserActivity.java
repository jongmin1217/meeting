package com.example.meeting.activity.likeuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.example.meeting.R;
import com.example.meeting.recyclerview.UserAdapter;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LikeUserActivity extends AppCompatActivity implements LikeUserView {

    @BindView(R.id.userRecyclerView)
    RecyclerView userRecyclerView;

    LikeUserPresenter presenter;
    UserAdapter adapter;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_user);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Intent intent = getIntent();
        num = Objects.requireNonNull(intent.getExtras()).getString("num");

        init();
        presenter = new LikeUserPresenter(this, adapter);

        presenter.getUserList(num);
    }

    private void init() {
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this);
        userRecyclerView.setAdapter(adapter);

    }
}
