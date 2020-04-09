package com.example.meeting.activity.signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.model.userData;
import com.example.meeting.retrofit.NetRetrofit;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileWritePresenter {

    private ProfileWriteView view;

    public ProfileWritePresenter(ProfileWriteView view){
        this.view = view;
    }

    void nicknameCheckTry(final String nickname){
        if(nickname.equals("")){
            view.nicknameCheckFail();
        }else{
            Call<ResponseBody> res = NetRetrofit.getInstance().getService().nicknameCheckTry(nickname);
            res.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        Log.d("ProfileWriteActivity", "결과값  " + result);
                        if(result.equals("succes")){
                            view.nicknameCheckSucces(nickname);
                        }else{
                            view.nicknameCheckFail();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Err", t.getMessage());
                    Log.d("loginActivity","실패"+t.getMessage());

                }
            });
        }

    }

    void alertDialog(final String type){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final ArrayList<String> selectedItems = new ArrayList();
        String[] test = new String[0];
        if(type.equals("personality")){
            test = new String[]{"지적인","차분한","유머있는","낙천적인","내향적인","외향적인","감성적인","상냥한","귀여운","열정적인","듬직한","개성있는"};
            builder.setTitle("성격 선택");
        }else if(type.equals("hobby")){
            test = new String[]{"스포츠","영화감상","음악감상","음주","드라이빙","독서","등산","공부"};
            builder.setTitle("취미 선택");
        }else if(type.equals("idealType")){
            test = new String[]{"지적인","차분한","유머있는","낙천적인","내향적인","외향적인","감성적인","상냥한","귀여운","열정적인","듬직한","개성있는"};
            builder.setTitle("이상형 선택");
        }

        final String[] items = test;



        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
            {
                if(isChecked == true) // Checked 상태일 때 추가
                {
                    selectedItems.add(items[pos]);
                }
                else				  // Check 해제 되었을 때 제거
                {
                    selectedItems.remove(items[pos]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String SeletedItemsString = "";

                for(int i =0; i<selectedItems.size();i++)
                {
                    if(i==0){
                        SeletedItemsString = selectedItems.get(i);
                    }else{
                        SeletedItemsString =  SeletedItemsString + ", " + selectedItems.get(i);
                    }

                }
                Gson gson = new Gson();
                String jsonPlace = gson.toJson(selectedItems);
                view.selectDialog(type,SeletedItemsString,jsonPlace);



            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    void profileWrite(String email,String selectArea,String selectYear,String selectMonth,String selectDay,String selectGender,
                      String selectHeight,String selectForm,String selectSmoke,String selectDrink, String selectPersonality,
                      String selectHobby,String selectIdealType,String selectNickname,boolean nicknameCheck){
        if(nicknameCheck==false){
            view.noVerificationNickname();
        }else{
            if(selectArea.equals("지역")||selectYear.equals("년도")||selectMonth.equals("달")||selectDay.equals("날")||selectGender.equals("성별")||
                    selectHeight.equals("키")||selectForm.equals("몸매")||selectSmoke.equals("흡연여부")||selectDrink.equals("음주여부")||selectPersonality==null||
                    selectHobby==null||selectIdealType==null){
                view.noInsert();
            }else{
                String selectBirth = selectYear+"/"+selectMonth+"/"+selectDay;
                Call<ResponseBody> res = NetRetrofit.getInstance().getService().profileWrite(email, selectArea, selectBirth, selectGender,
                         selectHeight, selectForm, selectSmoke, selectDrink,  selectPersonality, selectHobby, selectIdealType, selectNickname);
                res.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Log.d("profileWriteResulr", "결과값  " + result);
                            if(result.equals("succes")){

                                Toast.makeText(view.getContext(), "프로필사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                                view.profileWriteSucces(email);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Err", t.getMessage());
                        Log.d("loginActivity","실패"+t.getMessage());

                    }
                });
            }
        }


    }

}
