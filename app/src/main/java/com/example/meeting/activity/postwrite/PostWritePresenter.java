package com.example.meeting.activity.postwrite;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.meeting.custom.GetTime;
import com.example.meeting.model.userData;
import com.example.meeting.retrofit.NetRetrofit;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWritePresenter {
    private PostWriteView view;
    private GetTime getTime;
    private userData userdata;
    private Context context;

    public PostWritePresenter(PostWriteView view, Context context){
        this.context = context;
        this.view = view;
        getTime = new GetTime();
        userdata = new userData(context);
    }

    void postSave(String text, Uri imageUri){
        File imageFile = new File(Objects.requireNonNull(imageUri.getPath()));
        String imgFileName = System.currentTimeMillis()+"-"+userdata.getEmail()+"-";

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imgFileName+imageFile.getName(), requestFile);


        Call<Result> resultCall = NetRetrofit.getInstance().getService().postImageUpload(body);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("Retrofit", response.toString());
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("Retrofit", "asd");
            }
        });

        //selectedItems.add("http://13.209.4.115/profileimage/"+imgFileName+imageFile.getName());

        Call<ResponseBody> res = NetRetrofit.getInstance().getService().postWrite(userdata.getNum(),"http://13.209.4.115/postimage/"+imgFileName+imageFile.getName(),
                text,getTime.nowGetTime(),userdata.getGender());
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if(result.equals("succes")){
                        view.postWriteSucces();
                    }else{

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
}
