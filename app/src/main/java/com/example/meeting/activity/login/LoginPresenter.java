package com.example.meeting.activity.login;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.meeting.model.userData;
import com.example.meeting.retrofit.NetRetrofit;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {

    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    void loginTry(final String email, final String password, boolean autoLogin) {

        if (email.equals("") || password.equals("")) {
            view.loginTryFail("empty", email);
        } else {
            Call<ResponseBody> res = NetRetrofit.getInstance().getService().loginTry(email, password);
            res.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        Log.d("loginActivity", "결과값  " + result);
                        if (result.equals("no email") || result.equals("no password")) {
                            view.loginTryFail("no user", email);
                        } else if (result.equals("no certification")) {
                            view.loginTryFail("no certification", email);
                        } else if (result.equals("please profile info")) {
                            view.loginTryFail("please profile info", email);
                        } else if (result.equals("please image")) {
                            view.loginTryFail("please image", email);
                        } else {
                            if (autoLogin) {
                                SharedPreferences userData = view.getActivity().getSharedPreferences("userData", 0);
                                SharedPreferences.Editor editor = userData.edit();
                                editor.putString("autoLoginEmail", email);
                                editor.commit();
                                Toast.makeText(view.getActivity().getApplicationContext(), "자동로그인이 설정되었습니다", Toast.LENGTH_SHORT).show();
                            }



                            Call<ResponseBody> res = NetRetrofit.getInstance().getService().userInfo(email);
                            res.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String result = response.body().string();

                                        userData userdata = new userData(view.getActivity());


                                        JSONObject userDataObject = new JSONObject(result);

                                        userdata.setUserData(userDataObject.getString("num"),
                                                userDataObject.getString("email"),
                                                userDataObject.getString("nickname"),
                                                userDataObject.getString("image"),
                                                userDataObject.getString("height"),
                                                userDataObject.getString("form"),
                                                userDataObject.getString("hobby"),
                                                userDataObject.getString("ideaType"),
                                                userDataObject.getString("smoking"),
                                                userDataObject.getString("drinking"),
                                                userDataObject.getString("birth"),
                                                userDataObject.getString("area"),
                                                userDataObject.getString("personality"),
                                                userDataObject.getString("gender"));

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
                            view.loginTrySucces(email);
                        }
                    } catch (IOException e) {
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

    }
}
