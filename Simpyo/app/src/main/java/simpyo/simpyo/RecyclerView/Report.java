package simpyo.simpyo.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Report implements Serializable {

    @SerializedName("report_text")
    @Expose
    private String reportText = "";
    @SerializedName("address")
    @Expose
    private String address = "";
    @SerializedName("image1")
    @Expose
    private String image1 = "";
    @SerializedName("latitude")
    @Expose
    private double latitude = 30;
    @SerializedName("is_done")
    @Expose
    private int isDone = 0;
    @SerializedName("image3")
    @Expose
    private String image3 = "";
    @SerializedName("longtitude")
    @Expose
    private double longtitude = 120;
    @SerializedName("is_shared")
    @Expose
    private String isShared = "";
    @SerializedName("image2")
    @Expose
    private String image2 = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("pk")
    @Expose
    private int pk = 0;

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }


    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public void setLongtitude(Integer longtitude) {
        this.longtitude = longtitude;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }
}