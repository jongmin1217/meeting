package com.example.meeting.activity.profileedit.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meeting.model.userData;
import com.example.meeting.model.writingUserData;
import com.example.meeting.retrofit.NetRetrofit;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditWritePresenter {
    ProfileEditWriteView view;
    userData userdata;
    writingUserData writingUserData;

    public ProfileEditWritePresenter(ProfileEditWriteView view) {
        this.view = view;
        userdata = new userData(view.activity());
        writingUserData = new writingUserData(view.activity());
    }

    void writingChange(String area,String year,String month,String day,String gender,String height,String form,String smoke,String drink,
                       String nickname,String hobby,String personality,String idealType,boolean nicknameCheck,int nicknameCheckNum){
        writingUserData.setArea(area);
        writingUserData.setBirth(year+"/"+month+"/"+day);
        writingUserData.setGender(gender);
        writingUserData.setHeight(height);
        writingUserData.setForm(form);
        writingUserData.setSmoking(smoke);
        writingUserData.setDrinking(drink);
        writingUserData.setNickname(nickname);
        writingUserData.setHobby(hobby);
        writingUserData.setPersonality(personality);
        writingUserData.setIdealType(idealType);
        writingUserData.setNicknameCheck(nicknameCheck);
        writingUserData.setNicknameCheckNum(nicknameCheckNum);
    }

    int getIndex(Spinner spinner, String item) {
        String type = "";
        if(writingUserData.getArea().equals("")){
            if (item.equals("area")) {
                type = userdata.getArea();
            } else if (item.equals("year")) {
                type = userdata.getYear();
            } else if (item.equals("month")) {
                type = userdata.getMonth();
            } else if (item.equals("day")) {
                type = userdata.getDay();
            } else if (item.equals("gender")) {
                type = userdata.getGender();
            } else if (item.equals("height")) {
                type = userdata.getHeight();
            } else if (item.equals("form")) {
                type = userdata.getForm();
            } else if (item.equals("smoke")) {
                type = userdata.getSmoking();
            } else if (item.equals("drink")) {
                type = userdata.getDrinking();
            }
        }else{
            if (item.equals("area")) {
                type = writingUserData.getArea();
            } else if (item.equals("year")) {
                type = writingUserData.getYear();
            } else if (item.equals("month")) {
                type = writingUserData.getMonth();
            } else if (item.equals("day")) {
                type = writingUserData.getDay();
            } else if (item.equals("gender")) {
                type = writingUserData.getGender();
            } else if (item.equals("height")) {
                type = writingUserData.getHeight();
            } else if (item.equals("form")) {
                type = writingUserData.getForm();
            } else if (item.equals("smoke")) {
                type = writingUserData.getSmoking();
            } else if (item.equals("drink")) {
                type = writingUserData.getDrinking();
            }
        }
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(type)) {
                return i;
            }
        }
        return 0;
    }

    void alertDialog(final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.activity());
        final ArrayList<String> selectedItems = new ArrayList();
        String[] test = new String[0];
        if (type.equals("personality")) {
            test = new String[]{"지적인", "차분한", "유머있는", "낙천적인", "내향적인", "외향적인", "감성적인", "상냥한", "귀여운", "열정적인", "듬직한", "개성있는"};
            builder.setTitle("성격 선택");
        } else if (type.equals("hobby")) {
            test = new String[]{"스포츠", "영화감상", "음악감상", "음주", "드라이빙", "독서", "등산", "공부"};
            builder.setTitle("취미 선택");
        } else if (type.equals("idealType")) {
            test = new String[]{"지적인", "차분한", "유머있는", "낙천적인", "내향적인", "외향적인", "감성적인", "상냥한", "귀여운", "열정적인", "듬직한", "개성있는"};
            builder.setTitle("이상형 선택");
        }

        final String[] items = test;


        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos, boolean isChecked) {
                if (isChecked == true) // Checked 상태일 때 추가
                {
                    selectedItems.add(items[pos]);
                } else                  // Check 해제 되었을 때 제거
                {
                    selectedItems.remove(items[pos]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                String SeletedItemsString = "";

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (i == 0) {
                        SeletedItemsString = selectedItems.get(i);
                    } else {
                        SeletedItemsString = SeletedItemsString + ", " + selectedItems.get(i);
                    }

                }
                Gson gson = new Gson();
                String jsonPlace = gson.toJson(selectedItems);
                view.selectDialog(type, SeletedItemsString, jsonPlace);


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    void getUserInfo() throws JSONException {
        if(writingUserData.getArea().equals("")){
            view.getUserInfo(userdata.getNickname(), userdata.getHobbyString(), userdata.getPersonalityString(), userdata.getIdealTypeString(),
                    userdata.getHobbyJson(),userdata.getPersonalityJson(),userdata.getIdealTypeJson(),true,0);
        }else{
            view.getUserInfo(writingUserData.getNickname(), writingUserData.getHobbyString(), writingUserData.getPersonalityString(), writingUserData.getIdealTypeString(),
                    writingUserData.getHobbyJson(),writingUserData.getPersonalityJson(),writingUserData.getIdealTypeJson(),writingUserData.getNicknameCheck(),writingUserData.getNicknameCheckNum());
        }

    }

    void nicknameCheckTry(final String nickname) {
        if(nickname.equals(userdata.getNickname())){
            Toast.makeText(view.activity(), "동일한 닉네임입니다", Toast.LENGTH_SHORT).show();
        }else{
            Call<ResponseBody> res = NetRetrofit.getInstance().getService().nicknameCheckTry(nickname);
            res.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        Log.d("ProfileWriteActivity", "결과값  " + result);
                        if (result.equals("succes")) {
                            view.nicknameCheckSucces(nickname);
                        } else {
                            view.nicknameCheckFail();
                        }
                    } catch (Exception e) {
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

    void profileWrite(String email, String selectArea, String selectYear, String selectMonth, String selectDay, String selectGender,
                      String selectHeight, String selectForm, String selectSmoke, String selectDrink, String selectPersonality,
                      String selectHobby, String selectIdealType, String selectNickname, boolean nicknameCheck, int num,String nickname) {
        if(!nickname.equals(selectNickname)){
            Toast.makeText(view.activity(), "닉네임인증을 확인해주세요", Toast.LENGTH_SHORT).show();
        }else{
            if (selectArea.equals("지역") || selectYear.equals("년도") || selectMonth.equals("달") || selectDay.equals("날") || selectGender.equals("성별") ||
                    selectHeight.equals("키") || selectForm.equals("몸매") || selectSmoke.equals("흡연여부") || selectDrink.equals("음주여부") || selectPersonality == null ||
                    selectHobby == null || selectIdealType == null) {
                view.noInsert();
            } else {
                String selectBirth = selectYear + "/" + selectMonth + "/" + selectDay;
                userdata.setArea(selectArea);
                userdata.setBirth(selectBirth);
                userdata.setGender(selectGender);
                userdata.setHeight(selectHeight);
                userdata.setForm(selectForm);
                userdata.setSmoking(selectSmoke);
                userdata.setDrinking(selectDrink);
                userdata.setPersonality(selectPersonality);
                userdata.setHobby(selectHobby);
                userdata.setIdealType(selectIdealType);
                userdata.setNickname(selectNickname);
                Call<ResponseBody> res = NetRetrofit.getInstance().getService().profileWriteEdit(email, selectArea, selectBirth, selectGender,
                        selectHeight, selectForm, selectSmoke, selectDrink, selectPersonality, selectHobby, selectIdealType, selectNickname);
                res.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Log.d("profileWriteResulr", "결과값  " + result);

                        } catch (Exception e) {
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
}
