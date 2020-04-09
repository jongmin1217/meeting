package com.example.meeting.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meeting.activity.main.MainActivity;
import com.example.meeting.R;
import com.example.meeting.activity.signup.ProfileImageActivity;
import com.example.meeting.activity.signup.ProfileWriteActivity;
import com.example.meeting.activity.signup.SignupActivity;
import com.example.meeting.activity.signup.VerificationCodeActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView{
    @SuppressLint("StaticFieldLeak")
    public static Activity loginActivity;

    @BindView(R.id.emailInput)
    EditText email;
    @BindView(R.id.passwordInput)
    EditText password;
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    LoginPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new LoginPresenter(this);
        loginActivity = this;
    }

    public void btnLogin(View view){
        presenter.loginTry(email.getText().toString(),password.getText().toString(),checkBox.isChecked());
    }

    public void btnSignUp(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }



    @Override
    public void loginTryFail(String result,String email) {
        if(result.equals("empty")){
            Toast.makeText(getApplicationContext(), "아이디,비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if(result.equals("no user")){
            Toast.makeText(getApplicationContext(), "아이디,비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }else if(result.equals("no certification")){
            Toast.makeText(getApplicationContext(), "이메일 인증을 완료해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VerificationCodeActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }else if(result.equals("please profile info")){
            Toast.makeText(getApplicationContext(), "회원정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileWriteActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }else if(result.equals("please image")){
            Toast.makeText(getApplicationContext(), "프로필 사진을 설정해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileImageActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    }

    @Override
    public void loginTrySucces(String email) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type","login");
        startActivity(intent);
        finish();
    }

    @Override
    public Activity getActivity() {
        Activity activity = this;
        return activity;
    }
}
