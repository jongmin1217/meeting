package com.example.meeting.activity.main.fragment;

import android.util.Log;

import com.example.meeting.adapter.slider_adapter;
import com.example.meeting.model.PostImageData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.PostImageAdapter;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyinfoPresenter {
    private MyinfoView view;
    private PostImageAdapter adapter;
    private PostImageData data;
    private ArrayList<PostImageData> dataList;
    private userData userdata;

    public MyinfoPresenter(MyinfoView view, PostImageAdapter adapter) {
        this.view = view;
        this.adapter = adapter;
        data = new PostImageData();
        userdata = new userData(Objects.requireNonNull(view.getFragmentActivity()));
    }

    void getProfile() {
        slider_adapter slider_adapter = null;
        try {
            Log.d("qweqweqwe", userdata.getImage().get(0));
            slider_adapter = new slider_adapter(view.getFragmentActivity(), userdata.getImage());

            view.getProfile(slider_adapter, userdata.getNickname(), userdata.getArea(), userdata.getBirth(), userdata.getHeight(),
                    userdata.getForm(), userdata.getSmoking(), userdata.getDrinking(), userdata.getHobbyString(), userdata.getPersonalityString(),
                    userdata.getIdealTypeString());

            getMyPostList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    void getMyPostList() {
        dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().myPostList(userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject myPostDataObject = new JSONObject(result);
                    JSONArray jsonArray = myPostDataObject.getJSONArray("postData");
                    if(jsonArray.length()!=0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObject = jsonArray.getJSONObject(i);
                            data = new PostImageData();
                            data.setNum(postObject.getString("num"));
                            data.setUrl(postObject.getString("image"));
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
