package simpyo.simpyo.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import simpyo.simpyo.HttpRequest.PostHttp;
import simpyo.simpyo.R;
import simpyo.simpyo.RecyclerView.Report;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.ServerURL;
import simpyo.simpyo.Setting.Setting;

public class SetReportActivity extends AppCompatActivity {

    private TextView place1,place2;
    private TextView reportView;
    private TextView doneTitle;
    private ImageView btn1, btn2;
    private TextView image_title1;
    private ImageButton backBtn;

    private RelativeLayout doneBtn;
    private TextView doneLayout;

    private int is_done = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_report);

        setView();
        setFont();
        getReportObject();
    }

    private void setView(){
        place1 = (TextView)findViewById(R.id.place1);
        place2 = (TextView)findViewById(R.id.place2);
        reportView = (TextView)findViewById(R.id.reportView);
        doneTitle = (TextView)findViewById(R.id.doneTitle);

        btn1 = (ImageView)findViewById(R.id.btn1);
        btn2 = (ImageView)findViewById(R.id.btn2);

        image_title1 = (TextView)findViewById(R.id.image_title1);

        doneBtn = (RelativeLayout)findViewById(R.id.doneBtn);
        doneLayout = (TextView)findViewById(R.id.doneLayout);
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
        fontChange.setBoldFont(doneLayout);
    }

    private void getReportObject(){
        Intent intent = getIntent();
        final Report report = (Report)intent.getSerializableExtra("ReportObject");

        is_done = report.getIsDone();

        place1.setText(report.getName());
        place2.setText(report.getAddress());
        reportView.setText(report.getReportText());

        if(is_done == 0){
            btn1.setImageResource(R.drawable.btn_in_progress_active);
            btn2.setImageResource(R.drawable.btn_completed_not);
        }else if(is_done == 1){
            btn1.setImageResource(R.drawable.btn_in_progress_not);
            btn2.setImageResource(R.drawable.btn_completed_active);
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_done == 1){
                    btn1.setImageResource(R.drawable.btn_in_progress_active);
                    btn2.setImageResource(R.drawable.btn_completed_not);
                    is_done = 0;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_done == 0){
                    btn1.setImageResource(R.drawable.btn_in_progress_not);
                    btn2.setImageResource(R.drawable.btn_completed_active);
                    is_done = 1;
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = "";
                try {
                    response = new PostHttp().execute(ServerURL.SET_DONE,String.valueOf(report.getPk()),String.valueOf(is_done),String.valueOf(Setting.SMOKE_SWITCH)).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(response !=null){
                    response = response.trim();
                    if(response.equals("200")){
                        Toast.makeText(getApplicationContext(),"검거상태 지정 완료",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

    }
}
