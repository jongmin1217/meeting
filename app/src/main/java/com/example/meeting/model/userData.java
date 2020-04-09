package com.example.meeting.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

public class userData {
    private SharedPreferences userData;
    private SharedPreferences.Editor editor;
    private SharedPreferences msgData;
    private SharedPreferences.Editor msgEditor;

    @SuppressLint("CommitPrefEdits")
    public userData(Context context) {
        userData = context.getSharedPreferences("userData", 0);
        editor = userData.edit();
        msgData = context.getSharedPreferences("msgData", 0);
        msgEditor = msgData.edit();
    }

    public void addNotification(){
        int number = userData.getInt("notiNum",0);
        editor.putInt("notiNum",number+1);
        editor.commit();
    }

    public void removeNotification(){
        int number = userData.getInt("notiNum",0);
        editor.putInt("notiNum",number-1);
        editor.commit();
    }

    public void resetNotification(){
        editor.putInt("notiNum",0);
        editor.commit();
    }

    public int getNotification(){
        return userData.getInt("notiNum",0);
    }

    public void addMsg(String num){
        int number = msgData.getInt(num,0);
        msgEditor.putInt(num,number+1);
        msgEditor.commit();
    }

    public void resetMsg(String num){
        msgEditor.putInt(num,0);
        msgEditor.commit();
    }

    public int getMsg(String num){
        return msgData.getInt(num,0);
    }

    public int getAllMsg(){
        Collection<?> allData = msgData.getAll().values();
        Iterator<?> allDataIt = allData.iterator();
        int allMsg =0;
        while(allDataIt.hasNext())
        {
            allMsg += (Integer)allDataIt.next();
        }
        return allMsg;
    }

    public int getAllBadge(){
        int noti = getNotification();
        int msg = getAllMsg();
        return noti+msg;
    }

    public void setUserData(String num, String email, String nickname, String image, String height, String form, String hobby, String idealType,
                            String smoking, String drinking, String birth, String area, String personality, String gender) {

        editor.putString("num", num);
        editor.putString("email", email);
        editor.putString("nickname", nickname);
        editor.putString("image", image);
        editor.putString("height", height);
        editor.putString("form", form);
        editor.putString("hobby", hobby);
        editor.putString("idealType", idealType);
        editor.putString("smoking", smoking);
        editor.putString("drinking", drinking);
        editor.putString("birth", birth);
        editor.putString("area", area);
        editor.putString("personality", personality);
        editor.putString("gender", gender);
        editor.commit();
    }

    public void removeUserData() {
        editor.clear();
        editor.commit();
    }

    public String getHobbyString() throws JSONException {
        ArrayList<String> array = getHobby();
        StringBuilder hobby= new StringBuilder();
        for(int i=0; i<array.size(); i++){
            if(i==0){
                hobby.append(array.get(i));
            }else{
                hobby.append(", ").append(array.get(i));
            }
        }
        return hobby.toString();
    }

    public String getPersonalityString() throws JSONException {
        ArrayList<String> array = getPersonality();
        StringBuilder personality= new StringBuilder();
        for(int i=0; i<array.size(); i++){
            if(i==0){
                personality.append(array.get(i));
            }else{
                personality.append(", ").append(array.get(i));
            }
        }
        return personality.toString();
    }

    public String getIdealTypeString() throws JSONException {
        ArrayList<String> array = getIdealType();
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

    public String getHobbyJson(){
        return userData.getString("hobby","");
    }

    public String getPersonalityJson(){
        return userData.getString("personality","");
    }
    public String getIdealTypeJson(){
        return userData.getString("idealType","");
    }

    public String getNum() {
        return userData.getString("num", "");
    }

    public void setNum(String num) {
        editor.putString("num", num);
        editor.commit();
    }

    public String getEmail() {
        return userData.getString("email", "");
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getNickname() {
        return userData.getString("nickname", "");
    }

    public void setNickname(String nickname) {
        editor.putString("nickname", nickname);
        editor.commit();
    }

    public ArrayList<String> getImage() throws JSONException {
        JSONArray json_array = new JSONArray(userData.getString("image", ""));
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }
        return listdata;
    }

    public String getTitleImage() throws JSONException {
        JSONArray json_array = new JSONArray(userData.getString("image", ""));
        ArrayList<String> listdata = new ArrayList<>();

        return json_array.getString(0);
    }

    public void setImage(String image) {
        editor.putString("image", image);
        editor.commit();
    }

    public String getHeight() {
        return userData.getString("height", "");
    }

    public void setHeight(String height) {
        editor.putString("height", height);
        editor.commit();
    }

    public String getForm() {
        return userData.getString("form", "");
    }

    public void setForm(String form) {
        editor.putString("form", form);
        editor.commit();
    }

    public ArrayList<String> getHobby() throws JSONException{
        JSONArray json_array = new JSONArray(userData.getString("hobby", ""));
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }
        return listdata;
    }

    public void setHobby(String hobby) {
        editor.putString("hobby", hobby);
        editor.commit();
    }

    public ArrayList<String> getIdealType() throws JSONException{
        JSONArray json_array = new JSONArray(userData.getString("idealType", ""));
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }
        return listdata;
    }

    public void setIdealType(String idealType) {
        editor.putString("idealType", idealType);
        editor.commit();
    }

    public String getSmoking() {
        return userData.getString("smoking", "");
    }

    public void setSmoking(String smoking) {
        editor.putString("smoking", smoking);
        editor.commit();
    }

    public String getDrinking() {
        return userData.getString("drinking", "");
    }

    public void setDrinking(String drinking) {
        editor.putString("drinking", drinking);
        editor.commit();
    }

    public String getBirth() {
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        int nYear = calendar.get(Calendar.YEAR);

        int birth = Integer.parseInt(userData.getString("birth", "").substring(0, 4));

        String age = Integer.toString((nYear+1)-birth);
        return age;
    }

    public String getYear(){
        String[] par = userData.getString("birth","").split("/");
        return par[0];
    }

    public String getMonth(){
        String[] par = userData.getString("birth","").split("/");
        return par[1];
    }

    public String getDay(){
        String[] par = userData.getString("birth","").split("/");
        return par[2];
    }

    public void setBirth(String birth) {
        editor.putString("birth", birth);
        editor.commit();
    }

    public String getArea() {
        return userData.getString("area", "");
    }

    public void setArea(String area) {
        editor.putString("area", area);
        editor.commit();
    }

    public ArrayList<String> getPersonality() throws JSONException{
        JSONArray json_array = new JSONArray(userData.getString("personality", ""));
        ArrayList<String> listdata = new ArrayList<>();

        for (int i = 0; i < json_array.length(); i++) {
            listdata.add(json_array.getString(i));
        }
        return listdata;
    }

    public void setPersonality(String personality) {
        editor.putString("personality", personality);
        editor.commit();
    }

    public String getGender() {
        return userData.getString("gender", "");
    }

    public void setGender(String gender) {
        editor.putString("gender", gender);
        editor.commit();
    }

}
