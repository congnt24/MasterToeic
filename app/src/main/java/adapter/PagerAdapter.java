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
    private int part;
    private String question;
    private String transcript;
    public PagerAdapter(FragmentManager fm, int p, String q, String t) {
        super(fm);
        part = p;
        question = q;
        transcript = t;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return QuestionFragment.newInstance(part, question);
            case 1:
                return TranscriptFragment.newInstance(transcript);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
