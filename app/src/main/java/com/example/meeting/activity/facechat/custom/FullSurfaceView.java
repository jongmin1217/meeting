package com.example.meeting.activity.facechat.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FullSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    RenderingThread renderingThread;


    public FullSurfaceView(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);

    }

    public FullSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void change(Bitmap bitmap){
        renderingThread = new RenderingThread(bitmap);
        renderingThread.start();
    }

    class RenderingThread extends Thread {
        Bitmap img_android;
        public RenderingThread(Bitmap bitmap) {
            Log.d("RenderingThread", "RenderingThread()");
            img_android = bitmap;
        }

        public void run() {
            Log.d("RenderingThread", "run()");
            Canvas canvas = null;
            while (true) {
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        canvas.drawBitmap(img_android, 0, 0, null);
                    }
                } finally {
                    if (canvas == null) return;
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // RenderingThread

//    class StartThread extends Thread {
//        Bitmap img_android;
//        public StartThread() {
//            Log.d("RenderingThread", "RenderingThread()");
//            img_android = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.android);
//        }
//
//        public void run() {
//            Log.d("RenderingThread", "run()");
//            Canvas canvas = null;
//            while (true) {
//                canvas = mHolder.lockCanvas();
//                try {
//                    synchronized (mHolder) {
//                        canvas.drawBitmap(img_android, 0, 0, null);
//                    }
//                } finally {
//                    if (canvas == null) return;
//                    mHolder.unlockCanvasAndPost(canvas);
//                }
//            }
//        }
//    } // RenderingThread
}
