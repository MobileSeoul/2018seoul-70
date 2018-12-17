package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import simpyo.simpyo.GoogleMap.MapActivity;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;

public class ReportDoneActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView seeListBtn;
    private TextView homeBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_done);

        setView();
        setFont();
    }

    private void setView(){
        titleView = (TextView)findViewById(R.id.titleView);
        seeListBtn = (TextView)findViewById(R.id.seeListBtn);
        homeBtn = (TextView)findViewById(R.id.homeBtn);

        seeListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        // 글로벌 폰트 변경
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(titleView);
    }


}
