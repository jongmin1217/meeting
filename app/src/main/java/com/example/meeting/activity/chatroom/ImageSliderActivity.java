package com.example.meeting.activity.chatroom;

import android.content.Intent;
import android.os.Bundle;

import com.example.meeting.R;
import com.example.meeting.adapter.ChatImageSlider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import static com.example.meeting.activity.chatroom.ChatRoomPresenter.imageResult;

public class ImageSliderActivity extends AppCompatActivity implements ImageSliderView {
    private ChatImageSlider chatImageSlider;
    ViewPager viewPager;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        Intent intent = getIntent();
        int position = intent.getIntExtra("num",0);

        viewPager = findViewById(R.id.view);
        layout = findViewById(R.id.layout);

        chatImageSlider = new ChatImageSlider(this, imageResult,this);
        viewPager.setAdapter(chatImageSlider);
        viewPager.setCurrentItem(position);



    }


}
