package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ptpmcn.cong.dictionary.Dictionary;
import com.ptpmcn.cong.mastertoeiclc.IMenuHandler;
import com.ptpmcn.cong.mastertoeiclc.R;
import com.ptpmcn.cong.mastertoeiclc.SelectableTextView;


/**
 * Created by cong on 9/22/2015.
 */
public class TranscriptFragment extends Fragment implements IMenuHandler {
    SelectableTextView tvtranscript;
    private static TranscriptFragment instance;

    /**
     * Create new instance of Fragment class for call in PagerAdapter
     * @param transcript: Using to setText for transcription Text View
     * @return TranscriptFragment
     */
    public static TranscriptFragment newInstance(String transcript){
        instance = new TranscriptFragment();
        Bundle args = new Bundle();
        args.putString("transcript", transcript);
        instance.setArguments(args);
        return instance;
    }

    /**
     * Singleton Partern to get only one object of this fragment and change value of components inside this fragment
     * @return TranscriptFragment
     */
    public static TranscriptFragment getInstance(){
        if (instance == null)
            instance = new TranscriptFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transcript, container, false);
        tvtranscript = (SelectableTextView) rootView.findViewById(R.id.tv_transcript);
        tvtranscript.setText(getArguments().getString("transcript"));
        tvtranscript.init(this);
        return rootView;
    }

    public void setTranscript(String text){
        tvtranscript.setText(text);
    }

    @Override
    public void onCompleteSelectTextView(String str) {
        Dictionary.getInstance().showDialogAndAddToHistory(getActivity(), str);
    }
}
