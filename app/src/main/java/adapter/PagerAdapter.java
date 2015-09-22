package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by cong on 9/22/2015.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new fragments.QuestionFragment();
            case 1:
                return new fragments.TranscriptFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
