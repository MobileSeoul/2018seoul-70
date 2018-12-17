package simpyo.simpyo.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import simpyo.simpyo.RecyclerView.Report;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("simpyo/pin/nosmoke/")
    Call<List<NoSmokePin>> doNoSmokePin(@Field("param1") double s_x, @Field("param2") double n_x, @Field("param3") double s_y, @Field("param4") double n_y);

    @FormUrlEncoded
    @POST("simpyo/pin/yellow/")
    Call<List<YellowPin>> doYellowPin(@Field("param1") double s_x, @Field("param2") double n_x, @Field("param3") double s_y, @Field("param4") double n_y);

    @FormUrlEncoded
    @POST("simpyo/pin/smoke/")
    Call<List<SmokePin>> doSmokePin(@Field("param1") double s_x, @Field("param2") double n_x, @Field("param3") double s_y, @Field("param4") double n_y);


    @FormUrlEncoded
    @POST("simpyo/report/get/")
    Call<List<Report>> getReports(@Field("param1") int page);

    @FormUrlEncoded
    @POST("simpyo/report/get/smoke/")
    Call<List<Report>> getReports_smoke(@Field("param1") int page);

    @FormUrlEncoded
    @POST("simpyo/report/get/my/")
    Call<List<Report>> getMyReport(@Field("param1") String email, @Field("param2") int page);

    @FormUrlEncoded
    @POST("simpyo/report/get/my/smoke/")
    Call<List<Report>> getMyReport_smoke(@Field("param1") String email, @Field("param2") int page);
}