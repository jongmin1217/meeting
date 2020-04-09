package com.example.meeting.activity.chatlist;

import android.content.Context;
import android.util.Log;

import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.JsonToString;
import com.example.meeting.custom.sendNotification;
import com.example.meeting.model.ChatListData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.ChatListAdapter;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.meeting.service.socketServer.dos;

public class ChatListPresenter {
    private ChatListView view;
    private ChatListAdapter adapter;
    private ChatListData data;
    private ArrayList<ChatListData> dataList;
    private Context context;
    private userData userdata;
    private JsonToString jsonToString;
    private sendNotification sendnotification;
    private GetTime getTime;

    public ChatListPresenter(ChatListView view, ChatListAdapter adapter, Context context) {
        this.view = view;
        this.adapter = adapter;
        this.context = context;
        userdata = new userData(context);
        jsonToString = new JsonToString();
        getTime = new GetTime();
        sendnotification = new sendNotification();
    }

    public void getChatList(String num) {
        dataList = new ArrayList<>();
        Log.d("chatlistlog", "전   " + dataList.size());
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getChatList(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("chatlistlog", result);
                    JSONObject myChatListObject = new JSONObject(result);
                    JSONArray jsonArray = myChatListObject.getJSONArray("chatList");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject chatListObject = jsonArray.getJSONObject(i);
                            data = new ChatListData();
                            data.setNum(chatListObject.getString("num"));
                            data.setUserNum(chatListObject.getString("userNum"));
                            data.setNickname(chatListObject.getString("nickname"));
                            data.setUrl(chatListObject.getString("profileImage"));
                            data.setLastMsg(chatListObject.getString("lastMsg"));
                            data.setLastMsgTime(chatListObject.getString("lastMsgTime"));
                            dataList.add(data);
                        }
                        Log.d("chatlistlog", "후     : " + dataList.size());
                        adapter.getItem(dataList);

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());

            }
        });
    }

    public void removeChatList(String chatNum, String userNum) {
        view.showProgress();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().removeChatList(chatNum, userdata.getNum(), userNum);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONArray json_array = new JSONArray(result);
                    if(json_array.length()!=0){
                        for (int i = 0; i < json_array.length(); i++) {

                            File file = new File("data/data/com.example.meeting/app_imageDir/" + json_array.getString(i));
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    }
                    view.hideProgress();
                    sendnotification.insertNotification(userdata.getNum(),userdata.getNum(),"disconnect",userdata.getTitleImage(),false,getTime.nowGetTime(),userNum);
                    dos.writeUTF(jsonToString.disconnect(userdata.getNum(), userdata.getNum(), userNum, userdata.getNickname(), userdata.getTitleImage(), result));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}
