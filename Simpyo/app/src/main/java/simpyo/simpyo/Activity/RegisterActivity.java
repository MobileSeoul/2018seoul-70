package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import simpyo.simpyo.GoogleMap.MapActivity;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.Setting;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private TextView registerBtn;
    private TextView loginBtn;
    private EditText emailEdit, nameEdit, birthdayEdit, passwordEdit;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setView();
        setFont();
    }

    public void setView() {
        title = (TextView)findViewById(R.id.title);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        birthdayEdit = (EditText) findViewById(R.id.birthdayEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);

        registerBtn = (TextView) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        loginBtn = (TextView)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                String inputName = nameEdit.getText().toString();
                String inputPw = passwordEdit.getText().toString();
                String inputEmail = emailEdit.getText().toString();
                String birthDay = birthdayEdit.getText().toString();

                if("".equals(inputName)||"".equals(inputPw)||"".equals(inputEmail)||"".equals(birthDay)){
                    Toast.makeText(getApplicationContext(),"빈칸을 모두 채워주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginModel loginModel = new LoginModel(getApplicationContext());
                try {
                    loginModel.register(inputName,inputPw, inputEmail, birthDay);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Setting.postParameter = Setting.postParameter.trim();

                if("200".equals(Setting.postParameter)){
                    loginModel.saveId(inputName);

                    Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                }else if("204".equals(Setting.postParameter)){
                    Toast.makeText(getApplicationContext(),"회원가입 실패, 중복된 이메일이 있습니다",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.loginBtn:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}
