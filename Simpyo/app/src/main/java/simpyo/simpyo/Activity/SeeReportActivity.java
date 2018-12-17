package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import simpyo.simpyo.R;
import simpyo.simpyo.RecyclerView.Report;
import simpyo.simpyo.Setting.FontChange;

public class SeeReportActivity extends AppCompatActivity {

    private int smokeSwitch = 0;

    private TextView place1,place2;
    private TextView reportView;
    private TextView doneTitle;
    private ImageView doneView;
    private TextView image_title1;
    private ImageButton backBtn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_report);

        setView();
        setFont();
        getReportObject();
    }

    private void setView(){
        place1 = (TextView)findViewById(R.id.place1);
        place2 = (TextView)findViewById(R.id.place2);
        reportView = (TextView)findViewById(R.id.reportView);
        doneTitle = (TextView)findViewById(R.id.doneTitle);
        doneView = (ImageView)findViewById(R.id.doneView);
        image_title1 = (TextView)findViewById(R.id.image_title1);
        backBtn = (ImageButton)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        // 글로벌 폰트 변경
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(place1);
        fontChange.setBoldFont(doneTitle);
        fontChange.setBoldFont(image_title1);
    }

    private void getReportObject(){
        Intent intent = getIntent();
        Report report = (Report)intent.getSerializableExtra("ReportObject");
        smokeSwitch = intent.getIntExtra("smokeSwitch",0);

        place1.setText(report.getName());
        place2.setText(report.getAddress());
        reportView.setText(report.getReportText());

        int is_Done = report.getIsDone();
        if(is_Done == 0){
            doneView.setImageResource(R.drawable.in_progress);
        }else{
            doneView.setImageResource(R.drawable.completed);
        }
    }
}
