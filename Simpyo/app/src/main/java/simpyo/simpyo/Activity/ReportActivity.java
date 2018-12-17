package simpyo.simpyo.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import simpyo.simpyo.HttpRequest.PostHttp;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.GetPermission;
import simpyo.simpyo.Setting.ServerURL;
import simpyo.simpyo.Setting.Setting;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_EXTRENAL = 101;
    private static final int REQUEST_ALBUM = 102;
    private static int serverResponseCode = 204;
    // 값들
    private int smokeSwitch = 0;
    private double latitude = 30.3;
    private double longtitude = 120.2;
    private String thoroughfare = "";
    private String address = "";
    // 주소 검색에 필요한 변수
    private Geocoder geocoder = null;
    private List<Address> addressList = null;
    // View
    private TextView titleView;
    // 주소 View
    private TextView place1;
    private TextView place2;
    private TextView doneLayout;
    private RelativeLayout doneBtn;
    private EditText reportEdit;
    private ImageView image1, image2, image3;
    private String imageStr1 ="", imageStr2="", imageStr3="";
    private ImageView backBtn;
    private int imageSwitch = 0;

    private boolean CAN_UPLOAD = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        setView(); // view 설정정
        setFont(); // 폰트 전체 변경
        setCustomFont(); // 부분 부분 폰트 변경
        setMarkerInfo(); // 마크의 정보를 가져온다
    }

    private void setView() {
        titleView = (TextView) findViewById(R.id.titleView);
        place1 = (TextView) findViewById(R.id.place1);
        place2 = (TextView) findViewById(R.id.place2);
        doneLayout = (TextView) findViewById(R.id.doneLayout);
        doneBtn = (RelativeLayout) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(this);

        reportEdit = (EditText) findViewById(R.id.reportEdit);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);

        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

    }

    private void setMarkerInfo() {
        Intent intent = getIntent();
        this.smokeSwitch = intent.getIntExtra("smokeSwitch", 0); // 0 아니면 1
        this.latitude = intent.getDoubleExtra("latitude", 30.3);
        this.longtitude = intent.getDoubleExtra("longtitude", 120.3);
        this.thoroughfare = intent.getStringExtra("thoroughfare");

        Log.d(Setting.Tag, "smokeSwitch : " + this.smokeSwitch + ", latitude : " + this.latitude + ", longtitude : " + longtitude);

        // 흡연시설일 경우
        if (smokeSwitch == 1) {
            titleView.setText("시설신고");
        }

        ArrayList<String> locations = searchLocation(this.latitude, this.longtitude);
        place1.setText(thoroughfare);
        place2.setText(locations.get(0));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        // 글로벌 폰트 변경
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCustomFont() {
        // 부분부분 폰트 변경 (setFont 먼저 해줘야 한다)
        FontChange fontChange = new FontChange(getApplicationContext(), getWindow().getDecorView());
        fontChange.setBoldFont(titleView);
        fontChange.setBoldFont(place1);
        fontChange.setBoldFont(doneLayout);
    }

    private ArrayList<String> searchLocation(double latitude, double longtitude) {
        if (geocoder == null) {
            geocoder = new Geocoder(this);
        }

        try {
            addressList = geocoder.getFromLocation(latitude, longtitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] splitStr = addressList.get(0).toString().split(",");
        this.address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
        String thoroughfare = addressList.get(0).getThoroughfare();

        Log.d(Setting.Tag, "주소 : " + address + ", thoroughfare : " + thoroughfare);

        ArrayList<String> returnStr = new ArrayList<String>();
        returnStr.add(address); // 주소
        returnStr.add(thoroughfare); // thorough fare

        return returnStr;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doneBtn:

                String response = "";

                Log.d(Setting.Tag, "smokeSwitch : " + smokeSwitch);

                try {
                    if (smokeSwitch == 0) {
                        response = new PostHttp().execute(ServerURL.REPORT_UPLOAD, thoroughfare, address, reportEdit.getText().toString(), String.valueOf(this.latitude), String.valueOf(this.longtitude), "", "", "", LoginModel.getId(getApplicationContext()), "0").get();
                    } else if (smokeSwitch == 1) {
                        response = new PostHttp().execute(ServerURL.REPORT_UPLOAD, thoroughfare, address, reportEdit.getText().toString(), String.valueOf(this.latitude), String.valueOf(this.longtitude), "", "", "", LoginModel.getId(getApplicationContext()), "1").get();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                Log.d(Setting.Tag, "response : " + response);

                if (response != null) {
                    response = response.trim();

                    if (response.equals("200")) {
                        Intent intent = new Intent(getApplicationContext(), ReportDoneActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    return;
                }


                break;

            case R.id.image1:
                imageSwitch = 1;
                cameraPermission();
                doGetAlbum();
                break;
            case R.id.image2:
                imageSwitch = 2;
                cameraPermission();
                doGetAlbum();
                break;
            case R.id.image3:
                imageSwitch = 3;
                cameraPermission();
                doGetAlbum();
                break;

        }
    }

    private void doGetAlbum() {
        if (!CAN_UPLOAD) {
            // 파일 읽기 권한이 없습니다.
            Toast.makeText(getApplicationContext(), "권한이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(Setting.Tag, "앨범에서 가져오기");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(Setting.Tag, "requestCode : " + requestCode);

        if (requestCode == REQUEST_ALBUM && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                switch (imageSwitch) {
                    case 1:
                        image1.setImageBitmap(bitmap);
                        imageStr1 = getImageURI(data);
                        Log.d(Setting.Tag,"imageStr1 : "+imageStr3);
                        break;
                    case 2:
                        image2.setImageBitmap(bitmap);
                        imageStr2 = getImageURI(data);
                        Log.d(Setting.Tag,"imageStr2 : "+imageStr2);
                        break;
                    case 3:
                        image3.setImageBitmap(bitmap);
                        imageStr3 = getImageURI(data);
                        Log.d(Setting.Tag,"imageStr2 : "+imageStr3);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImageURI(Intent data){
        Uri uri = data.getData();
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        Log.d(Setting.Tag, DatabaseUtils.dumpCursorToString(cursor));

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();

        return picturePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_EXTRENAL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CAN_UPLOAD = true;
                } else {
                    // 권한 없음
                    Toast.makeText(getApplicationContext(), "권한 없이 어플리케이션을 실행할 수 없습니다", Toast.LENGTH_SHORT).show();
                    CAN_UPLOAD = false;
                }
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cameraPermission() {
        int result = GetPermission.cameraPermission(ReportActivity.this);

        if (result == 1) {
            Log.d(Setting.Tag, "GPS 권한 있음");

            CAN_UPLOAD = true;
        } else if (result == PermissionChecker.PERMISSION_GRANTED) {
            Log.d(Setting.Tag, "GPS 권한 있음");

            CAN_UPLOAD = true;
        } else {
            Log.d(Setting.Tag, "GPS 권한 없음");

            GetPermission.checkCameraPermission(ReportActivity.this);
        }
    }




}
