package com.example.meeting.recyclerview;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.meeting.R;
import com.example.meeting.activity.signup.ProfileImagePresenter;
import com.example.meeting.model.ProfileImageData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileImageAdapter extends RecyclerView.Adapter<ProfileImageAdapter.ItemViewHolder> {

    private ArrayList<ProfileImageData> listData = new ArrayList<>();
    private ProfileImagePresenter presenter;

    public ProfileImageAdapter(ProfileImagePresenter presenter) {

        this.presenter = presenter;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_profileimage, parent, false);
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
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(ProfileImageData data) {
        if (getItemCount() == 0) {
            listData.add(0, data);
        } else {
            listData.add((getItemCount() - 1), data);
        }

    }

    public void removeItem(int position) {
        listData.remove(position);
    }

    public ArrayList<ProfileImageData> getListData() {
        return listData;
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private ProfileImageData data;

        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

        }

        void onBind(ProfileImageData data) {
            this.data = data;

            image.setImageURI(data.getImage());

            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if ((getItemCount() - 1) == getAdapterPosition()) {
                presenter.selectImage(true, getAdapterPosition());
            } else {
                presenter.selectImage(false, getAdapterPosition());
            }
        }


    }

}
