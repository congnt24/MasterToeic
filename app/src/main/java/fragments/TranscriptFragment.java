package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ptpmcn.cong.mastertoeiclc.R;

/**
 * Created by cong on 9/22/2015.
 */
public class TranscriptFragment extends Fragment{
    TextView tvtranscript;
    private static TranscriptFragment instance;

    public static TranscriptFragment getInstance(){
        if(instance == null)
            instance = new TranscriptFragment();
        return instance;
    }
    private TranscriptFragment(){};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transcript, container, false);
        tvtranscript = (TextView) rootView.findViewById(R.id.tv_transcript);
        return rootView;
    }

    public void setTranscript(String text){
        //tvtranscript.setText(text);
    }
}
