package com.example.meeting.activity.applyFace;

import com.example.meeting.custom.JsonToString;

import java.io.IOException;

import static com.example.meeting.service.socketServer.dos;

public class ApplyFacePresenter {

    private ApplyFaceView view;

    public ApplyFacePresenter(ApplyFaceView view){
        this.view = view;
    }

    public void refuseFace(String userNum){
        try {
            dos.writeUTF(JsonToString.refuseFace(userNum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
