package com.example.meeting.custom;

import android.util.Log;

import com.example.meeting.retrofit.NetRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sendNotification {
    public void insertNotification(String contentNum,String sendUser,String type,String contentImage,boolean config,String notiTime,String receiveNum){
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().insertNotification(contentNum,sendUser,type,contentImage,config,notiTime,receiveNum);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("insertNotification",result);
                } catch (IOException e) {
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
