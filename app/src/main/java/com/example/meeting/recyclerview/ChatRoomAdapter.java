package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meeting.R;
import com.example.meeting.activity.chatroom.ChatRoomView;
import com.example.meeting.activity.chatroom.ImageSliderActivity;
import com.example.meeting.activity.chatroom.VideoPlayActivity;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.Util;
import com.example.meeting.model.ChatImageData;
import com.example.meeting.model.ChatRoomData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.meeting.activity.chatroom.ChatRoomPresenter.imageResult;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatRoomData> listData = new ArrayList<>();
    private ArrayList<Integer> unReadList = new ArrayList<>();

    private Context context;
    private String userProfileImage, userNickname;
    private GetTime getTime;
    private ChatRoomView view;

    public ChatRoomAdapter(Context context, String profileImage, String nickname, ChatRoomView view) {
        this.context = context;
        this.view = view;
        userProfileImage = profileImage;
        userNickname = nickname;
        getTime = new GetTime();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_notice, parent, false);
            return new ItemViewHolderNotice(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_my_msg, parent, false);
            return new ItemViewHolderMyMsg(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_msg, parent, false);
            return new ItemViewHolderUserMsg(view);
        } else if (viewType == 3) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_my_image, parent, false);
            return new ItemViewHolderMyImage(view);
        } else if (viewType == 4) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_image, parent, false);
            return new ItemViewHolderUserImage(view);
        } else if (viewType == 5) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_my_images, parent, false);
            return new ItemViewHolderMyImages(view);
        } else if (viewType == 6) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_images, parent, false);
            return new ItemViewHolderUserImages(view);
        } else if (viewType == 7) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_first_msg, parent, false);
            return new ItemViewHolderFirstMsg(view);
        } else if (viewType == 8) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_my_video, parent, false);
            return new ItemViewHolderMyVideo(view);
        } else if (viewType == 9) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_video, parent, false);
            return new ItemViewHolderUserVideo(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolderNotice) {
            ((ItemViewHolderNotice) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderMyMsg) {
            ((ItemViewHolderMyMsg) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderUserMsg) {
            ((ItemViewHolderUserMsg) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderMyImage) {
            ((ItemViewHolderMyImage) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderUserImage) {
            ((ItemViewHolderUserImage) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderMyImages) {
            ((ItemViewHolderMyImages) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderUserImages) {
            ((ItemViewHolderUserImages) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderFirstMsg) {
            ((ItemViewHolderFirstMsg) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderMyVideo) {
            ((ItemViewHolderMyVideo) holder).onBind(listData.get(position));
        } else if (holder instanceof ItemViewHolderUserVideo) {
            ((ItemViewHolderUserVideo) holder).onBind(listData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getItemViewType();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<ChatRoomData> listData) {
        this.listData = listData;
        notifyDataSetChanged();
        view.scroll();
    }

    public void insertItem(ChatRoomData data) {
        if (data.getItemViewType() == 1 && listData.get(getItemCount() - 1).getItemViewType() == 1 && data.getMsgTime().equals(listData.get(getItemCount() - 1).getMsgTime())) {
            listData.get(getItemCount() - 1).setTimeType(false);
            notifyItemChanged(getItemCount() - 1);
        } else if (data.getItemViewType() == 2 && listData.get(getItemCount() - 1).getItemViewType() == 2 && data.getMsgTime().equals(listData.get(getItemCount() - 1).getMsgTime())) {
            listData.get(getItemCount() - 1).setTimeType(false);
            notifyItemChanged(getItemCount() - 1);
        }
        listData.add(data);
        notifyItemInserted(getItemCount());

        if (data.getItemViewType() == 1 || data.getItemViewType() == 3 || data.getItemViewType() == 5) {
            view.scroll();
        } else if (data.getItemViewType() == 2) {
            view.getScroll(userProfileImage, data.getMsg());
        } else if(data.getItemViewType()==9){
            view.getScroll(userProfileImage, "동영상");
        }else{
            view.getScroll(userProfileImage, "사진");
        }

    }

    public void readMessage() {
        for (int i = 0; i < unReadList.size(); i++) {
            listData.get(unReadList.get(i)).setRead(true);
            notifyItemChanged(unReadList.get(i));
        }
        unReadList.clear();
    }

    public static int getItemNum(String url) {
        for (int i = 0; i < imageResult.size(); i++) {
            if (imageResult.get(i).equals(url)) {
                return i;
            }
        }
        return 0;
    }


    class ItemViewHolderFirstMsg extends RecyclerView.ViewHolder {
        private ChatRoomData chatRoomData;
        private TextView notice;

        ItemViewHolderFirstMsg(View itemView) {
            super(itemView);
            notice = itemView.findViewById(R.id.notice);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            notice.setText(chatRoomData.getMsg());
        }
    }

    class ItemViewHolderNotice extends RecyclerView.ViewHolder {
        private ChatRoomData chatRoomData;
        private TextView notice;

        ItemViewHolderNotice(View itemView) {
            super(itemView);
            notice = itemView.findViewById(R.id.notice);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            notice.setText(chatRoomData.getMsg());
        }
    }

    class ItemViewHolderMyMsg extends RecyclerView.ViewHolder {
        private ChatRoomData chatRoomData;
        private TextView myMessage, myTime, cnt;

        ItemViewHolderMyMsg(View itemView) {
            super(itemView);
            myMessage = itemView.findViewById(R.id.myMessage);
            myTime = itemView.findViewById(R.id.myTime);
            cnt = itemView.findViewById(R.id.cnt);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            if (!chatRoomData.isRead()) {
                cnt.setVisibility(View.VISIBLE);
                unReadList.add(getAdapterPosition());
            } else {
                cnt.setVisibility(View.INVISIBLE);
            }
            if (chatRoomData.isTimeType()) {
                myTime.setVisibility(View.VISIBLE);
            } else {
                myTime.setVisibility(View.INVISIBLE);
            }
            myMessage.setText(chatRoomData.getMsg());
            myTime.setText(chatRoomData.getMsgTime());
        }
    }

    class ItemViewHolderUserMsg extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView profileImage;
        private TextView nickname, userMessage, userTime;

        ItemViewHolderUserMsg(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            userMessage = itemView.findViewById(R.id.userMessage);
            userTime = itemView.findViewById(R.id.userTime);

            profileImage.setOnClickListener(this);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;

            Picasso.get().load(userProfileImage).into(profileImage);
            nickname.setText(userNickname);
            userMessage.setText(chatRoomData.getMsg());
            userTime.setText(chatRoomData.getMsgTime());
            if (chatRoomData.isTimeType()) {
                userTime.setVisibility(View.VISIBLE);
            } else {
                userTime.setVisibility(View.INVISIBLE);
            }
            if (!chatRoomData.isTimeType()) {
                if (listData.get(getAdapterPosition() - 1).getItemViewType() != 2) {
                    nickname.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                } else if (listData.get(getAdapterPosition() - 1).isTimeType()) {
                    nickname.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                } else {
                    nickname.setVisibility(View.GONE);
                    profileImage.setVisibility(View.INVISIBLE);
                }
            } else {
                if (listData.get(getAdapterPosition() - 1).isTimeType()) {
                    nickname.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                } else if (listData.get(getAdapterPosition() - 1).getItemViewType() != 2) {
                    nickname.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                } else {
                    nickname.setVisibility(View.GONE);
                    profileImage.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            userProfile(chatRoomData.getUserNum());
        }
    }

    class ItemViewHolderMyImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView myImage;
        private TextView myTime, cnt;

        ItemViewHolderMyImage(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.myImage);
            myTime = itemView.findViewById(R.id.myImageTime);
            cnt = itemView.findViewById(R.id.cnt);

            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_custom2);
            myImage.setBackground(drawable);
            myImage.setClipToOutline(true);

            myImage.setOnClickListener(this);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            if (!chatRoomData.isRead()) {
                cnt.setVisibility(View.VISIBLE);
                unReadList.add(getAdapterPosition());
            } else {
                cnt.setVisibility(View.INVISIBLE);
            }

            String[] array = chatRoomData.getImageUrlList().get(0).split("/");
            File file = new File("data/data/com.example.meeting/app_imageDir/" + array[4]);
            if (file.exists()) {
                Glide.with(context).load("data/data/com.example.meeting/app_imageDir/" + array[4]).override(250, 250).into(myImage);
            } else {
                Glide.with(context).load(chatRoomData.getImageUrlList().get(0)).placeholder(new ColorDrawable(Color.BLACK)).override(150, 150).into(myImage);
            }

            myTime.setText(chatRoomData.getMsgTime());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.myImage) {
                Intent intent = new Intent(context, ImageSliderActivity.class);
                if (chatRoomData.getImageType().equals("uri")) {
                    intent.putExtra("num", getItemNum(String.valueOf(chatRoomData.getImageUri())));
                } else {
                    intent.putExtra("num", getItemNum(chatRoomData.getImageUrlList().get(0)));
                }
                context.startActivity(intent);
            }
        }
    }

    class ItemViewHolderUserImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView profileImage, userImage;
        private TextView nickname, userTime;

        ItemViewHolderUserImage(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            userImage = itemView.findViewById(R.id.userImage);
            userTime = itemView.findViewById(R.id.userImageTime);

            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_custom2);
            userImage.setBackground(drawable);
            userImage.setClipToOutline(true);

            profileImage.setOnClickListener(this);
            userImage.setOnClickListener(this);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;

            Picasso.get().load(userProfileImage).into(profileImage);
            nickname.setText(userNickname);

            String[] array = chatRoomData.getImageUrlList().get(0).split("/");
            File file = new File("data/data/com.example.meeting/app_imageDir/" + array[4]);
            if (file.exists()) {
                Glide.with(context).load("data/data/com.example.meeting/app_imageDir/" + array[4]).override(250, 250).into(userImage);
            } else {
                Glide.with(context).load(chatRoomData.getImageUrlList().get(0)).placeholder(new ColorDrawable(Color.BLACK)).override(150, 150).into(userImage);
            }
            userTime.setText(chatRoomData.getMsgTime());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.userImage:
                    Intent intent = new Intent(context, ImageSliderActivity.class);
                    intent.putExtra("num", getItemNum(chatRoomData.getImageUrlList().get(0)));
                    context.startActivity(intent);
                    break;
                case R.id.profileImage:
                    userProfile(chatRoomData.getUserNum());
                    break;
            }
        }
    }

    class ItemViewHolderMyImages extends RecyclerView.ViewHolder {
        private ChatRoomData chatRoomData;
        private RecyclerView recyclerView;
        private ChatImageAdapter adapter;
        private TextView myTime, cnt;

        ItemViewHolderMyImages(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.myImage);
            myTime = itemView.findViewById(R.id.myImageTime);
            cnt = itemView.findViewById(R.id.cnt);

        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            if (!chatRoomData.isRead()) {
                cnt.setVisibility(View.VISIBLE);
                unReadList.add(getAdapterPosition());
            } else {
                cnt.setVisibility(View.INVISIBLE);
            }

            recyclerView.setLayoutManager(Util.getLayout(chatRoomData, context));
            adapter = new ChatImageAdapter(context);
            recyclerView.setAdapter(adapter);

            ArrayList<ChatImageData> dataList = new ArrayList<>();


            ArrayList<String> imageList = chatRoomData.getImageUrlList();
            for (int i = 0; i < imageList.size(); i++) {
                ChatImageData data = new ChatImageData();
                data.setType("url");
                data.setUrl(imageList.get(i));
                data.setPosition(i);
                data.setImageNum(chatRoomData.getImageNum());
                dataList.add(data);
            }
            adapter.getItem(dataList);

            myTime.setText(chatRoomData.getMsgTime());
        }
    }

    class ItemViewHolderUserImages extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView profileImage;
        private TextView nickname, userTime;
        private RecyclerView recyclerView;
        private ChatImageAdapter adapter;

        ItemViewHolderUserImages(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            recyclerView = itemView.findViewById(R.id.userImage);
            userTime = itemView.findViewById(R.id.userImageTime);

            profileImage.setOnClickListener(this);

        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;

            Picasso.get().load(userProfileImage).into(profileImage);
            nickname.setText(userNickname);
            userTime.setText(chatRoomData.getMsgTime());

            recyclerView.setLayoutManager(Util.getLayout(chatRoomData, context));
            adapter = new ChatImageAdapter(context);
            recyclerView.setAdapter(adapter);

            ArrayList<ChatImageData> dataList = new ArrayList<>();
            ArrayList<String> imageList = chatRoomData.getImageUrlList();
            for (int i = 0; i < imageList.size(); i++) {
                ChatImageData data = new ChatImageData();
                data.setType("url");
                data.setUrl(imageList.get(i));
                data.setImageNum(chatRoomData.getImageNum());
                data.setPosition(i);
                dataList.add(data);
            }
            adapter.getItem(dataList);

        }

        @Override
        public void onClick(View view) {
            userProfile(chatRoomData.getUserNum());
        }
    }

    private void userProfile(String num) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("num", num);
        context.startActivity(intent);
    }

    class ItemViewHolderMyVideo extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView myImage;
        private TextView myTime, cnt;
        private ImageButton btnVideoPlay;

        ItemViewHolderMyVideo(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.myImage);
            myTime = itemView.findViewById(R.id.myImageTime);
            cnt = itemView.findViewById(R.id.cnt);
            btnVideoPlay = itemView.findViewById(R.id.btnVideoPlay);

            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_custom2);
            myImage.setBackground(drawable);
            myImage.setClipToOutline(true);

            btnVideoPlay.setOnClickListener(this);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;
            if (!chatRoomData.isRead()) {
                cnt.setVisibility(View.VISIBLE);
                unReadList.add(getAdapterPosition());
            } else {
                cnt.setVisibility(View.INVISIBLE);
            }
            Glide.with(context).load("http://13.209.4.115/chatvideo/"+chatRoomData.getImage()).placeholder(new ColorDrawable(Color.BLACK)).into(myImage);
            Log.d("asdasdasd","http://13.209.4.115/chatvideo/"+chatRoomData.getImage());
            myTime.setText(chatRoomData.getMsgTime());
        }

        @Override
        public void onClick(View view) {
            String videoUrl = "http://13.209.4.115/chatvideo/"+chatRoomData.getImage().replace(".jpg", "");
            Intent intent = new Intent(context, VideoPlayActivity.class);
            intent.putExtra("url",videoUrl);
            context.startActivity(intent);
        }
    }

    class ItemViewHolderUserVideo extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatRoomData chatRoomData;
        private ImageView profileImage, userImage;
        private TextView nickname, userTime;
        private ImageButton btnVideoPlay;

        ItemViewHolderUserVideo(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            userImage = itemView.findViewById(R.id.userImage);
            userTime = itemView.findViewById(R.id.userImageTime);
            btnVideoPlay = itemView.findViewById(R.id.btnVideoPlay);

            GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imageview_custom2);
            userImage.setBackground(drawable);
            userImage.setClipToOutline(true);

            profileImage.setOnClickListener(this);
            btnVideoPlay.setOnClickListener(this);
        }

        void onBind(ChatRoomData chatRoomData) {
            this.chatRoomData = chatRoomData;

            Picasso.get().load(userProfileImage).into(profileImage);
            nickname.setText(userNickname);


            Glide.with(context).load("http://13.209.4.115/chatvideo/"+chatRoomData.getImage()).placeholder(new ColorDrawable(Color.BLACK)).into(userImage);

            userTime.setText(chatRoomData.getMsgTime());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.userImage:
                    Intent intent = new Intent(context, ImageSliderActivity.class);
                    intent.putExtra("num", getItemNum(chatRoomData.getImageUrlList().get(0)));
                    context.startActivity(intent);
                    break;
                case R.id.btnVideoPlay:
                    String videoUrl = "http://13.209.4.115/chatvideo/"+chatRoomData.getImage().replace(".jpg", "");
                    Intent videoIntent = new Intent(context, VideoPlayActivity.class);
                    videoIntent.putExtra("url",videoUrl);
                    context.startActivity(videoIntent);
                    break;
            }
        }
    }


}
