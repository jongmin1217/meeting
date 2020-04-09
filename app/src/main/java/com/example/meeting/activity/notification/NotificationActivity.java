package com.example.meeting.activity.notification;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.recyclerview.NotificationAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class NotificationActivity extends AppCompatActivity implements NotificationView{

    @BindView(R.id.noConfigText)
    TextView noConfigText;
    @BindView(R.id.ConfigText)
    TextView ConfigText;
    @BindView(R.id.noConfigRecyclerview)
    RecyclerView noConfigRecyclerview;
    @BindView(R.id.ConfigRecyclerview)
    RecyclerView ConfigRecyclerview;

    private NotificationPresenter presenter;
    private NotificationAdapter noConfingAdapter,configAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        noConfigText.setPaintFlags(noConfigText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        ConfigText.setPaintFlags(ConfigText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        init();
        presenter = new NotificationPresenter(this,this,noConfingAdapter,configAdapter);

        presenter.getNotification();
    }

    private void init() {
        noConfigRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        noConfingAdapter = new NotificationAdapter(this);
        noConfigRecyclerview.setAdapter(noConfingAdapter);

        ConfigRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        configAdapter = new NotificationAdapter(this);
        ConfigRecyclerview.setAdapter(configAdapter);
    }

    @Override
    public void noNoConfig() {
        noConfigText.setVisibility(GONE);
    }

    @Override
    public void noConfig() {
        ConfigText.setVisibility(GONE);
    }
}
