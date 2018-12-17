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

public class YellowPin {
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("longtitude")
    @Expose
    private Double longtitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public MarkerOptions getMarkerOptions(Context context) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longtitude))
                .title("2") // title 에 0,1,2 중 하나 넣어서 핀 구분 시키기
                .icon(BitmapDescriptorFactory.fromBitmap(getYellowIcon(context)));

        return markerOptions;
    }

    private Bitmap getYellowIcon(Context context){
        Drawable no_smoking = context.getResources().getDrawable(R.drawable.pin);
        Bitmap bitmap = ((BitmapDrawable) no_smoking).getBitmap();

        int resizeWidth = 25;
        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, targetHeight, false);
        return bitmap;
    }
}
