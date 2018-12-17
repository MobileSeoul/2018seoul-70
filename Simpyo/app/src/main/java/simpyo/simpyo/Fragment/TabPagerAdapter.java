package simpyo.simpyo.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import simpyo.simpyo.Setting.Setting;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    //Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position){
            case 0:
                Setting.SMOKE_SWITCH = 0;
                return new Fragment1();
            case 1:
                Setting.SMOKE_SWITCH = 1;
                return new Fragment2();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}