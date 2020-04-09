package com.example.meeting.activity.main.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.previousrecommendation.PreviousRecommendationActivity;
import com.example.meeting.custom.BusProvider;
import com.example.meeting.recyclerview.recommendationAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements HomeView, View.OnClickListener {


    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.recommendationRecyclerview)
    RecyclerView recommendationRecyclerview;
    @BindView(R.id.btnPreviousRecommendation)
    Button btnPreviousRecommendation;

    private recommendationAdapter adapter;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_home, container, false);
        ButterKnife.bind(this,view);
        BusProvider.getInstance().register(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        text.setPaintFlags(text.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        text2.setPaintFlags(text2.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        init();
        presenter = new HomePresenter(this,getContext(),adapter);
        presenter.getRecommendation();
        btnPreviousRecommendation.setOnClickListener(this);
        return view;
    }

    private void init(){
        recommendationRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new recommendationAdapter(getContext());
        recommendationRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPreviousRecommendation:
                Intent intent = new Intent(getContext(), PreviousRecommendationActivity.class);
                intent.putExtra("num",presenter.getUserNum());
                startActivity(intent);
                break;
        }
    }
}
