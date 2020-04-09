package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.model.LikeUserData;
import com.example.meeting.model.userData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemViewHolder>{

    private ArrayList<LikeUserData> listData = new ArrayList<>();
    private Context context;
    private userData userdata;

    public UserAdapter(Context context){
        Log.d("aassdd","UserAdapter");
        this.context = context;
        userdata = new userData(context);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_user, parent, false);
        Log.d("aassdd","onCreateViewHolder");
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d("aassdd","onBindViewHolder  "+listData.get(position).getUrl()+"  "+listData.get(position).getNum()+"  "+listData.get(position).getNickname());
        holder.onBind(listData.get(position));
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<LikeUserData> dataList){
        listData = dataList;
        Log.d("aassdd","getItem  "+listData.get(0).getUrl()+"  "+listData.get(0).getNum()+"  "+listData.get(0).getNickname());
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LikeUserData likeUserData;
        ImageView profileImage;
        TextView nickname;
        ConstraintLayout layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            layout = itemView.findViewById(R.id.layout);
        }

        public void onBind(LikeUserData likeUserData) {
            this.likeUserData = likeUserData;
            Log.d("aassdd","onbind  "+likeUserData.getUrl()+"  "+likeUserData.getNum()+"  "+likeUserData.getNickname());
            Picasso.get().load(likeUserData.getUrl()).into(profileImage);
            nickname.setText(likeUserData.getNickname());
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(!likeUserData.getNum().equals(userdata.getNum())){
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("num",likeUserData.getNum());
                context.startActivity(intent);
            }

        }
    }
}
