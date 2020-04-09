package com.example.meeting.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.example.meeting.R;
import com.example.meeting.activity.chatroom.ChatRoomActivity;
import com.example.meeting.activity.post.PostActivity;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.model.userData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.core.app.NotificationCompat;

import static com.example.meeting.Fcm.FirebaseInstanceIDService.NOTIFICATION_CHANNEL_ID;

public class Notification {
    private Context context;
    private userData userdata;

    public Notification(Context context){
        this.context = context;
        userdata = new userData(context);
    }

    public void sendNotification(JSONObject object) throws JSONException {


        String type = object.getString("type");
        String num = object.getString("num");
        String sendNum = object.getString("sendNum");
        String receiveNum = object.getString("receiveNum");
        String nickname = object.getString("nickname");
        String imageUrl = object.getString("imageUrl");
        String message = object.getString("message");

        Log.d("socketServerLog","???");

        String title="";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent=null;

        if(type.equals("postLike")||type.equals("postComent")) {
            userdata.addNotification();
            if(type.equals("postLike")){
                title = nickname + "님이 회원님의 게시글을 좋아합니다";
            }else {
                title = nickname + "님이 회원님의 게시글에 댓글을 남겼습니다";
            }
            notificationIntent = new Intent(context, PostActivity.class);
            notificationIntent.putExtra("num", num); //전달할 값
            notificationIntent.putExtra("type", "fcm");
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }else if(type.equals("userLike")||type.equals("connect")||type.equals("disconnect")){
            userdata.addNotification();
            if(type.equals("userLike")){
                title = nickname + "님이 회원님을 좋아합니다";
            }else if(type.equals("connect")){
                title = nickname + "님이 회원님의 좋아요를 수락하였습니다";
            }else {
                title = nickname + "님이 회원님과 연결을 해제하였습니다";
                userdata.resetMsg(num);
                JSONArray json_array = new JSONArray(message);
                if(json_array.length()!=0){
                    for (int i = 0; i < json_array.length(); i++) {

                        File file = new File("data/data/com.example.meeting/app_imageDir/" + json_array.getString(i));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

            }
            notificationIntent = new Intent(context, UserProfileActivity.class);
            notificationIntent.putExtra("num", num); //전달할 값
            notificationIntent.putExtra("type", "fcm");
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }else if(type.equals("message")){
            userdata.addMsg(num);
            JSONArray json_array = new JSONArray(object.getString("message"));
            if(json_array.getString(0).equals("text")){
                title = nickname+" : "+json_array.getString(1);
            }else if(json_array.getString(0).equals("image")){
                title = nickname+"님이 사진을 보냈습니다";
            }else if(json_array.getString(0).equals("video")){
                title = nickname+"님이 동영상을 보냈습니다";
            }

            notificationIntent = new Intent(context, ChatRoomActivity.class);
            notificationIntent.putExtra("type", "fcm");
            notificationIntent.putExtra("num", num); //전달할 값
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = null;
        try {
            builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                    .setContentTitle(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(Picasso.get().load(imageUrl).get());
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.like);
            CharSequence channelName  = "채널";
            String description = "채널명";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.mipmap.like);

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build());

    }
}
