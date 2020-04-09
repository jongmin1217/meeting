package com.example.meeting.Fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.example.meeting.R;
import com.example.meeting.activity.main.MainActivity;
import com.example.meeting.model.userData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("fcmNotificationLog","onMessageReceived  "+remoteMessage.getData().get("num")+"  "+remoteMessage.getData().get("title")+"  "+remoteMessage.getData().get("message"));
        if (remoteMessage.getData().size() > 0) {
            Log.d("fcmNotificationLog","dkdkdkdkdkdkdkdkdkdkdkdk");
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        userData userdata = new userData(this);

        String title = remoteMessage.getData().get("title");
        String type = remoteMessage.getData().get("type");
        String num = remoteMessage.getData().get("num");
        String imageUrl = remoteMessage.getData().get("imageUrl");
        String message = remoteMessage.getData().get("message");


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent=null;
        if(type.equals("applyFace")){
            String sendNum = remoteMessage.getData().get("sendNum");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("type","applyFace");
            intent.putExtra("roomNum",num);
            intent.putExtra("nickname",title);
            intent.putExtra("profile",imageUrl);
            intent.putExtra("userNum",sendNum);
            startActivity(intent);
        }else{
            if(type.equals("postLike")||type.equals("postComent")){
                userdata.addNotification();
                notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("type", "post"); //전달할 값
                notificationIntent.putExtra("num", num); //전달할 값
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK) ;
            }else if(type.equals("recommendation")){
                notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("type", "recommendation"); //전달할 값
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK) ;
            }else if(type.equals("userLike")||type.equals("connect")||type.equals("disconnect")){
                userdata.addNotification();
                if(type.equals("disconnect")){
                    userdata.resetMsg(num);
                    JSONArray json_array = null;
                    try {
                        json_array = new JSONArray(message);
                        if(json_array.length()!=0){
                            for (int i = 0; i < json_array.length(); i++) {

                                File file = new File("data/data/com.example.meeting/app_imageDir/" + json_array.getString(i));
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("type", "userLike"); //전달할 값
                notificationIntent.putExtra("num", num); //전달할 값
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK) ;
            }else if(type.equals("message")){
                userdata.addMsg(num);
                notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("type", "message"); //전달할 값
                notificationIntent.putExtra("num", num); //전달할 값
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK) ;
            }


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = null;
            try {
                builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
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


}
