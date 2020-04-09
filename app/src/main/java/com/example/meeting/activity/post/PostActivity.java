package com.example.meeting.activity.post;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.activity.likeuser.LikeUserActivity;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.custom.DoubleClickListener;
import com.example.meeting.custom.ItemSwipeHelperCallback;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.ComentAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity implements PostView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static String postNumber;
    @SuppressLint("StaticFieldLeak")
    public static PostActivity postActivity;

    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.likeAnimater)
    ImageView likeAnimater;
    @BindView(R.id.postImage)
    ImageView postImage;
    @BindView(R.id.btnLike)
    ImageButton btnLike;
    @BindView(R.id.btnPostDelete)
    ImageButton btnPostDelete;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.likeNum)
    TextView likeNum;
    @BindView(R.id.postText)
    TextView postText;
    @BindView(R.id.postTime)
    TextView postTime;
    @BindView(R.id.noComentText)
    TextView noComentText;
    @BindView(R.id.editComent)
    EditText editComent;
    @BindView(R.id.btnComent)
    Button btnComent;
    @BindView(R.id.btnNewComent)
    Button btnNewComent;
    @BindView(R.id.comentRecyclerView)
    RecyclerView comentRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    InputMethodManager imm;

    private Animation scale;
    private Animation move, move2;
    private PostPresenter presenter;
    private String num, writerNum;
    boolean like;
    private ComentAdapter adapter;
    private int position;
    private userData userdata;
    private String image;
    private boolean animation;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        scale = AnimationUtils.loadAnimation(this, R.animator.scale);
        move = AnimationUtils.loadAnimation(this, R.animator.move);
        move2 = AnimationUtils.loadAnimation(this, R.animator.move2);
        userdata = new userData(this);
        Intent intent = getIntent();
        num = Objects.requireNonNull(intent.getExtras()).getString("num");
        if(intent.getExtras().getString("type")!=null){
            if(Objects.requireNonNull(intent.getExtras().getString("type")).equals("fcm")){
                userdata.removeNotification();
            }
        }
        postNumber = num;
        postActivity = this;
        init();
        position = intent.getExtras().getInt("position");
        presenter = new PostPresenter(this, this, adapter);
        presenter.getPostInfo(num);
        presenter.likeConf(num);
        presenter.getPostComent(num);
        btnLike.setOnClickListener(this);
        likeNum.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        nickname.setOnClickListener(this);
        btnComent.setOnClickListener(this);
        btnPostDelete.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        btnNewComent.setOnClickListener(this);
        likeAnimater.setColorFilter(Color.parseColor("#ffffff"));
        postImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                postLikeClick(num, like);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("post"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            btnNewComent.setVisibility(View.VISIBLE);
            btnNewComent.startAnimation(move);
            animation = true;
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(animation){

                        btnNewComent.startAnimation(move2);
                    }

                }
            };
            handler.sendEmptyMessageDelayed(0, 3000); //3초후 화면전환
        }
    };


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    void postLikeClick(String num, boolean like) {
        likeNum.setText(presenter.changeLikeNum(likeNum.getText().toString(), like));
        presenter.postLike(num, like, writerNum, image);
        likeAnimater.setVisibility(View.VISIBLE);
        likeAnimater.startAnimation(scale);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLike:
                postLikeClick(num, like);
                break;
            case R.id.likeNum:
                if (likeNum.getText().toString().equals("0")) {
                    Toast.makeText(this, "좋아하는 유저가 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentUser = new Intent(this, LikeUserActivity.class);
                    intentUser.putExtra("num", num);
                    startActivity(intentUser);
                }

                break;
            case R.id.profileImage:
            case R.id.nickname:
                if (!writerNum.equals(userdata.getNum())) {
                    Intent intentProfileImage = new Intent(this, UserProfileActivity.class);
                    intentProfileImage.putExtra("num", writerNum);
                    startActivity(intentProfileImage);
                }

                break;
            case R.id.btnComent:
                if (!editComent.getText().toString().equals("")) {
                    presenter.comentInsert(num, editComent.getText().toString(), writerNum, image);
                    editComent.setText("");
                    hideKeyboard();
                }
                break;
            case R.id.btnPostDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("게시글을 삭제하시겠습니까?");
                builder.setPositiveButton("취소",
                        (dialog, which) -> {
                        });
                builder.setNegativeButton("확인",
                        (dialog, which) -> {
                            presenter.postDelete(num);
                            Intent intent = new Intent();
                            intent.putExtra("delete", true);
                            intent.putExtra("position", position);
                            setResult(RESULT_OK, intent);
                            finish();
                        });
                builder.show();
                break;
            case R.id.btnNewComent:
                Log.d("socketServerLog", "click");
                animation = false;
                btnNewComent.clearAnimation();
                btnNewComent.setVisibility(View.INVISIBLE);
                presenter.getPostComent(num);
                scrollView.smoothScrollTo(0,0);
                break;
        }
    }

    @Override
    public void getPostInfo(String writerNum, String profileImage, String nickname, String image, String postText, String postTime, String postLike, boolean myPost) {
        this.writerNum = writerNum;
        this.image = image;
        Picasso.get().load(profileImage).into(this.profileImage);
        Picasso.get().load(image).into(postImage);
        this.nickname.setText(nickname);
        this.postText.setText(postText);
        this.postTime.setText(postTime);
        likeNum.setText(postLike);
        if (myPost) {
            btnPostDelete.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void likeTrue() {
        btnLike.setImageResource(R.drawable.favorite);
        btnLike.setColorFilter(Color.parseColor("#ff2222"));
        like = true;
    }

    @Override
    public void likeFalse() {
        btnLike.setImageResource(R.drawable.non_favorite);
        btnLike.setColorFilter(Color.parseColor("#000000"));
        like = false;
    }

    @Override
    public void noComent() {
        noComentText.setVisibility(View.VISIBLE);
    }

    @Override
    public void invisible() {
        noComentText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void removeComent(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("댓글을 삭제하시겠습니까?");
        builder.setPositiveButton("취소",
                (dialog, which) -> adapter.returnItem());
        builder.setNegativeButton("확인",
                (dialog, which) -> adapter.removeItem(position, presenter));
        builder.show();
    }

    @Override
    public void endRefreshing(boolean coment) {
        if (coment) {
            noComentText.setVisibility(View.INVISIBLE);
        } else {
            noComent();

        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(editComent.getWindowToken(), 0);
    }

    private void init() {
        comentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComentAdapter(this, this);
        comentRecyclerView.setAdapter(adapter);

        ItemSwipeHelperCallback callback = new ItemSwipeHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(comentRecyclerView);
    }

    @Override
    public void onRefresh() {
        animation = false;
        btnNewComent.clearAnimation();
        btnNewComent.setVisibility(View.INVISIBLE);
        presenter.getPostInfo(num);
        presenter.getPostComent(num);
    }


}
