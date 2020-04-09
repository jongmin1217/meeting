package com.example.meeting.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.chatlist.ChatListPresenter;
import com.example.meeting.activity.chatlist.ChatListView;
import com.example.meeting.activity.chatroom.ChatRoomActivity;
import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.ItemSwipeHelperCallback;
import com.example.meeting.model.ChatListData;
import com.example.meeting.model.userData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ItemViewHolder> implements ItemSwipeHelperCallback.ItemTouchHelperAdapter{

    private Context context;
    private GetTime getTime;
    private userData userdata;
    private ChatListView view;
    ArrayList<ChatListData> listData = new ArrayList<>();

    public ChatListAdapter(Context context, ChatListView view){
        this.context = context;
        this.view = view;
        getTime = new GetTime();
        userdata = new userData(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<ChatListData> listData){
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    public void removeItem(int position, ChatListPresenter presenter){
        presenter.removeChatList(listData.get(position).getNum(),listData.get(position).getUserNum());

        listData.remove(position);
        notifyItemRemoved(position);

    }

    public void returnItem(){
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        view.removeChatList(position);
    }

    @Override
    public boolean swipeEnable(int position) {
        return true;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChatListData chatListData;
        private ImageView profileImage;
        private TextView nickname,message,time,cnt;
        private ConstraintLayout layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            cnt = itemView.findViewById(R.id.cnt);
            layout = itemView.findViewById(R.id.layout);
        }

        @SuppressLint("SetTextI18n")
        public void onBind(ChatListData chatListData) {
            this.chatListData = chatListData;

            Picasso.get().load(chatListData.getUrl()).into(profileImage);
            nickname.setText(chatListData.getNickname());
            message.setText(chatListData.getLastMsg());
            time.setText(getTime.timeString(chatListData.getLastMsgTime()));
            if(userdata.getMsg(chatListData.getNum())!=0){
                cnt.setVisibility(View.VISIBLE);
                cnt.setText(userdata.getMsg(chatListData.getNum())+"");
            }else{
                cnt.setVisibility(View.INVISIBLE);
            }
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, ChatRoomActivity.class);
            intent.putExtra("num",chatListData.getNum());
            intent.putExtra("nickname",chatListData.getNickname());
            intent.putExtra("profileImage",chatListData.getUrl());
            intent.putExtra("userNum",chatListData.getUserNum());
            intent.putExtra("type","normal");
            context.startActivity(intent);
        }
    }
}
