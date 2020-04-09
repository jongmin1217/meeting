package com.example.meeting.activity.post;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.JsonToString;
import com.example.meeting.custom.sendNotification;
import com.example.meeting.model.ComentData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.ComentAdapter;
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

import static com.example.meeting.service.socketServer.dos;

public class PostPresenter {
    private PostView view;
    private Context context;
    private Activity activity;
    private userData userdata;
    private GetTime getTime;
    private ComentAdapter adapter;
    private ComentData data;
    private JsonToString str;
    private sendNotification sendnotification;

    public PostPresenter(PostView view, Context context, ComentAdapter adapter) {
        this.view = view;
        this.context = context;
        this.adapter = adapter;
        activity = (Activity) context;
        userdata = new userData(context);
        getTime = new GetTime();
        str = new JsonToString();
        sendnotification = new sendNotification();
    }

    void getPostInfo(String num) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getPostInfo(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    boolean myPost = false;
                    JSONObject postInfoObject = new JSONObject(result);
                    if (postInfoObject.getString("writerNum").equals(userdata.getNum())) {
                        myPost = true;
                    }
                    view.getPostInfo(postInfoObject.getString("writerNum"), postInfoObject.getString("profileImage"),
                            postInfoObject.getString("nickname"), postInfoObject.getString("image"), postInfoObject.getString("postText"),
                            getTime.timeString(postInfoObject.getString("postTime")), postInfoObject.getString("postLike"), myPost);
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

    void postDelete(String num) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().postDelete(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();

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

    void likeConf(String num) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().likeConf(userdata.getNum(), num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if (result.equals("true")) {
                        view.likeTrue();
                    } else {
                        view.likeFalse();
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

    void postLike(String num, boolean like, String writerNum, String image) {
        if (like) {
            view.likeFalse();
        } else {
            if (!writerNum.equals(userdata.getNum())) {
                try {
                    sendnotification.insertNotification(num,userdata.getNum(),"postLike",image,false,getTime.nowGetTime(),writerNum);
                    dos.writeUTF(str.postLike(num, userdata.getNum(), writerNum, userdata.getNickname(), image, "like"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            view.likeTrue();
        }
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().postLike(num, userdata.getNum(), like);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();

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


    void comentInsert(String num, String coment, String writerNum, String image) {
        if (!writerNum.equals(userdata.getNum())) {
            try {
                sendnotification.insertNotification(num,userdata.getNum(),"postComent",image,false,getTime.nowGetTime(),writerNum);
                dos.writeUTF(str.postComent(num, userdata.getNum(), writerNum, userdata.getNickname(), image, "coment"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String time = getTime.nowGetTime();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().postComentInsert(num, userdata.getNum(), coment, time);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    view.invisible();
                    ComentData data = new ComentData();
                    data.setNum(result);
                    data.setUserNum(userdata.getNum());
                    data.setNickname(userdata.getNickname());
                    try {
                        data.setUrl(userdata.getTitleImage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    data.setComent(coment);
                    data.setComentTime(time);
                    adapter.addItem(data);
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

    void getPostComent(String num) {
        ArrayList<ComentData> dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getPostComent(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();

                    JSONObject ComentDataObject = new JSONObject(result);
                    JSONArray jsonArray = ComentDataObject.getJSONArray("postComent");
                    if (jsonArray.length() == 0) {
                        view.noComent();
                        view.endRefreshing(false);
                        adapter.removeAllItem();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ComentObject = jsonArray.getJSONObject(i);
                            data = new ComentData();
                            data.setNum(ComentObject.getString("num"));
                            data.setUserNum(ComentObject.getString("writerNum"));
                            data.setNickname(ComentObject.getString("nickname"));
                            data.setUrl(ComentObject.getString("profileImage"));
                            data.setComent(ComentObject.getString("coment"));
                            data.setComentTime(ComentObject.getString("comentTime"));
                            dataList.add(data);
                        }
                        adapter.getItem(dataList);
                        view.endRefreshing(true);
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

    public void removeComent(String num) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().removeComent(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();

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

    String changeLikeNum(String num, boolean like) {
        int intNum = Integer.parseInt(num);
        if (like) {
            intNum--;
        } else {
            intNum++;
        }

        return Integer.toString(intNum);
    }
}
