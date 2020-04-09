package com.example.meeting.activity.main.fragment;

import android.content.Context;
import android.util.Log;

import com.example.meeting.model.PostImageData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.PostImageAdapter;
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

public class PostPresenter {
    private PostView view;
    private PostImageAdapter adapter;
    private userData userdata;
    private PostImageData data;
    private ArrayList<PostImageData> dataList;
    private Context context;

    public PostPresenter(PostView view, PostImageAdapter adapter,Context context) {
        this.view = view;
        this.adapter = adapter;
        this.context = context;
        userdata = new userData(context);
        data = new PostImageData();
    }

    void getPost(){
        String gender = userdata.getGender();
        dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getPostList(gender);
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
                    view.endRefreshing();
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
