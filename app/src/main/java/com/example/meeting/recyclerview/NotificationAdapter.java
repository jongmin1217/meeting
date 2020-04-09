package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.post.PostActivity;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.custom.GetTime;
import com.example.meeting.model.NotificationData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder> {

    private ArrayList<NotificationData> listData = new ArrayList<>();
    private Context context;
    private GetTime getTime;

    public NotificationAdapter(Context context){
        this.context = context;
        getTime = new GetTime();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_notification, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    public void getItem(ArrayList<NotificationData> dataList){
        listData = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NotificationData notificationData;
        ImageView profileImage,image;
        TextView text,textTime;
        ConstraintLayout layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textTime = itemView.findViewById(R.id.textTime);
            layout = itemView.findViewById(R.id.layout);
        }

        public void onBind(NotificationData notificationData) {
            this.notificationData = notificationData;
            if(notificationData.getType().equals("postLike")){
                text.setText(notificationData.getNickname()+"님이 회원님의 게시글을 좋아합니다");
            }else if(notificationData.getType().equals("postComent")){
                text.setText(notificationData.getNickname()+"님이 회원님의 게시글의 댓글을 남겼습니다");
            }else if(notificationData.getType().equals("userLike")){
                text.setText(notificationData.getNickname()+"님이 회원님을 좋아합니다");
            }else if(notificationData.getType().equals("connect")){
                text.setText(notificationData.getNickname()+"님이 회원님의 좋아요를 수락했습니다");
            }else if(notificationData.getType().equals("disconnect")){
                text.setText(notificationData.getNickname()+"님이 회원님과 연결을 해제했습니다");
            }
            Picasso.get().load(notificationData.getProfileUrl()).into(profileImage);
            Picasso.get().load(notificationData.getImageUrl()).into(image);

            textTime.setText(getTime.timeString(notificationData.getTextTime()));

            profileImage.setOnClickListener(this);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.layout:
                    if(notificationData.getType().equals("postLike")||notificationData.getType().equals("postComent")){
                        Intent contentIntent = new Intent(context, PostActivity.class);
                        contentIntent.putExtra("num",notificationData.getItemNum());
                        context.startActivity(contentIntent);
                    }else if(notificationData.getType().equals("userLike")||notificationData.getType().equals("connect")||notificationData.getType().equals("disconnect")){
                        Intent userIntent = new Intent(context, UserProfileActivity.class);
                        userIntent.putExtra("num",notificationData.getItemNum());
                        context.startActivity(userIntent);
                    }
                    break;
                case R.id.profileImage:
                    Intent profileIntent = new Intent(context, UserProfileActivity.class);
                    profileIntent.putExtra("num",notificationData.getUserNum());
                    context.startActivity(profileIntent);
                    break;
            }
        }
    }
}
