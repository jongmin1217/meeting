package com.example.meeting.recyclerview;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.meeting.R;
import com.example.meeting.activity.profileedit.fragment.ProfileEditImagePresenter;
import com.example.meeting.activity.signup.ProfileImagePresenter;
import com.example.meeting.model.ProfileImageEditData;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileImageEditAdapter extends RecyclerView.Adapter<ProfileImageEditAdapter.ItemViewHolder> {
    private ArrayList<ProfileImageEditData> listData = new ArrayList<>();
    private ProfileEditImagePresenter presenter;

    public ProfileImageEditAdapter(ProfileEditImagePresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_profileimage, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageEditAdapter.ItemViewHolder holder, int position) {
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

    public void setItem(ProfileImageEditData data) {
        listData.add(data);
        Log("set");
    }

    public void addItem(ProfileImageEditData data) {
        listData.add((getItemCount() - 1), data);
        notifyDataSetChanged();
        Log("add");
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();
        Log("remove");
    }

    void Log(String type) {
        for (int i = 0; i < listData.size(); i++) {
            Log.d("zzxxcc", i + "  " + type + "  " + listData.get(i).getUrl());
        }
    }

    public ArrayList<ProfileImageEditData> getListData() {
        return listData;
    }

    public String getListDataString() {
        ArrayList<String> imagedata = new ArrayList<>();
        for(int i=0; i<listData.size(); i++){
            imagedata.add(listData.get(i).getUrl());
        }
        Gson gson = new Gson();
        String jsonPlace = gson.toJson(imagedata);
        return jsonPlace;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private ProfileImageEditData data;

        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

        }

        void onBind(ProfileImageEditData data) {
            this.data = data;

            Picasso.get().load(data.getUrl()).into(image);
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
