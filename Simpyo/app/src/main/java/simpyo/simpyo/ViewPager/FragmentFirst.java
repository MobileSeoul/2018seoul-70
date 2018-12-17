package simpyo.simpyo.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import simpyo.simpyo.R;

public class FragmentFirst extends Fragment {
    private RelativeLayout firstRelative;

    private TextView title1, title2;
    private TextView text1,text2,text3,text4,text5;

    public FragmentFirst() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstRelative = (RelativeLayout) inflater.inflate(R.layout.viewpager_first, container, false);


        return firstRelative;
    }


}