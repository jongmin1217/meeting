package com.example.meeting.activity.postwrite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.custom.ImageSelect;
import com.soundcloud.android.crop.Crop;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class PostWriteActivity extends AppCompatActivity implements PostWriteView, View.OnClickListener {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageButton)
    ImageButton imageButton;
    @BindView(R.id.btnDone)
    ImageButton btnDone;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.editText)
    EditText editText;

    PostWritePresenter presenter;
    ImageSelect imageSelect;

    private Uri imageUri;

    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new PostWritePresenter(this,this);
        imageSelect = new ImageSelect(this);

        imageView.setVisibility(GONE);

        imageButton.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                imageSelect.selectImage();
                break;
            case R.id.btnDone:
                if(imageUri==null||editText.getText().toString().equals("")){
                    Toast.makeText(this, "게시글을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    presenter.postSave(editText.getText().toString(),imageUri);
                }
                break;
            case R.id.btnBack:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM: {
                imageSelect.fromAlbum(data);
                break;
            }
            case FROM_CAMERA: {
                imageSelect.fromCamera(data);
                break;
            }
            case Crop.REQUEST_CROP:{
                if(imageView.getVisibility()==GONE){
                    imageView.setVisibility(View.VISIBLE);
                }
                imageUri = imageSelect.fromCrop(data);
                imageView.setImageURI(imageUri);
                break;
            }
        }
    }

    @Override
    public void postWriteSucces() {
        Toast.makeText(this, "게시글을 작성하였습니다", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("postwrite","done");
        setResult(RESULT_OK,intent);
        finish();
    }
}
