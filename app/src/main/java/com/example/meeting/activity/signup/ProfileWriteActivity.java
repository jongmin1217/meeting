package com.example.meeting.activity.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.meeting.R;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileWriteActivity extends AppCompatActivity implements ProfileWriteView,AdapterView.OnItemSelectedListener{

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

    ProfileWritePresenter presenter;

    String email,selectArea,selectYear,selectMonth,selectDay,selectGender,selectHeight,selectForm,selectSmoke,selectDrink,
    selectPersonality,selectHobby,selectIdealType,selectNickname;

    boolean nicknaemCheck;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilewrite);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new ProfileWritePresenter(this);

        nicknaemCheck = false;

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        ArrayAdapter area_Adapter = ArrayAdapter.createFromResource(this,
                R.array.area, android.R.layout.simple_spinner_item);
        area_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area_spinner.setAdapter(area_Adapter);
        area_spinner.setOnItemSelectedListener(this);

        ArrayAdapter year_Adapter = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        year_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_Adapter);
        year_spinner.setOnItemSelectedListener(this);

        ArrayAdapter month_Adapter = ArrayAdapter.createFromResource(this,
                R.array.month, android.R.layout.simple_spinner_item);
        month_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(month_Adapter);
        month_spinner.setOnItemSelectedListener(this);

        ArrayAdapter day_Adapter = ArrayAdapter.createFromResource(this,
                R.array.day, android.R.layout.simple_spinner_item);
        day_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(day_Adapter);
        day_spinner.setOnItemSelectedListener(this);

        ArrayAdapter gender_Adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        gender_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_Adapter);
        gender_spinner.setOnItemSelectedListener(this);

        ArrayAdapter height_Adapter = ArrayAdapter.createFromResource(this,
                R.array.height, android.R.layout.simple_spinner_item);
        height_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_spinner.setAdapter(height_Adapter);
        height_spinner.setOnItemSelectedListener(this);

        ArrayAdapter form_Adapter = ArrayAdapter.createFromResource(this,
                R.array.form, android.R.layout.simple_spinner_item);
        form_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        form_spinner.setAdapter(form_Adapter);
        form_spinner.setOnItemSelectedListener(this);

        ArrayAdapter smoke_Adapter = ArrayAdapter.createFromResource(this,
                R.array.smoke, android.R.layout.simple_spinner_item);
        smoke_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smoke_spinner.setAdapter(smoke_Adapter);
        smoke_spinner.setOnItemSelectedListener(this);

        ArrayAdapter drink_Adapter = ArrayAdapter.createFromResource(this,
                R.array.drink, android.R.layout.simple_spinner_item);
        drink_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drink_spinner.setAdapter(drink_Adapter);
        drink_spinner.setOnItemSelectedListener(this);
    }

    public void btnNicknameCheck(View view){
        if(nicknameInput.getText().toString().equals("")){
            Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
        }else{
            presenter.nicknameCheckTry(nicknameInput.getText().toString());
        }
    }

    public void btnPersonality(View view){
        presenter.alertDialog("personality");
    }

    public void btnHobby(View view){
        presenter.alertDialog("hobby");
    }

    public void btnIdealType(View view){
        presenter.alertDialog("idealType");
    }

    public void btnNext(View view){
        presenter.profileWrite(email,selectArea,selectYear,selectMonth,selectDay,selectGender,selectHeight,selectForm,selectSmoke,selectDrink,
                selectPersonality,selectHobby,selectIdealType,selectNickname,nicknaemCheck);

    }

    public void btnCancel(View view){
        finish();
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
    public void noVerificationNickname() {
        Toast.makeText(this, "닉넴임을 인증해주세요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noInsert() {
        Toast.makeText(this, "항목을 전부 입력해주세요", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void profileWriteSucces(String email) {
        Intent intent = new Intent(this,ProfileImageActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        this.context = this;
        return context;
    }


}
