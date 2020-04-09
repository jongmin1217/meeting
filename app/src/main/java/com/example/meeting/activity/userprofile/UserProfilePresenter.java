package com.example.meeting.activity.userprofile;

import android.content.Context;
import android.util.Log;

import com.example.meeting.adapter.slider_adapter;
import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.JsonToString;
import com.example.meeting.custom.sendNotification;
import com.example.meeting.model.PostImageData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.PostImageAdapter;
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

import static com.example.meeting.service.socketServer.dos;

public class UserProfilePresenter {
    private UserProfileView view;
    private PostImageAdapter adapter;
    private Context context;
    private PostImageData data;
    private ArrayList<PostImageData> dataList;
    private userData userdata;
    private String statusNum;
    private sendNotification sendnotification;
    private JsonToString str;
    private GetTime getTime;

    public UserProfilePresenter(UserProfileView view, PostImageAdapter adapter, Context context){
        this.view = view;
        this.adapter = adapter;
        this.context = context;
        data = new PostImageData();
        userdata = new userData(context);
        sendnotification = new sendNotification();
        str = new JsonToString();
        getTime = new GetTime();
    }

    void getProfile(String num) {

        Call<ResponseBody> res = NetRetrofit.getInstance().getService().userProfile(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject userDataObject = new JSONObject(result);


                    Calendar calendar = new GregorianCalendar(Locale.KOREA);
                    int nYear = calendar.get(Calendar.YEAR);
                    int birth = Integer.parseInt(userDataObject.getString("birth").substring(0, 4));
                    String age = Integer.toString((nYear+1)-birth);

                    slider_adapter slider_adapter = null;
                    slider_adapter = new slider_adapter(context, StringToList(userDataObject.getString("image")));

                    boolean sameGender;
                    if(userDataObject.getString("gender").equals(userdata.getGender())){
                        sameGender = true;
                    }else{
                        sameGender = false;
                    }

                    view.getProfile(slider_adapter, userDataObject.getString("nickname"), userDataObject.getString("area"), age, userDataObject.getString("height"),
                            userDataObject.getString("form"), userDataObject.getString("smoking"), userDataObject.getString("drinking"), JsonToString(userDataObject.getString("hobby")),
                            JsonToString(userDataObject.getString("personality")), JsonToString(userDataObject.getString("ideaType")),sameGender);

                    getPostList(num);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());
                Log.d("loginActivity", "실패" + t.getMessage());

            }
        });
    }

    void editUserStatus(String num, String type, String likeNum){

        Call<ResponseBody> res = NetRetrofit.getInstance().getService().editUserStatus(num, userdata.getNum(), type,likeNum,getTime.nowGetTime());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if(type.equals("insert")){
                        view.userStatus("send",statusNum);

                        sendnotification.insertNotification(userdata.getNum(),userdata.getNum(),"userLike","http://13.209.4.115/postimage/likeAnimation.png",false,getTime.nowGetTime(),num);
                        dos.writeUTF(str.sendLike(userdata.getNum(), userdata.getNum(), num, userdata.getNickname(), "http://13.209.4.115/postimage/likeAnimation.png", "userLike"));

                    }else if(type.equals("update")){
                        view.userStatus("connect",statusNum);

                        sendnotification.insertNotification(userdata.getNum(),userdata.getNum(),"connect","http://13.209.4.115/postimage/likeAnimation.png",false,getTime.nowGetTime(),num);
                        dos.writeUTF(str.connect(userdata.getNum(), userdata.getNum(), num, userdata.getNickname(), "http://13.209.4.115/postimage/likeAnimation.png", "connect"));

                    }
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

    public void getUserStatus(String num){
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getUserStatus(userdata.getNum(),num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if(result.equals("no")){
                        view.userStatus("no",null);
                    }else{
                        JSONObject userDataObject = new JSONObject(result);
                        statusNum = userDataObject.getString("num");
                        if(userDataObject.getString("status").equals("1")){
                            if(userDataObject.getString("type").equals("send")){
                                view.userStatus("send",statusNum);
                            }else if(userDataObject.getString("type").equals("receive")){
                                view.userStatus("receive",statusNum);
                            }
                        }else if(userDataObject.getString("status").equals("2")){
                            view.userStatus("connect",statusNum);
                        }

                    }

                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());
                Log.d("loginActivity", "실패" + t.getMessage());

            }
        });
    }

    private void getPostList(String num) {
        dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().myPostList(num);
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

    public void chatClick(String num){
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().chatClick(num,userdata.getNum());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if(!result.equals("")){
                        view.chatStart(result);
                    }
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

    public void applyFace(String num,String userNum){
        try {
            dos.writeUTF(JsonToString.applyFace(num,userdata.getNum(),userNum,userdata.getNickname(),userdata.getTitleImage(),"applyFace"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public String JsonToString(String text) throws JSONException {
        JSONArray json_array = new JSONArray(text);
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }

        ArrayList<String> array = listdata;
        StringBuilder idealType= new StringBuilder();
        for(int i=0; i<array.size(); i++){
            if(i==0){
                idealType.append(array.get(i));
            }else{
                idealType.append(", ").append(array.get(i));
            }
        }
        return idealType.toString();
    }

    public ArrayList<String> StringToList(String text) throws JSONException{
        JSONArray json_array = new JSONArray(text);
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }
        return listdata;
    }
}
