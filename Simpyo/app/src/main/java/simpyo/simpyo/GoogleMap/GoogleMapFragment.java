package simpyo.simpyo.GoogleMap;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;

import simpyo.simpyo.R;
import simpyo.simpyo.Setting.FontChange;

public class GoogleMapFragment extends Fragment {

    private Context context;

    private GoogleMap map;


    public GoogleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setFont() {
        FontChange fontChange = new FontChange(getActivity().getApplicationContext(), getActivity().getWindow().getDecorView());
    }
}
