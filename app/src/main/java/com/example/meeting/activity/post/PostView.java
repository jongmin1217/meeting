package com.example.meeting.activity.post;

public interface PostView {
    void getPostInfo(String writerNum,String profileImage,String nickname,String image,String postText,String postTime,String postLike,boolean myPost);
    void likeTrue();
    void likeFalse();
    void noComent();
    void invisible();
    void removeComent(int position);
    void endRefreshing(boolean coment);
}
