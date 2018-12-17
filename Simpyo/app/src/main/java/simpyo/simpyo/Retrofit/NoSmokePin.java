package simpyo.simpyo.Retrofit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import simpyo.simpyo.R;
import simpyo.simpyo.Setting.Setting;

public class NoSmokePin {
    @SerializedName("range_detail")
    @Expose
    private String rangeDetail;
    @SerializedName("longtitude")
    @Expose
    private Double longtitude;
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("fine")
    @Expose
    private Integer fine;

    public String getRangeDetail() {
        return rangeDetail;
    }

    public void setRangeDetail(String rangeDetail) {
        this.rangeDetail = rangeDetail;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public MarkerOptions getMarkerOptions(Context context) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longtitude))
                .title("0") // title 에 0,1,2 중 하나 넣어서 핀 구분 시키기
                .snippet(getName())
                .icon(BitmapDescriptorFactory.fromBitmap(getNoSmokeIcon(context, Setting.SMALL_SIZE)));

        return markerOptions;
    }

    private Bitmap getNoSmokeIcon(Context context, int resize){
        Drawable no_smoking = context.getResources().getDrawable(R.drawable.no_smoking);
        Bitmap bitmap = ((BitmapDrawable) no_smoking).getBitmap();


        int resizeWidth = resize;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);

        return bitmap;
    }




}
