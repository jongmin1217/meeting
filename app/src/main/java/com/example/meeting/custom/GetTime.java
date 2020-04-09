package com.example.meeting.custom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
    public GetTime() {

    }

    public String nowGetTime() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy / MM / dd - kk시 mm분");
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        String time = mFormat.format(mDate);

        return time;
    }

    private String nowGetDay() {
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy / MM / dd");
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        String time = mFormat.format(mDate);
        return time;
    }

    public String timeString(String time) {
        if (time.substring(0, 14).equals(nowGetDay())) {
            if (12 <= Integer.valueOf(time.substring(17, 19))) {
                if(24== Integer.valueOf(time.substring(17, 19))){
                    return "오전 12:" + time.substring(21, 23);
                }else{
                    return "오후 " + (Integer.valueOf(time.substring(17, 19)) - 12) + ":" + time.substring(21, 23);
                }

            } else {
                if (12 > Integer.valueOf(time.substring(17, 19)) && 9 < Integer.valueOf(time.substring(17, 19))) {
                    return "오전 " + time.substring(17, 19) + ":" + time.substring(21, 23);
                } else {
                    return "오전 " + time.substring(18, 19) + ":" + time.substring(21, 23);
                }
            }
        } else {
            if (10 <= Integer.valueOf(time.substring(7, 9))) {
                if (10 <= Integer.valueOf(time.substring(12, 14))) {
                    return time.substring(7, 9) + "월 " + time.substring(12, 14) + "일";
                } else {
                    return time.substring(7, 9) + "월 " + time.substring(13, 14) + "일";
                }
            } else {
                if (10 <= Integer.valueOf(time.substring(12, 14))) {
                    return time.substring(8, 9) + "월 " + time.substring(12, 14) + "일";
                } else {
                    return time.substring(8, 9) + "월 " + time.substring(13, 14) + "일";
                }
            }
        }
    }

    public String chatTime(String time) {

        if (10 <= Integer.valueOf(time.substring(7, 9))) {
            if (10 <= Integer.valueOf(time.substring(12, 14))) {
                return time.substring(0, 4) + "년 " + time.substring(7, 9) + "월 " + time.substring(12, 14) + "일";
            } else {
                return time.substring(0, 4) + "년 " + time.substring(7, 9) + "월 " + time.substring(13, 14) + "일";
            }
        } else {
            if (10 <= Integer.valueOf(time.substring(12, 14))) {
                return time.substring(0, 4) + "년 " + time.substring(8, 9) + "월 " + time.substring(12, 14) + "일";
            } else {
                return time.substring(0, 4) + "년 " + time.substring(8, 9) + "월 " + time.substring(13, 14) + "일";
            }
        }

    }

    public String chatRoomTime(String time) {
        if (12 <= Integer.valueOf(time.substring(17, 19))) {
            if(24== Integer.valueOf(time.substring(17, 19))){
                return "오전 12:" + time.substring(21, 23);
            }else{
                return "오후 " + (Integer.valueOf(time.substring(17, 19)) - 12) + ":" + time.substring(21, 23);
            }

        } else {
            if (12 > Integer.valueOf(time.substring(17, 19)) && 9 < Integer.valueOf(time.substring(17, 19))) {
                return "오전 " + time.substring(17, 19) + ":" + time.substring(21, 23);
            } else {
                return "오전 " + time.substring(18, 19) + ":" + time.substring(21, 23);
            }
        }
    }
}
