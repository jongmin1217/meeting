package com.example.meeting.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class square_imageview extends AppCompatImageView {
    public square_imageview(Context context) {
        super(context);
    }

    public square_imageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public square_imageview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        setMeasuredDimension(width, width);

    }
}
