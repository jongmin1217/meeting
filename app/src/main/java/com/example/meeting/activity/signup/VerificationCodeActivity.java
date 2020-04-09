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

public class VerificationCodeActivity extends AppCompatActivity implements VerificationCodeView{

    @BindView(R.id.certificationCodeInput)
    EditText certificationCode;

    VerificationCodePresenter presenter;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationcode);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        presenter = new VerificationCodePresenter(this);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
    }

    public void btnOk(View view){
        presenter.certificationCodeTry(email,certificationCode.getText().toString());
    }

    public void btnCancel(View view){
        finish();
    }

    @Override
    public void cerificationCodeSucces(String result) {
        Toast.makeText(this, "회원정보를 입력해주세요", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ProfileWriteActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    @Override
    public void cerificationCodeFail(String result) {
        Toast.makeText(this, "인증번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
    }
}
