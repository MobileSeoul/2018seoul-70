package simpyo.simpyo.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdpater extends FragmentStatePagerAdapter {

    private FragmentFirst fragmentFirst;
    private FragmentSecond fragmentSecond;
    private FragmentThird fragmentThird;

    public ViewPagerAdpater(android.support.v4.app.FragmentManager fragmentManager){
        super(fragmentManager);

        fragmentFirst = new FragmentFirst();
        fragmentSecond = new FragmentSecond();
        fragmentThird = new FragmentThird();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return this.fragmentFirst;
            case 1:
                return this.fragmentSecond;
            case 2:
                return this.fragmentThird;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3; // 페이지 갯수
    }
}