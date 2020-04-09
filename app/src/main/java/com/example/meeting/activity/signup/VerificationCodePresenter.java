package com.example.meeting.activity.signup;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.meeting.retrofit.NetRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodePresenter {
    private VerificationCodeView view;

    public VerificationCodePresenter(VerificationCodeView view){
        this.view = view;
    }

    @SuppressLint("LongLogTag")
    void certificationCodeTry(final String email,final String certificationCode){

        Call<ResponseBody> res = NetRetrofit.getInstance().getService().certificationCodeTry(email,certificationCode);
        res.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("VerificationCodeActivity","결과값  "+result);
                    if(result.equals("succes")){
                        view.cerificationCodeSucces(result);
                    }else{
                        view.cerificationCodeFail(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Err", t.getMessage());
                Log.d("VerificationCodeActivity","실패"+t.getMessage());

            }
        });
    }


}
