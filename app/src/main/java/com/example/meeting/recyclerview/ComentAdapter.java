package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.post.PostPresenter;
import com.example.meeting.activity.post.PostView;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.custom.GetTime;
import com.example.meeting.custom.ItemSwipeHelperCallback;
import com.example.meeting.model.ComentData;
import com.example.meeting.model.userData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ComentAdapter extends RecyclerView.Adapter<ComentAdapter.ItemViewHolder> implements ItemSwipeHelperCallback.ItemTouchHelperAdapter {

    private ArrayList<ComentData> listData = new ArrayList<>();
    private Context context;
    private userData userdata;
    private PostView view;
    private GetTime getTime;

    public ComentAdapter(Context context, PostView view){
        this.context = context;
        this.view = view;
        userdata = new userData(context);
        getTime = new GetTime();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_coment, parent, false);

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

    public void getItem(ArrayList<ComentData> dataList){
        listData = dataList;
        notifyDataSetChanged();
    }

    public void addItem(ComentData dataList){
        listData.add(0,dataList);

        notifyItemInserted(0);
    }

    public void removeItem(int position, PostPresenter presenter){
        presenter.removeComent(listData.get(position).getNum());

        listData.remove(position);
        notifyItemRemoved(position);
        if(listData.size()==0){
            view.noComent();
        }
    }

    public void removeAllItem(){
        listData.clear();
        notifyDataSetChanged();
    }

    public void returnItem(){
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        view.removeComent(position);
    }

    @Override
    public boolean swipeEnable(int position){
        if(listData.get(position).getUserNum().equals(userdata.getNum())){
            return true;
        }else{
            return false;
        }
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ComentData comentData;
        ImageView profileImage;
        TextView nickname,coment,comentTime;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            nickname = itemView.findViewById(R.id.nickname);
            coment = itemView.findViewById(R.id.coment);
            comentTime = itemView.findViewById(R.id.comentTime);


        }


        public void onBind(ComentData comentData) {
            this.comentData = comentData;

            Picasso.get().load(comentData.getUrl()).into(profileImage);
            nickname.setText(comentData.getNickname());
            coment.setText(comentData.getComent());
            comentTime.setText(getTime.timeString(comentData.getComentTime()));
            profileImage.setOnClickListener(this);
            nickname.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.profileImage:
                case R.id.nickname:
                    if(!comentData.getUserNum().equals(userdata.getNum())){
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("num",comentData.getUserNum());
                        context.startActivity(intent);
                    }

                    break;
            }
        }
    }
}
