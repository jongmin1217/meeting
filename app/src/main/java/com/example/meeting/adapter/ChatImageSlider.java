package com.example.meeting.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.meeting.R;
import com.example.meeting.activity.chatroom.ImageSliderView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ChatImageSlider extends PagerAdapter {
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    private ProgressDialog progressDialog;
    private ImageSliderView imageslideview;

    public ChatImageSlider(Context context, ArrayList<String> image, ImageSliderView view) {
        this.context = context;
        imageslideview = view;
        images = image;
    }

    @Override
    public int getCount() {
        return images.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_chat_image_slider, container, false);

        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = v.findViewById(R.id.imageView);
        ImageButton btnImageDownload = v.findViewById(R.id.btnImageDownload);
        ImageButton btnGalleryDownload = v.findViewById(R.id.btnGalleryDownload);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("downloading...");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        btnGalleryDownload.setColorFilter(Color.parseColor("#ffffff"));
        btnImageDownload.setColorFilter(Color.parseColor("#ffffff"));
        btnImageDownload.setPadding(10, 10, 10, 10);
        btnImageDownload.setScaleType(ImageView.ScaleType.FIT_XY);

        btnImageDownload.setOnClickListener(view -> {
            progressDialog.show();

            Glide.with(context).load(images.get(position)).asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            try {

                                String[] split = images.get(position).split("/");
                                Log.d("qweqweqwe", "imageName   " + split[4]);

                                Log.d("qweqweqwe", "start");
                                ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
                                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                File file = new File(directory, split[4]);
                                Log.d("qweqweqwe", "file make");
                                if (!file.exists()) {
                                    FileOutputStream fos = null;
                                    Log.d("qweqweqwe", "file make succes");

                                    Log.d("qweqweqwe", "rotate");
                                    fos = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    fos.flush();
                                    fos.close();

                                    Log.d("qweqweqwe", "end download");

                                    progressDialog.dismiss();
                                    btnImageDownload.setVisibility(View.GONE);
                                    btnImageDownload.setOnClickListener(null);
                                    imageView.setColorFilter(null);
                                    btnGalleryDownload.setVisibility(View.VISIBLE);
                                    Glide.with(context).load("data/data/com.example.meeting/app_imageDir/" + split[4]).into(imageView);

                                }
                            } catch (java.io.IOException e) {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }

                    });
        });

        btnGalleryDownload.setOnClickListener(view -> {
            progressDialog.show();
            String[] split = images.get(position).split("/");
            File file = new File("data/data/com.example.meeting/app_imageDir/" + split[4]);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = split[4];
            File saveFile = new File(myDir, fname);
            try {
                FileOutputStream out = new FileOutputStream(saveFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + myDir.getPath() + "/" + fname)));
                out.flush();
                out.close();
                Toast.makeText(context, "save image", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

        });

        String[] array = images.get(position).split("/");
        File file = new File("data/data/com.example.meeting/app_imageDir/" + array[4]);
        if (file.exists()) {
            btnGalleryDownload.setVisibility(View.VISIBLE);
            btnImageDownload.setVisibility(View.INVISIBLE);
            Glide.with(context).load("data/data/com.example.meeting/app_imageDir/" + array[4]).into(imageView);
        } else {
            btnGalleryDownload.setVisibility(View.INVISIBLE);
            btnImageDownload.setVisibility(View.VISIBLE);
            Glide.with(context).load(images.get(position)).override(150, 150).into(imageView);
            imageView.setColorFilter(Color.parseColor("#80000000"));
        }

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }


}
