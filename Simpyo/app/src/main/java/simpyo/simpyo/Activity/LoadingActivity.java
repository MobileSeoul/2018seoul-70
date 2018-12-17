package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import simpyo.simpyo.GoogleMap.MapActivity;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.Setting;

public class LoadingActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        setFont();
        moveActivity();


        // Intent intent = new Intent(LoadingActivity.this, MapActivity.class);
        // startActivity(intent);


    }

    private void startLoading(final boolean autoLogin) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(autoLogin){
                    moveMap();
                }else{
                    moveTutorial();
                }
            }
        }, Setting.loadingSecond); // 로딩 시간 세팅 - milliSecond 로 설정된다.
    }

    private void moveActivity() {
        LoginModel loginModel = new LoginModel(getApplicationContext());
        boolean autoLogin = loginModel.getAutoLogin(); // 자동 로그인이 되어있나? 를 확인하고 불러옴.

        startLoading(autoLogin);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
    }



    private void moveTutorial(){
        Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveMap(){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
        finish();
    }
}
