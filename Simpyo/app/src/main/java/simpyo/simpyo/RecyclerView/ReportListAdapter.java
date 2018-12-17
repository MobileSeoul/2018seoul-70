package simpyo.simpyo.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import simpyo.simpyo.Activity.SeeReportActivity;
import simpyo.simpyo.Activity.SetReportActivity;
import simpyo.simpyo.Model.LoginModel;
import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;
import simpyo.simpyo.Setting.Setting;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {

    private Activity activity;
    private List<Report> reports = new ArrayList<Report>();
    private ViewHolder viewHolder;

    public ReportListAdapter(List<Report> reports, Activity activity) {
        this.reports = reports;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.item_report, parent, false);

        // Return a new holder instance
        viewHolder = new ViewHolder(postView);
        setFont(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Report report = reports.get(position);

        holder.nameView.setText(report.getName());
        holder.addressView.setText(report.getAddress());
        holder.timeView.setText(report.getIsShared());

        if(report.getIsDone()==0){
            holder.doneView.setImageResource(R.drawable.in_progress);
        }else if(report.getIsDone()==1){
            holder.doneView.setImageResource(R.drawable.completed);
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Setting.Tag,"smokeSwitch : "+Setting.SMOKE_SWITCH);

                int is_admin = LoginModel.getAdmin(activity.getApplicationContext());
                if(is_admin == 0){
                    Intent intent = new Intent(activity.getApplicationContext(), SeeReportActivity.class);
                    intent.putExtra("ReportObject",report);
                    intent.putExtra("smokeSwitch",Setting.SMOKE_SWITCH);
                    activity.startActivity(intent);
                }else if(is_admin == 1){
                    Intent intent = new Intent(activity.getApplicationContext(), SetReportActivity.class);
                    intent.putExtra("ReportObject",report);
                    activity.startActivity(intent);
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont(ViewHolder viewHolder) {
        // 글로벌 폰트 변경
        FontChange fontChange = new FontChange(activity.getApplicationContext(), activity.getWindow().getDecorView());
        fontChange.setBoldFont(viewHolder.nameView);
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void addReports(List<Report> reports) {
        this.reports.addAll(reports);
    }

    @Override
    public int getItemCount() {
        if (reports != null) {
            return reports.size();
        }
        return 0;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameView;
        public TextView addressView;
        public TextView timeView;
        public ImageView doneView;

        public RelativeLayout itemLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.name);
            addressView = (TextView) itemView.findViewById(R.id.address);
            timeView = (TextView) itemView.findViewById(R.id.time);
            doneView = (ImageView) itemView.findViewById(R.id.done);

            itemLayout = (RelativeLayout)itemView.findViewById(R.id.itemLayout);
        }
    }
}