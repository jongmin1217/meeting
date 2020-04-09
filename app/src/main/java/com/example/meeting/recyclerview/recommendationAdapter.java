package com.example.meeting.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meeting.R;
import com.example.meeting.activity.userprofile.UserProfileActivity;
import com.example.meeting.model.recommendationData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class recommendationAdapter extends RecyclerView.Adapter<recommendationAdapter.ItemViewHolder> {

    private ArrayList<recommendationData> listData = new ArrayList<>();
    private Context context;

    public recommendationAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recommendation, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) holder.itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;  // 핸드폰의 가로 해상도를 구함.
        deviceWidth = deviceWidth / 2;
        holder.itemView.getLayoutParams().width = deviceWidth;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        holder.itemView.requestLayout(); // 변경 사항 적용
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<recommendationData> dataList){
        listData = dataList;

        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private recommendationData recommendationData;
        private ImageView image;
        private TextView nickname,age,area;
        private ConstraintLayout layout;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            nickname = itemView.findViewById(R.id.nickname);
            age = itemView.findViewById(R.id.age);
            area = itemView.findViewById(R.id.area);
            layout = itemView.findViewById(R.id.layout);

            nickname.setPaintFlags(nickname.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            layout.setOnClickListener(this);
        }

        void onBind(recommendationData recommendationData) {
            this.recommendationData = recommendationData;

            Picasso.get().load(recommendationData.getUrl()).into(image);
            nickname.setText(recommendationData.getNickname());
            age.setText(recommendationData.getAge());
            area.setText(recommendationData.getArea());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("num",recommendationData.getNum());
            context.startActivity(intent);
        }
    }
}
