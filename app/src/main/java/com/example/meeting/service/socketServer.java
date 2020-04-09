package com.example.meeting.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.meeting.activity.applyFace.ApplyFaceActivity;
import com.example.meeting.custom.JsonToString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatRoomNum;
import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatUserNum;
import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatWaitNum;
import static com.example.meeting.activity.post.PostActivity.postNumber;
import static com.example.meeting.activity.userprofile.UserProfileActivity.userProfileNum;

public class socketServer extends Service {
    private String html;
    private DataInputStream dis;
    public static DataOutputStream dos;
    private Socket socket;

    private String userNum;
    private String ip = "13.209.4.115";
    private int port = 5001;

    private boolean isStop;
    private JsonToString str;
    private Notification notification;

    public socketServer() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new Notification(this);
        str = new JsonToString();
        Log.d("socketServerLog", "service start");
        new Thread(() -> {
            try {
                setSocket(ip, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!isStop) {
                Log.d("socketServerLog", "while");
                try {
                    html = dis.readUTF();
                    JSONObject object = new JSONObject(html);
                    if (object.getString("type").equals("postComent")) {
                        if (getActivityName().equals("com.example.meeting.activity.post.PostActivity") && postNumber.equals(object.getString("num"))) {
                            sendMsgPost();
                        } else if (getActivityName().equals("com.example.meeting.activity.main.MainActivity")) {
                            notification.sendNotification(object);
                            sendNotification("notice");
                        } else {
                            notification.sendNotification(object);
                        }
                    } else if (object.getString("type").equals("postLike")) {
                        notification.sendNotification(object);
                        if (getActivityName().equals("com.example.meeting.activity.main.MainActivity")) {
                            sendNotification("notice");
                        }
                    } else if (object.getString("type").equals("userLike") || object.getString("type").equals("connect") || object.getString("type").equals("disconnect")) {

                        if(object.getString("num").equals(chatUserNum)&&object.getString("type").equals("disconnect")){
                            sendMessage("disconnect","","");

                            JSONArray json_array = new JSONArray(object.getString("message"));
                            if(json_array.length()!=0){
                                for (int i = 0; i < json_array.length(); i++) {

                                    File file = new File("data/data/com.example.meeting/app_imageDir/" + json_array.getString(i));
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                }
                            }
                        }else{
                            notification.sendNotification(object);
                            if (getActivityName().equals("com.example.meeting.activity.main.MainActivity")) {
                                if (object.getString("type").equals("disconnect")) {
                                    sendNotification("message");
                                }
                                sendNotification("notice");
                            }else if(getActivityName().equals("com.example.meeting.activity.userprofile.UserProfileActivity")&&object.getString("num").equals(userProfileNum)){
                                sendUserStatus();
                            }else if(getActivityName().equals("com.example.meeting.activity.chatlist.ChatListActivity")&&object.getString("type").equals("disconnect")){
                                sendChatList();
                            }
                        }


                    } else if (object.getString("type").equals("message")) {
                        if (chatRoomNum.equals(object.getString("num"))) {
                            JSONArray json_array = new JSONArray(object.getString("message"));
                            sendMessage(json_array.getString(0), json_array.getString(1), json_array.getString(2));
                        } else {
                            if(chatWaitNum.equals(object.getString("num"))){
                                JSONArray json_array = new JSONArray(object.getString("message"));
                                sendMessage(json_array.getString(0), json_array.getString(1), json_array.getString(2));
                                notification.sendNotification(object);
                            }else{
                                if (getActivityName().equals("com.example.meeting.activity.main.MainActivity")) {
                                    notification.sendNotification(object);
                                    sendNotification("message");
                                } else if (getActivityName().equals("com.example.meeting.activity.chatlist.ChatListActivity")) {
                                    notification.sendNotification(object);
                                    sendChatList();
                                } else {
                                    notification.sendNotification(object);
                                }
                            }

                        }

                    } else if (object.getString("type").equals("chatRead")) {
                        if (chatRoomNum.equals(object.getString("num"))) {
                            sendChatRead();
                        }
                    } else if(object.getString("type").equals("applyFace")){
                        Intent intent = new Intent(this, ApplyFaceActivity.class);
                        intent.putExtra("roomNum",object.getString("num"));
                        intent.putExtra("nickname",object.getString("nickname"));
                        intent.putExtra("profile",object.getString("imageUrl"));
                        intent.putExtra("userNum",object.getString("sendNum"));
                        startActivity(intent);
                    } else if(object.getString("type").equals("refuseFace")){
                        if(getActivityName().equals("com.example.meeting.activity.facechat.CallActivity")){
                            refuseFace();
                        }

                    }else if(object.getString("type").equals("cancelFace")){
                        if(getActivityName().equals("com.example.meeting.activity.applyFace.ApplyFaceActivity")){
                            cancelFace();
                        }
                    }else if(object.getString("type").equals("facetrans")){
                        faceTrans();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d("socketServerLog", "while end");

        }).start();
    }

    String getActivityName() {
        ActivityManager manager = (ActivityManager) getBaseContext().getSystemService(Activity.ACTIVITY_SERVICE);

        assert manager != null;
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo info = list.get(0);

        return info.topActivity.getClassName();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("socketServerLog", "onStartCommand");
        userNum = intent.getStringExtra("num");
        return super.onStartCommand(intent, flags, startId);
    }

    public void setSocket(String ip, int port) throws IOException {

        try {
            socket = new Socket(ip, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF(str.start(userNum));
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("socketServerLog", "destroy");
        try {
            dos.writeUTF(str.end(userNum));
            isStop = true;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sendMsgPost() {
        Intent intent = new Intent("post");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(String type) {
        Intent intent = new Intent("notification");
        intent.putExtra("type", type);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendChatList() {
        Intent intent = new Intent("chatList");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendUserStatus() {
        Intent intent = new Intent("userStatus");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendMessage(String type, String msg, String msgTime) {
        Log.d("checkLog","sendmessage");
        Intent intent = new Intent("message");
        intent.putExtra("type", type);
        intent.putExtra("msg", msg);
        intent.putExtra("msgTime", msgTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendChatRead() {
        Intent intent = new Intent("message");
        intent.putExtra("type", "chatRead");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void refuseFace() {
        Intent intent = new Intent("refuseFace");
        intent.putExtra("type","refuseFace");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void cancelFace() {
        Intent intent = new Intent("cancelFace");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void faceTrans(){
        Intent intent = new Intent("refuseFace");
        intent.putExtra("type","faceTrans");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
