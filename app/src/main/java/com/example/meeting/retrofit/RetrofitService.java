package com.example.meeting.retrofit;

import javax.xml.transform.Result;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("loginTry.php")
    Call<ResponseBody> loginTry(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "password", encoded = true) String password);

    @FormUrlEncoded
    @POST("signUpTry.php")
    Call<ResponseBody> signUpTry(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "password", encoded = true) String password,
            @Field(value = "phoneNumber", encoded = true) String phoneNumber,
            @Field(value = "code", encoded = true) String code);



    @FormUrlEncoded
    @POST("certificationCodeTry.php")
    Call<ResponseBody> certificationCodeTry(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "certificationCode", encoded = true) String certificationCode);

    @FormUrlEncoded
    @POST("nicknameCheckTry.php")
    Call<ResponseBody> nicknameCheckTry(
            @Field(value = "nickname", encoded = true) String nickname);

    @FormUrlEncoded
    @POST("profileWrite.php")
    Call<ResponseBody> profileWrite(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "selectArea", encoded = true) String selectArea,
            @Field(value = "selectBirth", encoded = true) String selectBirth,
            @Field(value = "selectGender", encoded = true) String selectGender,
            @Field(value = "selectHeight", encoded = true) String selectHeight,
            @Field(value = "selectForm", encoded = true) String selectForm,
            @Field(value = "selectSmoke", encoded = true) String selectSmoke,
            @Field(value = "selectDrink", encoded = true) String selectDrink,
            @Field(value = "selectPersonality", encoded = true) String selectPersonality,
            @Field(value = "selectHobby", encoded = true) String selectHobby,
            @Field(value = "selectIdealType", encoded = true) String selectIdealType,
            @Field(value = "selectNickname", encoded = true) String selectNickname);

    @FormUrlEncoded
    @POST("profileWriteEdit.php")
    Call<ResponseBody> profileWriteEdit(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "selectArea", encoded = true) String selectArea,
            @Field(value = "selectBirth", encoded = true) String selectBirth,
            @Field(value = "selectGender", encoded = true) String selectGender,
            @Field(value = "selectHeight", encoded = true) String selectHeight,
            @Field(value = "selectForm", encoded = true) String selectForm,
            @Field(value = "selectSmoke", encoded = true) String selectSmoke,
            @Field(value = "selectDrink", encoded = true) String selectDrink,
            @Field(value = "selectPersonality", encoded = true) String selectPersonality,
            @Field(value = "selectHobby", encoded = true) String selectHobby,
            @Field(value = "selectIdealType", encoded = true) String selectIdealType,
            @Field(value = "selectNickname", encoded = true) String selectNickname);

    @FormUrlEncoded
    @POST("profileImageSet.php")
    Call<ResponseBody> profileImageSet(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "image", encoded = true) String image);

    @FormUrlEncoded
    @POST("profileImageEdit.php")
    Call<ResponseBody> profileImageEdit(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "image", encoded = true) String image);

    @FormUrlEncoded
    @POST("postWrite.php")
    Call<ResponseBody> postWrite(
            @Field(value = "writerNum", encoded = true) String writerNum,
            @Field(value = "image", encoded = true) String image,
            @Field(value = "postText", encoded = true) String postText,
            @Field(value = "postTime", encoded = true) String postTime,
            @Field(value = "gender", encoded = true) String gender);

    @FormUrlEncoded
    @POST("postLike.php")
    Call<ResponseBody> postLike(
            @Field(value = "num", encoded = true) String num,
            @Field(value = "user", encoded = true) String user,
            @Field(value = "like", encoded = true) boolean like);

    @FormUrlEncoded
    @POST("postComentInsert.php")
    Call<ResponseBody> postComentInsert(
            @Field(value = "num", encoded = true) String num,
            @Field(value = "user", encoded = true) String user,
            @Field(value = "coment", encoded = true) String coment,
            @Field(value = "comentTime", encoded = true) String comentTime);

    @FormUrlEncoded
    @POST("removeComent.php")
    Call<ResponseBody> removeComent(
            @Field(value = "num", encoded = true) String num);

    @FormUrlEncoded
    @POST("postDelete.php")
    Call<ResponseBody> postDelete(
            @Field(value = "num", encoded = true) String num);

    @FormUrlEncoded
    @POST("setToken.php")
    Call<ResponseBody> setToken(
            @Field(value = "token", encoded = true) String token,
            @Field(value = "num", encoded = true) String num);

    @FormUrlEncoded
    @POST("removeToken.php")
    Call<ResponseBody> removeToken(
            @Field(value = "num", encoded = true) String num);

    @FormUrlEncoded
    @POST("signUpRecommendation.php")
    Call<ResponseBody> signUpRecommendation(
            @Field(value = "num", encoded = true) String num);

    @FormUrlEncoded
    @POST("fcm.php")
    Call<ResponseBody> fcm(
            @Field(value = "num", encoded = true) String num,
            @Field(value = "sendNum", encoded = true) String sendNum,
            @Field(value = "receiveNum", encoded = true) String receiveNum,
            @Field(value = "type", encoded = true) String type,
            @Field(value = "nickname", encoded = true) String nickname,
            @Field(value = "imageUrl", encoded = true) String imageUrl,
            @Field(value = "message", encoded = true) String message);

    @FormUrlEncoded
    @POST("insertNotification.php")
    Call<ResponseBody> insertNotification(
            @Field(value = "contentNum", encoded = true) String contentNum,
            @Field(value = "sendUser", encoded = true) String sendUser,
            @Field(value = "type", encoded = true) String type,
            @Field(value = "contentImage", encoded = true) String contentImage,
            @Field(value = "config", encoded = true) boolean config,
            @Field(value = "notiTime", encoded = true) String notiTime,
            @Field(value = "receiveNum", encoded = true) String receiveNum);


    @FormUrlEncoded
    @POST("editUserStatus.php")
    Call<ResponseBody> editUserStatus(
            @Field(value = "num", encoded = true) String num,
            @Field(value = "userNum", encoded = true) String userNum,
            @Field(value = "type", encoded = true) String type,
            @Field(value = "likeNum", encoded = true) String likeNum,
            @Field(value = "time", encoded = true) String time);

    @FormUrlEncoded
    @POST("insertMessage.php")
    Call<ResponseBody> insertMessage(
            @Field(value = "chatNum", encoded = true) String chatNum,
            @Field(value = "writerNum", encoded = true) String writerNum,
            @Field(value = "msg", encoded = true) String msg,
            @Field(value = "msgTime", encoded = true) String msgTime,
            @Field(value = "msgType", encoded = true) String msgType);

    @FormUrlEncoded
    @POST("chatRead.php")
    Call<ResponseBody> chatRead(
            @Field(value = "chatNum", encoded = true) String chatNum,
            @Field(value = "userNum", encoded = true) String userNum);

    @FormUrlEncoded
    @POST("removeChatList.php")
    Call<ResponseBody> removeChatList(
            @Field(value = "num", encoded = true) String num,
            @Field(value = "sendNum", encoded = true) String sendNum,
            @Field(value = "receiveNum", encoded = true) String receiveNum);

    @GET("userInfo.php")
    Call<ResponseBody> userInfo(
            @Query("email") String email);

    @GET("userProfile.php")
    Call<ResponseBody> userProfile(
            @Query("num") String num);

    @GET("myPostList.php")
    Call<ResponseBody> myPostList(
            @Query("writerNum") String writerNum);

    @GET("getPostList.php")
    Call<ResponseBody> getPostList(
            @Query("gender") String gender);

    @GET("likeUser.php")
    Call<ResponseBody> likeUser(
            @Query("num") String num);

    @GET("getPostInfo.php")
    Call<ResponseBody> getPostInfo(
            @Query("num") String num);

    @GET("likeConf.php")
    Call<ResponseBody> likeConf(
            @Query("user") String user,
            @Query("num") String num);

    @GET("getPostComent.php")
    Call<ResponseBody> getPostComent(
            @Query("num") String num);

    @GET("getPreviousRecommendation.php")
    Call<ResponseBody> getPreviousRecommendation(
            @Query("num") String num);

    @GET("getRecommendation.php")
    Call<ResponseBody> getRecommendation(
            @Query("num") String num);

    @GET("selectNotification.php")
    Call<ResponseBody> selectNotification(
            @Query("num") String num);

    @GET("getUserStatus.php")
    Call<ResponseBody> getUserStatus(
            @Query("num") String num,
            @Query("userNum") String userNum);

    @GET("getMessage.php")
    Call<ResponseBody> getMessage(
            @Query("num") String num);

    @GET("getChatList.php")
    Call<ResponseBody> getChatList(
            @Query("num") String num);

    @GET("fcmChatRoom.php")
    Call<ResponseBody> fcmChatRoom(
            @Query("num") String num,
            @Query("userNum") String userNum);

    @GET("chatClick.php")
    Call<ResponseBody> chatClick(
            @Query("num") String num,
            @Query("userNum") String userNum);

    @Multipart
    @POST("profileImageUpload.php")
    Call<Result> profileImageUpload(
            @Part MultipartBody.Part File);

    @Multipart
    @POST("postImageUpload.php")
    Call<Result> postImageUpload(
            @Part MultipartBody.Part File);

    @Multipart
    @POST("chatImageUpload.php")
    Call<Result> chatImageUpload(
            @Part MultipartBody.Part File);

    @Multipart
    @POST("chatVideoUpload.php")
    Call<Result> chatVideoUpload(
            @Part MultipartBody.Part File);
}
