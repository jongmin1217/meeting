package com.example.meeting.Fcm;

import android.util.Log;

import com.example.meeting.retrofit.NetRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FcmSender {
    public void sendFcmNotification(String num,String sendNum,String receiveNum,String type,String nickname,String url,String message){
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().fcm(num,sendNum,receiveNum,type,nickname,url,message);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();

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
