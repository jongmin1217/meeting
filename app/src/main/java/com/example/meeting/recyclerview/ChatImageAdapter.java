package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.meeting.R;
import com.example.meeting.activity.chatroom.ImageSliderActivity;
import com.example.meeting.custom.Util;
import com.example.meeting.model.ChatImageData;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatImageAdapter extends RecyclerView.Adapter<ChatImageAdapter.ItemViewHolder> {
    ArrayList<ChatImageData> listData = new ArrayList<>();
    private Context context;

    public ChatImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chat_image, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<ChatImageData> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ChatImageData chatImageData;
        private ImageView image;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

            GradientDrawable drawable= (GradientDrawable) context.getDrawable(R.drawable.image_view_custom);
            image.setBackground(drawable);
            image.setClipToOutline(true);

            image.setOnClickListener(this);
        }

        void onBind(ChatImageData chatImageData) {
            this.chatImageData = chatImageData;
            switch (chatImageData.getImageNum()){
                case 2:
                case 4:
                    itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 110);
                    itemView.requestLayout();
                    break;
                case 3:
                case 6:
                case 9:
                    itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 73);
                    itemView.requestLayout();
                    break;
                case 5:
                case 7:
                    if (chatImageData.getPosition() == 0 || chatImageData.getPosition() == 1 || chatImageData.getPosition() == 2) {
                        itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 73);
                        itemView.requestLayout();
                    } else {
                        itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 110);
                        itemView.requestLayout();
                    }
                    break;
                case 8:
                    if (chatImageData.getPosition() == 0 || chatImageData.getPosition() == 1 || chatImageData.getPosition() == 2
                            || chatImageData.getPosition() == 3|| chatImageData.getPosition() == 4|| chatImageData.getPosition() == 5) {
                        itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 73);
                        itemView.requestLayout();
                    } else {
                        itemView.getLayoutParams().width = (int) Util.convertDpToPixel(context, 110);
                        itemView.requestLayout();
                    }
                    break;

            }


            if (chatImageData.getType().equals("uri")) {
                image.setImageURI(chatImageData.getUri());
            } else {
                String[] array = chatImageData.getUrl().split("/");
                File file = new File("data/data/com.example.meeting/app_imageDir/" + array[4]);
                if (file.exists()) {
                    Glide.with(context).load("data/data/com.example.meeting/app_imageDir/" + array[4]).override(200, 200).into(image);
                } else {
                    Glide.with(context).load(chatImageData.getUrl()).placeholder(new ColorDrawable(Color.BLACK)).override(100, 100).into(image);
                }

            }
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.image) {
                Intent intent = new Intent(context, ImageSliderActivity.class);
                if (chatImageData.getType().equals("uri")) {
                    intent.putExtra("num", ChatRoomAdapter.getItemNum(String.valueOf(chatImageData.getUri())));
                } else {
                    intent.putExtra("num", ChatRoomAdapter.getItemNum(chatImageData.getUrl()));
                }
                context.startActivity(intent);
            }
        }
    }
}
