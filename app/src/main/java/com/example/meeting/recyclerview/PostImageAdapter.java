package com.example.meeting.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.meeting.R;
import com.example.meeting.activity.post.PostActivity;
import com.example.meeting.model.PostImageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ItemViewHolder> {

    private ArrayList<PostImageData> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_postimage, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) holder.itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;  // 핸드폰의 가로 해상도를 구함.
        deviceWidth = deviceWidth / 3;
        holder.itemView.getLayoutParams().width = deviceWidth;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        holder.itemView.requestLayout(); // 변경 사항 적용
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void getItem(ArrayList<PostImageData> dataList){
        listData = dataList;
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image;
        private PostImageData postImageData;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

        void onBind(PostImageData postImageData) {
            this.postImageData = postImageData;
            Picasso.get().load(postImageData.getUrl()).into(image);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PostActivity.class);
            intent.putExtra("num",postImageData.getNum());
            intent.putExtra("position",getAdapterPosition());
            Activity activity = (Activity)view.getContext();
            activity.startActivityForResult(intent,3000);
        }
    }
}
