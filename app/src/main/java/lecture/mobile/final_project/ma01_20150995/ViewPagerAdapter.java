package lecture.mobile.final_project.ma01_20150995;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by user on 2017-12-01.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment fragment = new Fragment();

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
        fragment = new FragmentDateList();
    }
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new FirstFragment();
            case 1:
                return new FragmentDateList();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
