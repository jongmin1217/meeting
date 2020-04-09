package com.example.meeting.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meeting.R;
import com.example.meeting.activity.main.fragment.MoveView;
import com.example.meeting.activity.movie.MovieActivity;
import com.example.meeting.model.MoveData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ItemViewHolder> {

    private ArrayList<MoveData> listData = new ArrayList<>();
    private Context context;
    private MoveView view;

    public MoveAdapter(Context context,MoveView view){
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_move, parent, false);

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

    public void getItem(ArrayList<MoveData> dataList){
        listData = dataList;
        notifyDataSetChanged();
        view.hideProgress();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MoveData moveData;
        ImageView moveImage,star;
        TextView textTitle,textAge,textStar,textExp,textRelease,textDirector,textActor,actor;
        ConstraintLayout layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            star = itemView.findViewById(R.id.star);
            moveImage = itemView.findViewById(R.id.moveImage);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAge = itemView.findViewById(R.id.textAge);
            textStar = itemView.findViewById(R.id.textStar);
            textExp = itemView.findViewById(R.id.textExp);
            textRelease = itemView.findViewById(R.id.textRelease);
            textDirector = itemView.findViewById(R.id.textDirector);
            textActor = itemView.findViewById(R.id.textActor);
            actor = itemView.findViewById(R.id.actor);

            layout.setOnClickListener(this);
        }

        public void onBind(MoveData moveData) {
            this.moveData = moveData;
            star.setColorFilter(Color.parseColor("#ff9999"));
            Glide.with(context).load(moveData.getImgUrl()).into(moveImage);
            textTitle.setText(moveData.getTitle());
            String agepar = moveData.getAge().substring(0, 2);
            GradientDrawable drawable;
            switch (agepar){
                case "12":
                    drawable = (GradientDrawable) context.getDrawable(R.drawable.circle_age_12);
                    textAge.setText("12");
                    break;
                case "15":
                    drawable = (GradientDrawable) context.getDrawable(R.drawable.circle_age_15);
                    textAge.setText("15");
                    break;
                case "전체":
                    drawable = (GradientDrawable) context.getDrawable(R.drawable.circle_age_all);
                    textAge.setText("전체");
                    break;
                default:
                    drawable = (GradientDrawable) context.getDrawable(R.drawable.circle_age_18);
                    textAge.setText("청불");
                    break;
            }
            textAge.setBackground(drawable);
            textAge.setClipToOutline(true);
            textStar.setText(moveData.getStar());
            textExp.setText(moveData.getExp());
            textRelease.setText(moveData.getRelease());
            textDirector.setText(moveData.getDirector());
            if(moveData.getActor().equals("")){
                actor.setVisibility(View.INVISIBLE);
                textActor.setVisibility(View.INVISIBLE);
            }else{
                textActor.setText(moveData.getActor());
            }

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MovieActivity.class);
            intent.putExtra("url",moveData.getLink());
            context.startActivity(intent);
        }
    }
}
