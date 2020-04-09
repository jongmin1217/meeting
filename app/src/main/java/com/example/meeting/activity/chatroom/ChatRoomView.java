package com.example.meeting.activity.chatroom;

public interface ChatRoomView {
    void scroll();
    void getScroll(String profileImage,String msg);
    void showProgress();
    void hideProgress();
}
