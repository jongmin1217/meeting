package com.example.meeting.activity.main.fragment;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.recyclerview.MoveAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoveFragment extends Fragment implements MoveView {

    @BindView(R.id.moveRecyclerView)
    RecyclerView moveRecyclerView;
    @BindView(R.id.text)
    TextView text;

    private MovePresenter presenter;
    private ProgressDialog progressDialog;
    private MoveAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_move, container, false);
        ButterKnife.bind(this, view);
        text.setPaintFlags(text.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        init();

        presenter = new MovePresenter(this,adapter);

        presenter.getMoveInfo();

        return view;
    }

    private void init() {
        moveRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MoveAdapter(getContext(), this);
        moveRecyclerView.setAdapter(adapter);
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
