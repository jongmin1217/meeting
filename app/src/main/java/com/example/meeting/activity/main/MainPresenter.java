package com.example.meeting.activity.main;

import android.util.Log;

import com.example.meeting.model.userData;
import com.example.meeting.retrofit.NetRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView view;
    private userData userdata;

    public MainPresenter(MainView view){

        this.view = view;
        userdata = new userData(view.getActivity());
    }

    void logout(){
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().removeToken(userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("firebaseLog",result);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
        userdata.removeUserData();
        view.logout();
    }

    void setToken(String token){
        Log.d("firebaseLog",token);
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().setToken(token,userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("firebaseLog",result);

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
