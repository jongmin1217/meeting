package com.example.meeting.activity.previousrecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.example.meeting.R;
import com.example.meeting.recyclerview.recommendationAdapter;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviousRecommendationActivity extends AppCompatActivity implements PreviousRecommendationView{

    @BindView(R.id.previousRecommendationRecyclerview)
    RecyclerView previousRecommendationRecyclerview;

    private String num;
    private recommendationAdapter adapter;
    private PreviousRecommendationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Intent intent = getIntent();
        num = Objects.requireNonNull(intent.getExtras()).getString("num");

        init();
        presenter = new PreviousRecommendationPresenter(this,this,adapter);
        presenter.getPreviousRecommendation(num);

    }

    private void init(){
        previousRecommendationRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new recommendationAdapter(this);
        previousRecommendationRecyclerview.setAdapter(adapter);
    }
}
