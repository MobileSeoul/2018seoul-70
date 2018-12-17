package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.Setting;
import simpyo.simpyo.ViewPager.ViewPagerAdpater;

public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private Button nextBtn;
    private RelativeLayout btnLayout;

    private int PAGE = 0;
    private int MIN_PAGE = 0;
    private int MAX_PAGE = 2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        setView();
        setFont();
    }

    private void setView() {
        nextBtn = (Button) findViewById(R.id.nextBtn);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdpater viewPagerAdpater = new ViewPagerAdpater(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdpater);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0); // 메인 화면에 들어왔을때 보여주는 Fragment 아이템. 첫번째를 세팅

        btnLayout = (RelativeLayout)findViewById(R.id.btnLayout);
        btnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Setting.Tag, "Page : " + PAGE);

                if(PAGE == 2){
                    Intent intent = new Intent(TutorialActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                PAGE++;
                viewPager.setCurrentItem(PAGE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Setting.Tag, "Page : " + PAGE);

                if(PAGE == 2){
                    Intent intent = new Intent(TutorialActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                PAGE++;
                viewPager.setCurrentItem(PAGE);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        // 글로벌 폰트 변경
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(nextBtn);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        PAGE = position;
        Button nextBtn = (Button)findViewById(R.id.nextBtn);
        if(position == 2){
            nextBtn.setText("시작하기");
        }else{
            nextBtn.setText("다음");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
