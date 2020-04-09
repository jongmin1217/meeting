package com.example.meeting.activity.chatroom;

import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.JsonToString;
import com.example.meeting.custom.Util;
import com.example.meeting.model.ChatRoomData;
import com.example.meeting.model.userData;
import com.example.meeting.recyclerview.ChatRoomAdapter;
import com.example.meeting.retrofit.NetRetrofit;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.meeting.activity.chatroom.ChatRoomActivity.chatRoomNum;
import static com.example.meeting.custom.Util.getName;
import static com.example.meeting.custom.Util.getRealPathFromURI;
import static com.example.meeting.custom.Util.saveBitmapAsFile;
import static com.example.meeting.service.socketServer.dos;

public class ChatRoomPresenter {

    public static ArrayList<String> imageResult = new ArrayList<>();
    private ChatRoomView view;
    private ChatRoomAdapter adapter;
    private Context context;
    private userData userdata;
    private ArrayList<ChatRoomData> dataList;
    private ChatRoomData data;
    private GetTime getTime;
    private String day;
    private String userNum;
    private JsonToString jsonToString;
    private final int pick_from_Multi_album = 0;
    private final int pickVideo = 1;
    private File tempFile;
    private int cnt;

    public ChatRoomPresenter(ChatRoomView view, ChatRoomAdapter adapter, Context context, String userNum) {
        this.view = view;
        this.adapter = adapter;
        this.context = context;
        this.userNum = userNum;
        userdata = new userData(context);
        getTime = new GetTime();
        jsonToString = new JsonToString();
    }

    public void chatRead(String num, String userNum) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().chatRead(num, userNum);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    dos.writeUTF(jsonToString.sendChatRead(num, userNum));
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


