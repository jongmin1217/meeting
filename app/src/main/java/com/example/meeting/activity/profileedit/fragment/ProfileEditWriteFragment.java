package com.example.meeting.activity.profileedit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.custom.BusProvider;

import org.json.JSONException;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditWriteFragment extends Fragment implements ProfileEditWriteView, AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.area_spinner)
    Spinner area_spinner;
    @BindView(R.id.year_spinner)
    Spinner year_spinner;
    @BindView(R.id.month_spinner)
    Spinner month_spinner;
    @BindView(R.id.day_spinner)
    Spinner day_spinner;
    @BindView(R.id.gender_spinner)
    Spinner gender_spinner;
    @BindView(R.id.height_spinner)
    Spinner height_spinner;
    @BindView(R.id.form_spinner)
    Spinner form_spinner;
    @BindView(R.id.smoke_spinner)
    Spinner smoke_spinner;
    @BindView(R.id.drink_spinner)
    Spinner drink_spinner;
    @BindView(R.id.nicknameInput)
    EditText nicknameInput;
    @BindView(R.id.check_image)
    ImageView check_image;
    @BindView(R.id.personality)
    TextView personality;
    @BindView(R.id.hobby)
    TextView hobby;
    @BindView(R.id.idealType)
    TextView idealType;
    @BindView(R.id.btnHobby)
    Button btnHobby;
    @BindView(R.id.btnPersonality)
    Button btnPersonality;
    @BindView(R.id.btnIdealType)
    Button btnIdealType;
    @BindView(R.id.btnNicknameCheck)
    Button btnNicknameCheck;

    View view;
    private ProfileEditWritePresenter presenter;
    private boolean nicknaemCheck;
    private int nicknameChecknum;

    public String selectArea,selectYear,selectMonth,selectDay,selectGender,selectHeight,selectForm,selectSmoke,selectDrink,
            selectPersonality,selectHobby,selectIdealType,selectNickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_edit_fragment_profilewrite, container, false);
        ButterKnife.bind(this, view);
        BusProvider.getInstance().register(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new ProfileEditWritePresenter(this);
        Log.d("zzxxcc","앙 기모따");

        try {
            presenter.getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(nicknaemCheck){
            check_image.setImageResource(R.drawable.succes);
        }else{
            check_image.setImageResource(R.drawable.fail);
        }
        ArrayAdapter area_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.area, android.R.layout.simple_spinner_item);
        area_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area_spinner.setAdapter(area_Adapter);
        area_spinner.setSelection(presenter.getIndex(area_spinner,"area"));
        area_spinner.setOnItemSelectedListener(this);

        ArrayAdapter year_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.year, android.R.layout.simple_spinner_item);
        year_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_Adapter);
        year_spinner.setSelection(presenter.getIndex(year_spinner,"year"));
        year_spinner.setOnItemSelectedListener(this);

        ArrayAdapter month_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.month, android.R.layout.simple_spinner_item);
        month_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(month_Adapter);
        month_spinner.setSelection(presenter.getIndex(month_spinner,"month"));
        month_spinner.setOnItemSelectedListener(this);

        ArrayAdapter day_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.day, android.R.layout.simple_spinner_item);
        day_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(day_Adapter);
        day_spinner.setSelection(presenter.getIndex(day_spinner,"day"));
        day_spinner.setOnItemSelectedListener(this);

        ArrayAdapter gender_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.gender, android.R.layout.simple_spinner_item);
        gender_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_Adapter);
        gender_spinner.setSelection(presenter.getIndex(gender_spinner,"gender"));
        gender_spinner.setOnItemSelectedListener(this);

        ArrayAdapter height_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.height, android.R.layout.simple_spinner_item);
        height_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_spinner.setAdapter(height_Adapter);
        height_spinner.setSelection(presenter.getIndex(height_spinner,"height"));
        height_spinner.setOnItemSelectedListener(this);

        ArrayAdapter form_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.form, android.R.layout.simple_spinner_item);
        form_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        form_spinner.setAdapter(form_Adapter);
        form_spinner.setSelection(presenter.getIndex(form_spinner,"form"));
        form_spinner.setOnItemSelectedListener(this);

        ArrayAdapter smoke_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.smoke, android.R.layout.simple_spinner_item);
        smoke_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smoke_spinner.setAdapter(smoke_Adapter);
        smoke_spinner.setSelection(presenter.getIndex(smoke_spinner,"smoke"));
        smoke_spinner.setOnItemSelectedListener(this);

        ArrayAdapter drink_Adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.drink, android.R.layout.simple_spinner_item);
        drink_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drink_spinner.setAdapter(drink_Adapter);
        drink_spinner.setSelection(presenter.getIndex(drink_spinner,"drink"));
        drink_spinner.setOnItemSelectedListener(this);

        btnHobby.setOnClickListener(this);
        btnPersonality.setOnClickListener(this);
        btnIdealType.setOnClickListener(this);
        btnNicknameCheck.setOnClickListener(this);

        return view;
    }
    public void save(String email){
        presenter.profileWrite(email,selectArea,selectYear,selectMonth,selectDay,selectGender,selectHeight,selectForm,selectSmoke,selectDrink,
                selectPersonality,selectHobby,selectIdealType,nicknameInput.getText().toString(),nicknaemCheck,nicknameChecknum,selectNickname);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==area_spinner){
            selectArea = (String) area_spinner.getItemAtPosition(i);
        }else if(adapterView==year_spinner){
            selectYear = (String) year_spinner.getItemAtPosition(i);
        }else if(adapterView==month_spinner){
            selectMonth = (String) month_spinner.getItemAtPosition(i);
        }else if(adapterView==day_spinner){
            selectDay = (String) day_spinner.getItemAtPosition(i);
        }else if(adapterView==gender_spinner){
            selectGender = (String) gender_spinner.getItemAtPosition(i);
        }else if(adapterView==height_spinner){
            selectHeight = (String) height_spinner.getItemAtPosition(i);
        }else if(adapterView==form_spinner){
            selectForm = (String) form_spinner.getItemAtPosition(i);
        }else if(adapterView==smoke_spinner){
            selectSmoke = (String) smoke_spinner.getItemAtPosition(i);
        }else if(adapterView==drink_spinner){
            selectDrink = (String) drink_spinner.getItemAtPosition(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public Activity activity() {
        Activity activity = getActivity();
        return activity;
    }

    @Override
    public void selectDialog(String type, String text, String items) {
        if(type.equals("personality")){
            personality.setText(text);
            selectPersonality = items;
        }else if(type.equals("hobby")){
            hobby.setText(text);
            selectHobby = items;
        }else if(type.equals("idealType")){
            idealType.setText(text);
            selectIdealType = items;
        }
    }

    @Override
    public void getUserInfo(String nickname, String hobby, String personality, String idealType,String hobbyJson,String personalityJson,String idealTypeJson,boolean nicknameCheck,int nicknameChecknum) {
        nicknameInput.setText(nickname);
        this.hobby.setText(hobby);
        this.personality.setText(personality);
        this.idealType.setText(idealType);
        this.nicknaemCheck = nicknameCheck;
        this.nicknameChecknum = nicknameChecknum;
        selectHobby = hobbyJson;
        selectPersonality = personalityJson;
        selectIdealType = idealTypeJson;
        selectNickname = nickname;
    }

    @Override
    public void nicknameCheckSucces(String nickname) {
        check_image.setImageResource(R.drawable.succes);
        selectNickname = nickname;
        nicknaemCheck = true;
    }

    @Override
    public void nicknameCheckFail() {
        check_image.setImageResource(R.drawable.fail);
        nicknaemCheck = false;
    }


    @Override
    public void noInsert() {
        Toast.makeText(getContext(), "항목을 전부 입력해주세요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view==btnHobby){
            presenter.alertDialog("hobby");
        }else if(view==btnPersonality){
            presenter.alertDialog("personality");
        }else if(view==btnIdealType){
            presenter.alertDialog("idealType");
        }else if(view==btnNicknameCheck){
            if(nicknameInput.getText().toString().equals("")){
                Toast.makeText(getContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            }else{
                presenter.nicknameCheckTry(nicknameInput.getText().toString());
                nicknameChecknum++;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.writingChange(selectArea,selectYear,selectMonth,selectDay,selectGender,selectHeight,selectForm,selectSmoke,selectDrink,
                selectNickname,selectHobby,selectPersonality,selectIdealType,nicknaemCheck,nicknameChecknum);
    }
}
