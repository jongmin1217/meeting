package com.example.meeting.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.meeting.R;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements SignupView{

    @BindView(R.id.emailInput)
    EditText email;
    @BindView(R.id.phoneNumberInput)
    EditText phoneNumber;
    @BindView(R.id.passwordInput)
    EditText password;
    @BindView(R.id.passwordConfirmInput)
    EditText passwordConfirm;

    SignupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new SignupPresenter(this);
    }

    public void btnOk(View view){
        presenter.signUpTry(email.getText().toString(),password.getText().toString(),passwordConfirm.getText().toString(),
                phoneNumber.getText().toString());
    }

    public void btnCancel(View view){
        finish();
    }

    @Override
    public void signUpFail(String err) {
        if(err.equals("Already registered")){
            Toast.makeText(getApplicationContext(), "이미 가입된 회원입니다", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void signUpSucces(String email) {
        Intent intent = new Intent(this,VerificationCodeActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }
}
