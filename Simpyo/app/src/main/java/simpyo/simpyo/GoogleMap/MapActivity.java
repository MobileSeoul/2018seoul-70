package simpyo.simpyo.GoogleMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simpyo.simpyo.Activity.LoginActivity;
import simpyo.simpyo.Activity.ReportActivity;
import simpyo.simpyo.Activity.ReportListActivity;
import simpyo.simpyo.Activity.ReportMyListActivity;
import simpyo.simpyo.HttpRequest.PostHttp;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Retrofit.NetRetrofit;
import simpyo.simpyo.Retrofit.NoSmokePin;
import simpyo.simpyo.Retrofit.SmokePin;
import simpyo.simpyo.Retrofit.YellowPin;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.ServerURL;
import simpyo.simpyo.Setting.Setting;
import simpyo.simpyo.Setting.ThreadSleep;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private final int CameraDegree = 16;
    private DrawerLayout drawer; // 드로어
    private NavigationView navigationView; // 그 내부 뷰
    private View headerView; // 최상뷰
    private RelativeLayout headerLayout; // 최상뷰의 RelativeLayout
    private RelativeLayout menuLayout; // 메뉴뷰의 RelativeLayout
    private ImageButton drawerBtn;
    // 드로어 아이템들
    private TextView myReportMenu, myInfo, helpMenu, logoutMenu;
    private TextView nameView;
    // 구글 맵
    private GoogleMap map = null;
    // 현재 위치
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mCurrentLocation = null;
    private Marker nowMarker = null;

    // 가져온 Marker 들 가져오기
    private ArrayList<Marker> no_smokes = new ArrayList<Marker>();
    private ArrayList<Marker> smokes = new ArrayList<Marker>();
    private ArrayList<Marker> yellow_pins = new ArrayList<Marker>();

    // 흡연스팟 체크
    private boolean yellowCheck = false;
    private boolean yellowClicked = false;
    private ImageView smoking_check;
    private ImageView smoking_check_done;
    private RelativeLayout smoking_check_done_view;

    // 폰트
    private TextView title1;
    private TextView explain2;

    // report Layout
    private RelativeLayout reportLayout;
    private RelativeLayout reportBtn;
    private TextView zone;
    private TextView place1;
    private TextView place2;
    private TextView report1, report2;

    // 장소 검색
    private Geocoder geocoder = null;
    private List<Address> addressList = null;

    // marker 클릭
    private Marker clickedMarker = null;
    private boolean clicked = false; // 클릭한적이 없음이라고 초기화
    private String thoroughfare = "";

    // 금연구역, 흡연시설, 전체보기
    private ImageView no_smokingBtn, smokingBtn, view_allBtn;

    // 관리자
    private int is_admin = 0;

    private static Bitmap getSelected(Context context) {
        Drawable pin_selected = context.getResources().getDrawable(R.drawable.pin_select);
        Bitmap bitmap = ((BitmapDrawable) pin_selected).getBitmap();

        int resizeWidth = Setting.SMALL_SIZE;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        Bitmap result = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 스크린 온 시킴
        setContentView(R.layout.activity_navigation);

        setFont();
        setView();
        setCustomFont();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 들어올 때마다 admin 인지 아닌지 체크
        try {
            setViewAdmin();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        headerLayout = (RelativeLayout) headerView.findViewById(R.id.header_layout);
        menuLayout = (RelativeLayout) navigationView.findViewById(R.id.menu_layout);

        // 드로어 아이템
        myReportMenu = (TextView) navigationView.findViewById(R.id.myReportMenu);
        myInfo = (TextView) navigationView.findViewById(R.id.myInfo);
        helpMenu = (TextView) navigationView.findViewById(R.id.helpMenu);
        logoutMenu = (TextView) navigationView.findViewById(R.id.logoutMenu);
        nameView = (TextView) headerView.findViewById(R.id.nameView);
        nameView.setText(LoginModel.getName(getApplicationContext())); // 이름 설정
        myReportMenu.setOnClickListener(this);
        myInfo.setOnClickListener(this);
        helpMenu.setOnClickListener(this);
        logoutMenu.setOnClickListener(this);

        smoking_check = (ImageView) findViewById(R.id.smoking_check);
        smoking_check.setOnClickListener(this);
        smoking_check_done = (ImageView) findViewById(R.id.smoking_check_done);
        smoking_check_done.setOnClickListener(this);
        smoking_check_done_view = (RelativeLayout) findViewById(R.id.smoking_check_done_view);
        smoking_check_done.bringToFront(); // 맨 앞으로

        // 폰트
        title1 = (TextView) findViewById(R.id.title1);
        explain2 = (TextView) findViewById(R.id.explain2);

        // report Layout
        reportLayout = (RelativeLayout) findViewById(R.id.reportLayout);
        zone = (TextView) findViewById(R.id.zone);
        place1 = (TextView) findViewById(R.id.place1);
        place2 = (TextView) findViewById(R.id.place2);
        report1 = (TextView) findViewById(R.id.report1);
        report2 = (TextView) findViewById(R.id.report2);
        reportBtn = (RelativeLayout) findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(this);

        // 금연구역, 흡연시설, 전체보기 버튼
        no_smokingBtn = (ImageView) findViewById(R.id.no_smoking_btn);
        smokingBtn = (ImageView) findViewById(R.id.smoking_btn);
        view_allBtn = (ImageView) findViewById(R.id.view_all_btn);

        no_smokingBtn.setOnClickListener(this);
        smokingBtn.setOnClickListener(this);
        view_allBtn.setOnClickListener(this);

        drawerBtn = (ImageButton) findViewById(R.id.drawerBtn);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // 현재 위치 버튼 보여지기, 가능하게 하기
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        // 카메라 이동
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.5709973, 126.9780009), CameraDegree);
        map.moveCamera(cameraUpdate);

        // 현재위치 버튼 클릭했을때
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.d(Setting.Tag, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");

                // 현재 위치 받아오기
                getCurrentLocation();
                // 카메라 이동동
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), CameraDegree);
                map.moveCamera(cameraUpdate);

                return true;
            }
        });


        // 맵을 클릭할때 -> 다른핀이 클릭되어있으면 내려가고, 아니면 노란핀이 찍힌다.
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(Setting.Tag, "onMapClick :" + latLng.latitude + ", " + latLng.longitude);

                if (nowMarker != null) {
                    nowMarker.remove();
                    nowMarker = null;
                }
                if (yellowCheck) {
                    Log.d(Setting.Tag, "yellowCheck : " + yellowCheck);

                    yellowClicked = true;
                    nowMarker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("3")
                            .icon(BitmapDescriptorFactory.fromBitmap(getSelected(getApplicationContext()))));
                }

                // 맵 다른부분을 클릭했을때 내려가게끔 하는 코드 -> 수정이 필요하다
                if (clicked && !yellowCheck) {
                    Log.d(Setting.Tag, "clicked : " + clicked + ", yellowCheck : " + yellowCheck);

                    int clickedSwitch = Integer.parseInt(clickedMarker.getTitle());
                    if (clickedSwitch == 0) {
                        clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(Setting.SMALL_SIZE)));
                    } else if (clickedSwitch == 1) {
                        clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getSmokeIcon(Setting.SMALL_SIZE)));
                    }

                    Setting.transAnimation(false, reportLayout);

                    clicked = false; // 취소
                    clickedMarker = null; // 초기화

                    smoking_check.setVisibility(View.VISIBLE); // 노랑핀 보이게
                }

            }
        });

        // 카메라의 움직임이 멈출 때
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.d(Setting.Tag, "핀을 가져온다");
                CameraUpdateFactory.zoomTo(16);

                new ThreadSleep().execute(2000);

                LatLngBounds startBounds = getBounds();

                double southwest_x = startBounds.southwest.latitude;
                double northeast_x = startBounds.northeast.latitude;
                double southwest_y = startBounds.southwest.longitude;
                double northeast_y = startBounds.northeast.longitude;

                Log.d(Setting.Tag, "s_x : " + southwest_x + ", n_x : " + northeast_x + ", s_y : " + southwest_y + ", n_y : " + northeast_y);

                getNoSmokePin(southwest_x, northeast_x, southwest_y, northeast_y);
                getSmokePin(southwest_x, northeast_x, southwest_y, northeast_y);
                getYellowPin(southwest_x, northeast_x, southwest_y, northeast_y);
            }
        });


        map.setOnMarkerClickListener(this); // 마커 클릭 리스너

        LatLngBounds startBounds = getBounds();

        double southwest_x = startBounds.southwest.latitude;
        double northeast_x = startBounds.northeast.latitude;
        double southwest_y = startBounds.southwest.longitude;
        double northeast_y = startBounds.northeast.longitude;

        Log.d(Setting.Tag, "s_x : " + southwest_x + ", n_x : " + northeast_x + ", s_y : " + southwest_y + ", n_y : " + northeast_y);

        getNoSmokePin(southwest_x, northeast_x, southwest_y, northeast_y);
    }

    private void getNoSmokePin(double southwest_x, double northeast_x, double southwest_y, double northeast_y) {
        Log.d(Setting.Tag, "금연핀을 가져옵니다");

        Call<List<NoSmokePin>> call = NetRetrofit.getInstance().getService().doNoSmokePin(southwest_x, northeast_x, southwest_y, northeast_y);
        call.enqueue(new Callback<List<NoSmokePin>>() {
            @Override
            public void onResponse(Call<List<NoSmokePin>> call, Response<List<NoSmokePin>> response) {
                List<NoSmokePin> pinList = response.body();
                for (NoSmokePin pin : pinList) {
                    Log.d(Setting.Tag, "NO_SMOKE_Pin Name : " + pin.getName() + ", latitude : " + pin.getLatitude() + ", longtitude : " + pin.getLongtitude());

                    Marker marker = map.addMarker(pin.getMarkerOptions(getApplicationContext()));

                    boolean overlap = searchPin(0, marker.getTitle());
                    if (!overlap) {
                        marker.remove();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<NoSmokePin>> call, Throwable t) {

            }
        });
    }

    private void getSmokePin(double southwest_x, double northeast_x, double southwest_y, double northeast_y) {
        Log.d(Setting.Tag, "흡연핀을 가져옵니다");

        Call<List<SmokePin>> call = NetRetrofit.getInstance().getService().doSmokePin(southwest_x, northeast_x, southwest_y, northeast_y);
        call.enqueue(new Callback<List<SmokePin>>() {
            @Override
            public void onResponse(Call<List<SmokePin>> call, Response<List<SmokePin>> response) {
                List<SmokePin> pinList = response.body();
                for (SmokePin pin : pinList) {
                    Log.d(Setting.Tag, "SMOKE_Pin Name : " + pin.getName() + ", latitude : " + pin.getLatitude() + ", longtitude : " + pin.getLongtitude());

                    Marker marker = map.addMarker(pin.getMarkerOptions(getApplicationContext()));
                    boolean overlap = searchPin(1, marker.getTitle());
                    if (!overlap) {
                        marker.remove();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<SmokePin>> call, Throwable t) {

            }
        });
    }

    private void getYellowPin(double southwest_x, double northeast_x, double southwest_y, double northeast_y) {
        Log.d(Setting.Tag, "노랑핀을 가져옵니다");

        Call<List<YellowPin>> call = NetRetrofit.getInstance().getService().doYellowPin(southwest_x, northeast_x, southwest_y, northeast_y);
        call.enqueue(new Callback<List<YellowPin>>() {
            @Override
            public void onResponse(Call<List<YellowPin>> call, Response<List<YellowPin>> response) {
                List<YellowPin> pinList = response.body();
                for (YellowPin pin : pinList) {
                    Log.d(Setting.Tag, "Yellow PIN, latitude : " + pin.getLatitude() + ", longtitude : " + pin.getLongtitude());

                    Marker marker = map.addMarker(pin.getMarkerOptions(getApplicationContext()));
                    boolean overlap = searchPin(2, marker.getTitle());
                    if (!overlap) {
                        marker.remove();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<YellowPin>> call, Throwable t) {

            }
        });
    }

    // 현재 보이는 영역을 가져온다 -> 경도, 위도
    public LatLngBounds getBounds() {
        Projection projection = map.getProjection();
        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;

        return bounds;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        OnCompleteListener<Location> completeListener = new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mCurrentLocation = task.getResult();
                    Log.d(Setting.Tag, "현재 위치 :" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
                } else {
                    Log.d(Setting.Tag, "getCurrentPosition Err", task.getException());
                }
            }
        };

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, completeListener);
    }

    @Override
    public boolean onMarkerClick(Marker marker) { // 마커 클릭했을때!
        int smokeSwitch = Integer.parseInt(marker.getTitle()); // 금연존, 흡연존, 노랑이

        // 노랑핀 클릭했을때
        if (smokeSwitch == 2 || smokeSwitch == 3) {
            return true;
        }

        if (yellowCheck) {
            return true;
        }

        setMarkerReportView(marker);
        setReportLayout(smokeSwitch, marker); // reportLayout view 설정
        setMarkerView(marker, smokeSwitch); // marker 설정

        return true;
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
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
        String thoroughfare = addressList.get(0).getThoroughfare();

        Log.d(Setting.Tag, "주소 : " + address + ", thoroughfare : " + thoroughfare);

        ArrayList<String> returnStr = new ArrayList<String>();
        returnStr.add(address); // 주소
        returnStr.add(thoroughfare); // thorough fare

        return returnStr;
    }

    private void setMarkerView(Marker marker, int smokeSwitch) {
        ArrayList<String> address = searchLocation(marker.getPosition().latitude, marker.getPosition().longitude);
        // place1.setText(address.get(1)); // thorough fare
        place2.setText(address.get(0)); // 주소

        yellowCheck = false; // 노랑색 핀 체크 불가
        if (nowMarker != null) {
            nowMarker.remove();
            nowMarker = null;
        }

        Log.d(Setting.Tag, "clicked : " + clicked);
        if (clicked) {
            // 클릭한적이 있을때
            // 그전에 클릭했던 마커 아이콘의 크기를 줄이고, 현재 클릭한 마커 아이콘의 크기를 늘린다
            // clicked 는 그대로
            if (clickedMarker != null) {
                int clickedSwitch = Integer.parseInt(clickedMarker.getTitle());
                Log.d(Setting.Tag, "clickedSwitch : " + clickedSwitch);

                // 그전의 마커 아이콘 크기 small
                if (clickedSwitch == 0) {
                    clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(Setting.SMALL_SIZE)));
                } else if (clickedSwitch == 1) {
                    clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getSmokeIcon(Setting.SMALL_SIZE)));
                }
            }

            // 아이콘 크게
            if (smokeSwitch == 0) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(Setting.BIG_SIZE)));
            } else if (smokeSwitch == 1) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getSmokeIcon(Setting.BIG_SIZE)));
            }

            // 초기화 후 -> 현재 클릭한 marker 로 세팅
            clickedMarker = null;
            clickedMarker = marker;

            clicked = true; // 클릭한적이 있음으로 둠
        } else {
            // 클릭한적이 없을때
            // 현재 클릭한 마커 아이콘의 크기를 늘린다.

            // 아이콘 크게
            if (smokeSwitch == 0) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(Setting.BIG_SIZE)));
            } else if (smokeSwitch == 1) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getSmokeIcon(Setting.BIG_SIZE)));
            }

            clickedMarker = null;
            clickedMarker = marker;

            clicked = true;
        }

    }

    private void setReportLayout(int smokeSwitch, Marker marker) {
        switch (smokeSwitch) {
            case 0: // 금연존
                zone.setText("금연존");
                zone.setTextColor(Color.parseColor("#5480e8"));
                report1.setText("금연구역 ");
                report2.setText("간편신고");
                place1.setText(marker.getSnippet());

                thoroughfare = marker.getSnippet();
                break;
            case 1: // 흡연존
                zone.setText("흡연존");
                zone.setTextColor(Color.parseColor("#53595f"));
                report1.setText("흡연시설 ");
                report2.setText("불편신고");
                place1.setText(marker.getSnippet());

                thoroughfare = marker.getSnippet();
                break;
            case 2: // 노랑핀
                break;
        }
    }

    private void setMarkerReportView(Marker marker) {
        // 뷰 이동
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(marker.getPosition());
        map.animateCamera(cameraUpdate);

        // 뷰 처리 - 애니메이션
        if (reportLayout.getVisibility() == View.GONE) {
            Setting.transAnimation(true, reportLayout);
        }
        smoking_check.setVisibility(View.GONE);
        smoking_check_done_view.setVisibility(View.GONE);

        if (nowMarker != null) {
            nowMarker.remove(); // pin_selected 제거
            nowMarker = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCurrentLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smoking_check: // 흡연스팟 체크 클릭
                smoking_check.setVisibility(View.GONE);
                Setting.transAnimation(true, smoking_check_done_view);

                yellowCheck = true;
                break;

            case R.id.smoking_check_done: // 선택 완료 클릭
                if (yellowClicked) {
                    LatLng pinPosition = nowMarker.getPosition();
                    double latitude = pinPosition.latitude;
                    double longtitude = pinPosition.longitude;

                    new PostHttp().execute(ServerURL.UPLOAD_YELLOW, String.valueOf(latitude), String.valueOf(longtitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitude, longtitude))
                            .title("2") // title 에 0,1,2 중 하나 넣어서 핀 구분 시키기
                            .icon(BitmapDescriptorFactory.fromBitmap(getYellowIcon(getApplicationContext())));

                    Marker yellow_pin = map.addMarker(markerOptions);
                    yellow_pins.add(yellow_pin);

                    yellowClicked = false;
                }


                if (nowMarker != null) {
                    nowMarker.remove();
                    nowMarker = null;
                }
                yellowCheck = false;

                Setting.transAnimation(true, smoking_check);
                smoking_check_done_view.setVisibility(View.GONE);
                break;

            case R.id.no_smoking_btn:
                for (Marker no_smoke : no_smokes) {
                    no_smoke.setVisible(true);
                }
                for (Marker smoke : smokes) {
                    smoke.setVisible(false);
                }
                break;
            case R.id.smoking_btn:
                for (Marker no_smoke : no_smokes) {
                    no_smoke.setVisible(false);
                }
                for (Marker smoke : smokes) {
                    smoke.setVisible(true);
                }
                break;
            case R.id.view_all_btn:
                for (Marker no_smoke : no_smokes) {
                    no_smoke.setVisible(true);
                }
                for (Marker smoke : smokes) {
                    smoke.setVisible(true);
                }
                break;
            case R.id.reportBtn:
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                intent.putExtra("smokeSwitch", Integer.parseInt(clickedMarker.getTitle()));
                intent.putExtra("thoroughfare", thoroughfare);
                intent.putExtra("latitude", clickedMarker.getPosition().latitude);
                intent.putExtra("longtitude", clickedMarker.getPosition().longitude);
                startActivity(intent);
                break;

            case R.id.myReportMenu:
                if (this.is_admin == 1) {
                    Intent intent2 = new Intent(getApplicationContext(), ReportListActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent3 = new Intent(getApplicationContext(), ReportMyListActivity.class);
                    startActivity(intent3);
                }

                break;
            case R.id.myInfo:
                break;
            case R.id.helpMenu:
                break;
            case R.id.logoutMenu:
                LoginModel.logout(MapActivity.this);
                Intent intent3 = new Intent(MapActivity.this, LoginActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
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
        fontChange.setBoldFont(title1);
        fontChange.setBoldFont(explain2);
        fontChange.setExtraBoldFont(zone);
        fontChange.setBoldFont(place1);
        fontChange.setBoldFont(report1);
        fontChange.setBoldFont(report2);
    }

    @Override
    public void onBackPressed() {
        // Drawerlayout 에 대해서
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            // report Layout 에 대해서
            if (reportLayout.getVisibility() == View.VISIBLE) {
                Setting.transAnimation(false, reportLayout);
                smoking_check.setVisibility(View.VISIBLE);
                if (clicked) {
                    if (Integer.parseInt(clickedMarker.getTitle()) == 0) {
                        clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(Setting.SMALL_SIZE)));

                    } else if (Integer.parseInt(clickedMarker.getTitle()) == 1) {
                        clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getSmokeIcon(Setting.SMALL_SIZE)));
                    }
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    // 필요 없음
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private Bitmap getSmokeIcon(int resize) {
        Drawable no_smoking = getResources().getDrawable(R.drawable.smoking);
        Bitmap bitmap = ((BitmapDrawable) no_smoking).getBitmap();

        int resizeWidth = resize;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);
        return bitmap;
    }

    private Bitmap getNoSmokeIcon(int resize) {
        Drawable no_smoking = getResources().getDrawable(R.drawable.no_smoking);
        Bitmap bitmap = ((BitmapDrawable) no_smoking).getBitmap();

        int resizeWidth = resize;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);
        return bitmap;
    }

    private void setViewAdmin() throws ExecutionException, InterruptedException {
        String is_admin = LoginModel.checkAdmin(getApplicationContext());
        is_admin = is_admin.trim();

        if (is_admin.equals("1")) {
            // 관리자!
            LoginModel loginModel = new LoginModel(getApplicationContext());

            Log.d(Setting.Tag, "관리자님 반갑습니다");

            loginModel.saveAdmin(1);

            this.is_admin = 1;
            myReportMenu.setText("신고 내역");

        } else {
            // 일반
            LoginModel loginModel = new LoginModel(getApplicationContext());

            Log.d(Setting.Tag, "일반");

            loginModel.saveAdmin(0);

            this.is_admin = 0;
            myReportMenu.setText("My 신고 내역");
        }
    }

    private Bitmap getYellowIcon(Context context) {
        Drawable no_smoking = context.getResources().getDrawable(R.drawable.pin);
        Bitmap bitmap = ((BitmapDrawable) no_smoking).getBitmap();

        int resizeWidth = 25;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);
        return bitmap;
    }

    private boolean searchPin(int smokeSwitch, String title) {
        switch (smokeSwitch) {
            case 0:
                for (Marker marker : no_smokes) {
                    if (marker.getTitle().equals(title)) {
                        Log.d(Setting.Tag, "no_smokes : 중복됨");
                        return false;
                    }
                }

                return true;
            case 1:
                for (Marker marker : smokes) {
                    if (marker.getTitle().equals(title)) {
                        Log.d(Setting.Tag, "smokes : 중복됨");
                        return false;
                    }
                }

                return true;
            case 2:
                for (Marker marker : yellow_pins) {
                    if (marker.getTitle().equals(title)) {
                        Log.d(Setting.Tag, "smokes : 중복됨");
                        return false;
                    }
                }

                return true;
            default:
                return true;
        }
    }


}
