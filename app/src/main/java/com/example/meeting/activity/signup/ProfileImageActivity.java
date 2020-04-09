package com.example.meeting.activity.signup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import com.example.meeting.activity.main.MainActivity;
import com.example.meeting.R;
import com.example.meeting.model.ProfileImageData;
import com.example.meeting.recyclerview.ProfileImageAdapter;
import com.soundcloud.android.crop.Crop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

import static com.example.meeting.activity.login.LoginActivity.loginActivity;

public class ProfileImageActivity extends AppCompatActivity implements ProfileImageView {

    ProfileImagePresenter presenter;
    ProfileImageAdapter adapter;

    Activity activity;

    String email;
    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileimage);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        presenter = new ProfileImagePresenter(this);
        presenter.read_cascade_file();
        init();

        presenter.getData();

    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.imageRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ProfileImageAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    public void btnComplete(View view){
        presenter.profileSave(adapter,email);
    }

    public void btnCancel(View view){
        finish();
    }

    @Override
    public void addImage(Uri uri) {
        ProfileImageData image = new ProfileImageData();
        image.setImage(uri);
        adapter.addItem(image);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeImage(int position) {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void profileUploadSucces() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type","signup");
        startActivity(intent);
        finish();
        loginActivity.finish();
    }

    @Override
    public Activity getActivity()
    {
        this.activity = this;
        return activity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM : {
                presenter.fromAlbum(data);
                break;
            }
            case FROM_CAMERA : {
                presenter.fromCamera(data);
                break;
            }
            case Crop.REQUEST_CROP:{
                presenter.fromCrop(data);
                break;
            }
        }
    }



}
