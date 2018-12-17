package simpyo.simpyo.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import simpyo.simpyo.GoogleMap.MapActivity;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.GetPermission;
import simpyo.simpyo.Setting.Setting;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView loginBtn = null;
    private TextView registerBtn = null;
    private TextView title1 = null;

    private EditText emailEdit;
    private EditText passwordEdit;

    public static LoginActivity loginActivity;

    private static final int REQUEST_CODE = 100;
    private boolean LOGIN_ACCESS = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = LoginActivity.this;

        setFont();
        setView();
        setCustomFont();

        int result = GetPermission.locationPermission(LoginActivity.this);

        if(result == 1){
            Log.d(Setting.Tag,"GPS 권한 있음");

            LOGIN_ACCESS = true;
        }else if(result == PermissionChecker.PERMISSION_GRANTED){
            Log.d(Setting.Tag,"GPS 권한 있음");

            LOGIN_ACCESS = true;
        } else{
            Log.d(Setting.Tag,"GPS 권한 없음");

            GetPermission.checkLocationPermission(LoginActivity.this);
        }

    }

    public void setView() {
        loginBtn = (TextView) findViewById(R.id.loginBtn);
        registerBtn = (TextView) findViewById(R.id.registerBtn);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCustomFont() {
        title1 = (TextView) findViewById(R.id.title1);

        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(title1);
        fontChange.setBoldFont(loginBtn);
        fontChange.setBoldFont(registerBtn);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                LoginModel loginModel = new LoginModel(getApplicationContext());
                String inputId = emailEdit.getText().toString();
                String inputPw = passwordEdit.getText().toString();

                if ("".equals(inputId) || "".equals(inputPw)) {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    loginModel.login(inputId, inputPw);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Setting.postParameter = Setting.postParameter.trim();

                if("204".equals(Setting.postParameter)){
                    Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                }else if(!LOGIN_ACCESS){
                    Toast.makeText(getApplicationContext(),"권한이 없습니다.",Toast.LENGTH_SHORT).show();

                    GetPermission.checkLocationPermission(LoginActivity.this);
                }else{
                    String login_info[] = Setting.postParameter.split("#");
                    loginModel.saveName(login_info[0]); // 이름 저장 -> 로컬로
                    loginModel.saveAdmin(Integer.parseInt(login_info[1])); // admin 저장

                    loginModel.saveId(inputId); // 아이디
                    loginModel.savePassword(inputPw); // 패스워드
                    loginModel.saveAutoLogin(true); // 자동 로그인

                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.registerBtn:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LOGIN_ACCESS = true;
                } else {
                    // 권한 없음
                    Toast.makeText(getApplicationContext(), "권한 없이 어플리케이션을 실행할 수 없습니다",Toast.LENGTH_SHORT).show();
                    LOGIN_ACCESS = false;
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
    }
}
