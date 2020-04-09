package com.example.meeting.activity.signup;


import android.util.Log;

import com.example.meeting.mailsender.GMailSender;
import com.example.meeting.retrofit.NetRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPresenter {

    private SignupView view;

    public SignupPresenter(SignupView view){
        this.view = view;
    }

    void signUpTry(final String email,final String password,final String passwordConfirm,final String phoneNumber){
        if(password.equals(passwordConfirm)){

            // gMailSender 객체화, 계정설정
            final GMailSender gMailSender = new GMailSender("syj408886@gmail.com", "fwts wask kynh bvhf");
            // 인증코드생성
            final String code = gMailSender.createEmailCode();

            Call<ResponseBody> res = NetRetrofit.getInstance().getService().signUpTry(email,password,phoneNumber,code);
            res.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        Log.d("signUpActivity","결과값  "+result);
                        if(result.equals("succes")){
                            gMailSender.sendMail("new love 인증코드입니다.", "인증코드 : "+code, email);
                            view.signUpSucces(email);
                        }else{
                            view.signUpFail("Already registered");
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
                    Log.d("loginActivity","실패"+t.getMessage());

                }
            });
        }else{
            view.signUpFail("Not equal password");
        }

    }
}
