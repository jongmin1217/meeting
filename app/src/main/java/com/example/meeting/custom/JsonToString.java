package com.example.meeting.custom;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonToString {
    public JsonToString(){

    }

    public String start(String num) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","start");
            object.put("num",num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String end(String num) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","end");
            object.put("num",num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String postLike(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","postLike");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String postComent(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","postComent");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String sendLike(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","userLike");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String connect(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","connect");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String disconnect(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();

        try {
            object.put("type","disconnect");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String sendMessage(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","message");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String sendChatRead(String num,String receiveNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","chatRead");
            object.put("num",num);
            object.put("receiveNum",receiveNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String applyFace(String num,String sendNum,String receiveNum,String nickname,String imageUrl,String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","applyFace");
            object.put("num",num);
            object.put("sendNum",sendNum);
            object.put("receiveNum",receiveNum);
            object.put("nickname",nickname);
            object.put("imageUrl",imageUrl);
            object.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String cancelFace(String receiveNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","cancelFace");
            object.put("receiveNum",receiveNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String refuseFace(String receiveNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","refuseFace");
            object.put("receiveNum",receiveNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String facetrans(String receiveNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("type","facetrans");
            object.put("receiveNum",receiveNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
