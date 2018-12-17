package simpyo.simpyo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simpyo.simpyo.R;
import simpyo.simpyo.RecyclerView.Report;
import simpyo.simpyo.RecyclerView.ReportListAdapter;
import simpyo.simpyo.Retrofit.NetRetrofit;
import simpyo.simpyo.Setting.Setting;

public class Fragment2 extends Fragment {

    private static final int page_count = 20;
    private List<Report> reportArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportListAdapter adapter;
    private int page = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_fragment1, container, false);

        // 기본 설정
        recyclerView = (RecyclerView) relativeLayout.findViewById(R.id.recyclerView);
        adapter = new ReportListAdapter(setDefaultReport(), getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 이제 가져오자!
        getReportList_START();

        swipeRefreshLayout = (SwipeRefreshLayout)relativeLayout.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReportList_START();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(Setting.Tag, "End of list");

                    getReportList();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return relativeLayout;
    }

    private void getReportList() {
        Call<List<Report>> call = NetRetrofit.getInstance().getService().getReports_smoke(page);
        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                List<Report> reportList = response.body();
                reportArrayList = reportList;

                adapter.addReports(reportArrayList);
                adapter.notifyItemMoved(page * page_count, (page + 1) * page_count);

                page++;
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {

            }
        });
    }

    private void getReportList_START() {
        Call<List<Report>> call = NetRetrofit.getInstance().getService().getReports_smoke(0);
        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                List<Report> reportList = response.body();
                reportArrayList = reportList;

                for(Report report : reportList){
                    Log.d(Setting.Tag,"report Name : "+report.getName());
                }

                adapter.setReports(reportArrayList);
                adapter.notifyDataSetChanged();

                page++;
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {

            }
        });
    }

    private List<Report> setDefaultReport(){
        List<Report> report = new ArrayList<>();
        report.add(new Report());
        report.add(new Report());

        return report;
    }
}