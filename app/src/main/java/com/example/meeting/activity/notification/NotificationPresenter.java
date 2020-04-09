package com.example.meeting.activity.notification;

import android.content.Context;
import android.util.Log;

import com.example.meeting.model.NotificationData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.NotificationAdapter;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresenter {

    private NotificationView view;
    private Context context;
    private NotificationAdapter noConfigAdapter,configAdapter;
    private userData userdata;

    public NotificationPresenter(NotificationView view, Context context,NotificationAdapter noConfigAdapter,NotificationAdapter configAdapter){
        this.view = view;
        this.context = context;
        this.noConfigAdapter = noConfigAdapter;
        this.configAdapter = configAdapter;
        userdata = new userData(context);
    }

    void getNotification(){
        ArrayList<NotificationData> noConfigData,configData;
        noConfigData = new ArrayList<>();
        configData = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().selectNotification(userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject notificationObject = new JSONObject(result);
                    JSONArray jsonArray = notificationObject.getJSONArray("notificationData");
                    if(jsonArray.length()!=0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject notiObject = jsonArray.getJSONObject(i);
                            NotificationData data = new NotificationData();

                            if(notiObject.getString("config").equals("0")){
                                data.setItemNum(notiObject.getString("contentNum"));
                                data.setUserNum(notiObject.getString("sendUser"));
                                data.setType(notiObject.getString("type"));
                                data.setImageUrl(notiObject.getString("contentImage"));
                                data.setNickname(notiObject.getString("nickname"));
                                data.setProfileUrl(notiObject.getString("userImage"));
                                data.setTextTime(notiObject.getString("notiTime"));
                                noConfigData.add(data);
                            }else{
                                data.setItemNum(notiObject.getString("contentNum"));
                                data.setUserNum(notiObject.getString("sendUser"));
                                data.setType(notiObject.getString("type"));
                                data.setImageUrl(notiObject.getString("contentImage"));
                                data.setNickname(notiObject.getString("nickname"));
                                data.setProfileUrl(notiObject.getString("userImage"));
                                data.setTextTime(notiObject.getString("notiTime"));
                                configData.add(data);
                            }

                        }
                        if(noConfigData.size()==0){
                            view.noNoConfig();
                        }else if(configData.size()==0){
                            view.noConfig();
                        }
                        noConfigAdapter.getItem(noConfigData);
                        configAdapter.getItem(configData);

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
}
