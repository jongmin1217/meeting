package com.example.meeting.activity.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.main.MainActivity;
import com.example.meeting.activity.profileedit.ProfileEditActivity;
import com.example.meeting.adapter.slider_adapter;
import com.example.meeting.custom.BusProvider;
import com.example.meeting.event.ActivityResultEvent;
import com.example.meeting.recyclerview.PostImageAdapter;
import com.squareup.otto.Subscribe;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MyinfoFragment extends Fragment implements MyinfoView, View.OnClickListener {

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
    @BindView(R.id.likeText)
    TextView likeText;
    @BindView(R.id.btnProfileEdit)
    ImageButton btnProfileEdit;
    @BindView(R.id.postImageRecyclerview)
    RecyclerView postImageRecyclerview;


    private MyinfoPresenter presenter;
    private PostImageAdapter adapter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_myinfo, container, false);
        ButterKnife.bind(this,view);
        BusProvider.getInstance().register(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        likeText.setText("편집");

        init();
        presenter = new MyinfoPresenter(this,adapter);

        presenter.getProfile();
        btnProfileEdit.setOnClickListener(this);
        return view;
    }

    private void init(){
        postImageRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PostImageAdapter();
        postImageRecyclerview.setAdapter(adapter);
    }

    @Override
    public void getProfile(slider_adapter slider_adapter,String nickname,String area,String age,String height,String form,
                           String smoking,String drinking,String hobby,String personality,String idealType) {
        viewPager.setAdapter(slider_adapter);
        viewPager.setCurrentItem(0);
        toolbar.setTitle(nickname);areaInput.setText(area);ageInput.setText(age);heightInput.setText(height);
        formInput.setText(form);smokingInput.setText(smoking);drinkingInput.setText(drinking);hobbyInput.setText(hobby);
        personalityInput.setText(personality);idealTypeInput.setText(idealType);
    }

    @Override
    public Activity getFragmentActivity() {
        Activity activity = getActivity();
        return activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnProfileEdit:
                    Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                    startActivityForResult(intent,1000);
                break;
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    String result = data.getStringExtra("edit");
                    if(result.equals("done")){
                        presenter.getProfile();
                    }
                    break;
                case 2000:
                    if(((MainActivity) Objects.requireNonNull(getActivity())).getFragment()){
                        String postresult = data.getStringExtra("postwrite");
                        if(postresult.equals("done")){
                            presenter.getMyPostList();
                        }
                    }
                    break;
                case 3000:
                    if(data.getBooleanExtra("delete",false)){
                        adapter.removeItem(data.getIntExtra("position",0));
                    }
                    break;
            }
        }
    }
}
