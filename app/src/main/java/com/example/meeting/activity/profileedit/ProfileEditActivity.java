package com.example.meeting.activity.profileedit;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.example.meeting.R;
import com.example.meeting.event.ActivityResultEvent;
import com.example.meeting.activity.profileedit.fragment.ProfileEditImageFragment;
import com.example.meeting.activity.profileedit.fragment.ProfileEditWriteFragment;
import com.example.meeting.custom.BusProvider;
import com.example.meeting.model.userData;
import com.example.meeting.model.writingUserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    private FragmentManager fragmentManager;
    private ProfileEditWriteFragment ProfileEditWriteFragment;
    private ProfileEditImageFragment ProfileEditImageFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        fragmentManager = getSupportFragmentManager();

        ProfileEditWriteFragment = new ProfileEditWriteFragment();
        ProfileEditImageFragment = new ProfileEditImageFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, ProfileEditWriteFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.write: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, ProfileEditWriteFragment).commitAllowingStateLoss();
                    return true;
                }
                case R.id.image: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, ProfileEditImageFragment).commitAllowingStateLoss();
                    return true;
                }
                default:
                    return false;
            }
        });

    }

    public void btnDone(View view){
        userData userdata = new userData(this);
        ProfileEditImageFragment.save(userdata.getEmail());
        ProfileEditWriteFragment.save(userdata.getEmail());
        Intent intent = new Intent();
        intent.putExtra("edit","done");
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    protected void onDestroy() {
        super.onDestroy();
        writingUserData writingUserData = new writingUserData(this);
        writingUserData.removeUserData();
    }


}

