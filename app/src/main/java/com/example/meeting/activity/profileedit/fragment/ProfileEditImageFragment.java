package com.example.meeting.activity.profileedit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.meeting.R;
import com.example.meeting.event.ActivityResultEvent;
import com.example.meeting.custom.BusProvider;
import com.example.meeting.model.ProfileImageEditData;
import com.example.meeting.recyclerview.ProfileImageEditAdapter;
import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;
import org.json.JSONException;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class ProfileEditImageFragment extends Fragment implements ProfileEditImageView {

    @BindView(R.id.imageRecyclerView)
    RecyclerView recyclerView;

    private ProfileImageEditAdapter adapter;
    private ProfileEditImagePresenter presenter;

    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 1;

    private View view;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_edit_fragment_profileimage, container, false);
        ButterKnife.bind(this, view);
        BusProvider.getInstance().register(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new ProfileEditImagePresenter(this);

        presenter.read_cascade_file();
        init();

        try {
            presenter.getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void init() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ProfileImageEditAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getImage(ArrayList<String> url) {
        for (int i = 0; i < url.size(); i++) {
            ProfileImageEditData image = new ProfileImageEditData();
            image.setUrl(url.get(i));
            adapter.setItem(image);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addImage(String url) {
        ProfileImageEditData image = new ProfileImageEditData();
        image.setUrl(url);
        adapter.addItem(image);
    }

    @Override
    public void removeImage(int position) {
        adapter.removeItem(position);
    }



    @Override
    public Activity getActivityFragment() {
        Activity activity = getActivity();
        return activity;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    public void save(String email) {
        if (presenter != null) {
            presenter.Save(adapter, email);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case FROM_ALBUM: {
                presenter.fromAlbum(data);
                break;
            }
            case FROM_CAMERA: {
                presenter.fromCamera(data);
                break;
            }
            case Crop.REQUEST_CROP: {
                presenter.fromCrop(data);
                break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.writingChange(adapter.getListDataString());
    }
}