    public void getMessage(String num) {
        dataList = new ArrayList<>();
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().getMessage(num);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("chatroomlog", result);
                    JSONObject myChatroomObject = new JSONObject(result);
                    JSONArray jsonArray = myChatroomObject.getJSONArray("message");
                    if (jsonArray.length() != 0) {
                        imageResult = new ArrayList<>();
                        day = "";
                        int timeInsert = 0;
                        for (int i = 0; i < jsonArray.length() + timeInsert; i++) {
                            JSONObject chatRoomObject = jsonArray.getJSONObject(i - timeInsert);


                            if (chatRoomObject.getString("msgType").equals("notice")) {
                                data = new ChatRoomData();
                                data.setItemViewType(7);
                            } else {
                                if (!chatRoomObject.getString("msgTime").substring(0, 14).equals(day)) {
                                    day = chatRoomObject.getString("msgTime").substring(0, 14);
                                    data = new ChatRoomData();
                                    data.setItemViewType(0);
                                    data.setMsg(getTime.chatTime(chatRoomObject.getString("msgTime").substring(0, 14)));
                                    dataList.add(data);
                                    i++;
                                    timeInsert++;
                                }
                                data = new ChatRoomData();
                                if (chatRoomObject.getString("msgType").equals("text")) {
                                    if (chatRoomObject.getString("writerNum").equals(userdata.getNum())) {
                                        data.setItemViewType(1);
                                        if (chatRoomObject.getString("isRead").equals("0")) {
                                            data.setRead(false);
                                        } else {
                                            data.setRead(true);
                                        }
                                        if (i != 0) {
                                            if (dataList.get(i - 1).getItemViewType() == 1 && getTime.chatRoomTime(chatRoomObject.getString("msgTime")).equals(dataList.get(i - 1).getMsgTime())) {
                                                dataList.get(i - 1).setTimeType(false);
                                                data.setTimeType(true);
                                            } else {
                                                data.setTimeType(true);
                                            }
                                        } else {
                                            data.setTimeType(true);
                                        }
                                    } else {
                                        data.setItemViewType(2);
                                        if (i != 0) {
                                            if (dataList.get(i - 1).getItemViewType() == 2 && getTime.chatRoomTime(chatRoomObject.getString("msgTime")).equals(dataList.get(i - 1).getMsgTime())) {
                                                dataList.get(i - 1).setTimeType(false);
                                                data.setTimeType(true);
                                            } else {
                                                data.setTimeType(true);
                                            }
                                        } else {
                                            data.setTimeType(true);
                                        }

                                    }
                                } else if (chatRoomObject.getString("msgType").equals("image")) {

                                    JSONArray array = new JSONArray(chatRoomObject.getString("msg"));
                                    ArrayList<String> imageList = new ArrayList<>();
                                    for (int j = 0; j < array.length(); j++) {
                                        imageResult.add(array.getString(j));
                                        imageList.add(array.getString(j));
                                    }
                                    data.setImageUrlList(imageList);
                                    data.setImageNum(array.length());
                                    data.setImageType("url");
                                    if (chatRoomObject.getString("writerNum").equals(userdata.getNum())) {
                                        if (array.length() == 1) {
                                            data.setItemViewType(3);
                                        } else {
                                            data.setItemViewType(5);
                                        }
                                        if (chatRoomObject.getString("isRead").equals("0")) {
                                            data.setRead(false);
                                        } else {
                                            data.setRead(true);
                                        }
                                    } else {
                                        if (array.length() == 1) {
                                            data.setItemViewType(4);
                                        } else {
                                            data.setItemViewType(6);
                                        }
                                    }
                                } else if (chatRoomObject.getString("msgType").equals("video")) {

                                    if (chatRoomObject.getString("writerNum").equals(userdata.getNum())) {
                                        data.setItemViewType(8);
                                        if (chatRoomObject.getString("isRead").equals("0")) {
                                            data.setRead(false);
                                        } else {
                                            data.setRead(true);
                                        }
                                    } else {

                                        data.setItemViewType(9);

                                    }
                                }
                                data.setImage(chatRoomObject.getString("msg"));
                                data.setMsgTime(getTime.chatRoomTime(chatRoomObject.getString("msgTime")));
                                data.setUserNum(chatRoomObject.getString("writerNum"));

                            }
                            data.setMsg(chatRoomObject.getString("msg"));
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

    public void receiveMessage(String type, String msg, String msgTime) {
        dayCheck(msgTime.substring(0, 14));
        if (type.equals("text")) {
            data = new ChatRoomData();
            data.setItemViewType(2);
            data.setMsg(msg);
            data.setTimeType(true);
            data.setMsgTime(getTime.chatRoomTime(msgTime));
            adapter.insertItem(data);
        } else if (type.equals("image")) {

            data = new ChatRoomData();
            try {
                JSONArray object = new JSONArray(msg);
                data.setImageNum(object.length());
                if (object.length() == 1) {

                    data.setItemViewType(4);
                } else {

                    data.setItemViewType(6);
                }
                ArrayList<String> imageList = new ArrayList<>();
                for (int i = 0; i < object.length(); i++) {
                    imageList.add(object.getString(i));
                    imageResult.add(object.getString(i));
                    Log.d("qweqweqwe", object.length() + "");
                    Log.d("qweqweqwe", object.getString(i));
                }
                data.setImageUrlList(imageList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            data.setMsgTime(getTime.chatRoomTime(msgTime));
            data.setImageType("url");
            adapter.insertItem(data);
        } else {
            data = new ChatRoomData();
            data.setImage(msg);
            data.setMsgTime(getTime.chatRoomTime(msgTime));
            data.setItemViewType(9);
            adapter.insertItem(data);
        }

    }

    public void sendMessage(String num, String msg) {

        String msgTime = getTime.nowGetTime();

        serverSend(num, msg, msgTime, "text");

        dayCheck(msgTime.substring(0, 14));
        data = new ChatRoomData();
        data.setItemViewType(1);
        data.setMsg(msg);
        data.setMsgTime(getTime.chatRoomTime(msgTime));
        data.setRead(false);
        data.setTimeType(true);
        adapter.insertItem(data);
    }

    private void serverSend(String num, String msg, String msgTime, String type) {
        Call<ResponseBody> res = NetRetrofit.getInstance().getService().insertMessage(num, userdata.getNum(), msg, msgTime, type);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    ArrayList<String> msgResult = new ArrayList<>();

                    msgResult.add(type);
                    msgResult.add(msg);
                    msgResult.add(msgTime);
                    Gson gson = new Gson();
                    String jsonPlace = gson.toJson(msgResult);
                    dos.writeUTF(jsonToString.sendMessage(num, userdata.getNum(), userNum, userdata.getNickname(), "http://13.209.4.115/postimage/sms.png", jsonPlace));

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

    public void selectImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case pick_from_Multi_album:
                    if (data != null) {
                        if (data.getClipData() == null) {
                            Toast.makeText(context, "다중선택이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() > 9) {
                                Toast.makeText(context, "사진은 9장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                ArrayList<Uri> uriList = new ArrayList<>();
                                for (int i = 0; i < clipData.getItemCount(); i++) {

                                    Log.d("imageUploadTime", "start clipData");
                                    Uri uri = clipData.getItemAt(i).getUri();

                                    Cursor cursor = null;

                                    try {

                                        Log.d("imageUploadTime", "try clipData");
                                        String[] proj = {MediaStore.Images.Media.DATA};

                                        assert uri != null;
                                        cursor = context.getContentResolver().query(uri, proj, null, null, null);

                                        assert cursor != null;
                                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                                        cursor.moveToFirst();

                                        tempFile = new File(cursor.getString(column_index));
                                        Log.d("imageUploadTime", "end clipData");
                                    } finally {
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    }


                                    uriList.add(Uri.fromFile(tempFile));
                                    Log.d("imageUploadTime", "end listadd");
                                }
                                String msgTime = getTime.nowGetTime();

                                imageUpload(uriList, msgTime);

                            }
                        }
                    }
                    break;
                case pickVideo:
                    Uri uri = data.getData();
                    String path = getRealPathFromURI(context, uri);
                    String name = getName(context, uri);
                    assert uri != null;
                    File tempFile = new File(path);
                    String imgFileName = System.currentTimeMillis() + "- test -";
                    String fileNameResult = imgFileName + name;
                    videoUpload(tempFile, fileNameResult, "video");
                    Bitmap b = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
                    String imagePath = "/storage/emulated/0/DCIM/Camera/" + name + ".jpg";
                    saveBitmapAsFile(b, imagePath);
                    File imageFile = new File(imagePath);
                    String imageFileName = imgFileName + name + ".jpg";
                    videoUpload(imageFile, imageFileName, "image");
                    break;
            }
        }
    }

    public void imageUpload(ArrayList<Uri> uriList, String msgTime) {
        view.showProgress();
        ArrayList<String> imageListName = new ArrayList<>();
        cnt = 0;
        for (int i = 0; i < uriList.size(); i++) {
            Log.d("imageUploadTime", "start imageUpload");

            final File imageFile = new File(uriList.get(i).getPath());
            String imgFileName = System.currentTimeMillis() + "-" + userdata.getEmail() + "-";
            String fileNameResult = imgFileName + imageFile.getName();

            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());

            Log.d("imageUploadTime", "create bitmap");
            ExifInterface exif = null;


            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory, fileNameResult);
            if (!file.exists()) {
                FileOutputStream fos = null;
                try {
                    exif = new ExifInterface(imageFile.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = Util.rotateBitmap(bitmap, orientation);
                    fos = new FileOutputStream(file);
                    assert bmRotated != null;
                    bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();


                    Log.d("imageUploadTime", "FileOutputStream");
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", fileNameResult, requestFile);

            Log.d("parkj", imageFile.getName());
            Log.d("parkj", String.valueOf(requestFile));


            Log.d("imageUploadTime", "start serverUpload");
            Call<Result> resultCall = NetRetrofit.getInstance().getService().chatImageUpload(body);

            resultCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.d("Retrofit", response.toString());

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.d("Retrofit", call.toString() + "   " + t.toString());
                    imageListName.add("http://13.209.4.115/chatimage/" + fileNameResult);
                    imageResult.add("http://13.209.4.115/chatimage/" + fileNameResult);
                    cnt++;
                    Log.d("imageUploadTime", "end serverUpload");
                    if (uriList.size() == cnt) {
                        dayCheck(msgTime.substring(0, 14));

                        ChatRoomData imageData = new ChatRoomData();
                        if (imageListName.size() == 1) {
                            imageData.setItemViewType(3);
                            imageData.setImageUrlList(imageListName);
                        } else {
                            imageData.setImageNum(imageListName.size());
                            imageData.setItemViewType(5);
                            imageData.setImageUrlList(imageListName);
                        }
                        imageData.setImageType("url");
                        imageData.setMsgTime(getTime.chatRoomTime(msgTime));
                        imageData.setRead(false);
                        adapter.insertItem(imageData);

                        Gson gson = new Gson();
                        String jsonPlace = gson.toJson(imageListName);
                        serverSend(chatRoomNum, jsonPlace, msgTime, "image");
                        view.hideProgress();
                        Log.d("imageUploadTime", "last end");
                    }
                }
            });

        }
    }

    private void dayCheck(String insertDay) {
        if (!insertDay.equals(day)) {

            day = insertDay;
            ChatRoomData data = new ChatRoomData();
            data.setItemViewType(0);
            data.setMsg(getTime.chatTime(insertDay));
            adapter.insertItem(data);
        }
    }

    private void videoUpload(File file, String fileName, String type) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", fileName, requestFile);


        Call<Result> resultCall = NetRetrofit.getInstance().getService().chatVideoUpload(body);

        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("Retrofit", "success  " + response.toString());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("Retrofit", "failed  " + call.toString() + "   " + t.toString());
                if (type.equals("image")) {
                    ChatRoomData data = new ChatRoomData();
                    String nowTime = getTime.nowGetTime();
                    data.setImage(fileName);
                    data.setItemViewType(8);
                    data.setMsgTime(getTime.chatRoomTime(nowTime));
                    data.setRead(false);
                    adapter.insertItem(data);

                    serverSend(chatRoomNum, fileName, nowTime, "video");
                }
            }
        });
    }

    public void applyFace(String num){
        try {
            dos.writeUTF(JsonToString.applyFace(num,userdata.getNum(),userNum,userdata.getNickname(),userdata.getTitleImage(),"applyFace"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
