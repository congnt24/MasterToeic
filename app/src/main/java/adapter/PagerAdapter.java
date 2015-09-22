package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.QuestionFragment;
import fragments.TranscriptFragment;

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
                return QuestionFragment.getInstance();
            case 1:
                return TranscriptFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
