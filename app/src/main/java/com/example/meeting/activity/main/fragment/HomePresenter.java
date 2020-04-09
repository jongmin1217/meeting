package com.example.meeting.activity.main.fragment;

import android.content.Context;
import android.util.Log;

import com.example.meeting.model.recommendationData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.recommendationAdapter;
import com.example.meeting.retrofit.NetRetrofit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private HomeView view;
    private Context context;
    private userData userdata;
    private recommendationAdapter adapter;

    public HomePresenter(HomeView view, Context context, recommendationAdapter adapter) {
        this.view = view;
        this.context = context;
        this.adapter = adapter;
        userdata = new userData(context);
    }

    String getUserNum(){
        return userdata.getNum();
    }

    void getRecommendation() {
        String num = userdata.getNum();
        ArrayList<recommendationData> dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getRecommendation(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("recommendationResult",result);
                    JSONObject userDataObject = new JSONObject(result);
                    JSONArray jsonArray = userDataObject.getJSONArray("userInfo");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject postObject = jsonArray.getJSONObject(i);
                            recommendationData data = new recommendationData();

                            data.setNum(postObject.getString("num"));
                            data.setNickname(postObject.getString("nickname"));
                            data.setUrl(postObject.getString("image"));
                            Calendar calendar = new GregorianCalendar(Locale.KOREA);
                            int nYear = calendar.get(Calendar.YEAR);
                            int birth = Integer.parseInt(postObject.getString("birth").substring(0, 4));
                            String age = Integer.toString((nYear + 1) - birth);
                            data.setAge(age+"세");
                            data.setArea(postObject.getString("area"));

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