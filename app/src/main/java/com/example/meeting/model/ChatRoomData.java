package com.example.meeting.model;

import android.net.Uri;

import java.util.ArrayList;

public class ChatRoomData {
    private String userNum;
    private String type;
    private String msg;
    private String msgTime;
    private String image;
    private int itemViewType;
    private Uri imageUri;
    private String imageType;
    private int imageNum;
    private ArrayList<Uri> imageUriList;
    private ArrayList<String> imageUrlList;
    private boolean isRead;
    private boolean timeType;

    public boolean isTimeType() {
        return timeType;
    }

    public void setTimeType(boolean timeType) {
        this.timeType = timeType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public ArrayList<Uri> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(ArrayList<Uri> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
