package com.example.meeting.activity.main.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meeting.R;
import com.example.meeting.recyclerview.PostImageAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostFragment extends Fragment implements PostView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.postImageRecyclerviewPost)
    RecyclerView postImageRecyclerview;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private PostPresenter presenter;
    private PostImageAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_post, container, false);
        ButterKnife.bind(this,view);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        init();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        presenter = new PostPresenter(this,adapter,getContext());
        presenter.getPost();
        return view;
    }


    private void init(){
        postImageRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PostImageAdapter();
        postImageRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        presenter.getPost();
    }

    @Override
    public void endRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
