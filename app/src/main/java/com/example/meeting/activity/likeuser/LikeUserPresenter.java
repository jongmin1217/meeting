package com.example.meeting.activity.likeuser;

import android.util.Log;

import com.example.meeting.model.LikeUserData;
import com.example.meeting.recyclerview.UserAdapter;
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

public class LikeUserPresenter {
    private LikeUserView view;
    private UserAdapter adapter;
    private LikeUserData data;
    private ArrayList<LikeUserData> dataList;

    public LikeUserPresenter(LikeUserView view, UserAdapter adapter){
        this.view = view;
        this.adapter = adapter;
        data = new LikeUserData();
    }

    void getUserList(String num){
        dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().likeUser(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject myPostDataObject = new JSONObject(result);
                    JSONArray jsonArray = myPostDataObject.getJSONArray("user");
                    Log.d("aassdd",result);
                    if(jsonArray.length()!=0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObject = jsonArray.getJSONObject(i);
                            data = new LikeUserData();
                            data.setNum(postObject.getString("num"));
                            data.setNickname(postObject.getString("nickname"));
                            data.setUrl(postObject.getString("image"));
                            Log.d("aassdd",data.getUrl()+"  "+data.getNickname()+"  "+data.getNum());
                            dataList.add(data);
                        }
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
}
