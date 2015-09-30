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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ptpmcn.cong.mastertoeiclc.R;

/**
 * Created by cong on 9/22/2015.
 */
public class TranscriptFragment extends Fragment{
    TextView tvtranscript;
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
        tvtranscript = (TextView) rootView.findViewById(R.id.tv_transcript);
        tvtranscript.setText(getArguments().getString("transcript"));
        tvtranscript.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode

                // Here is an example MenuItem
                menu.add(0, R.id.tv_transcript, 0, "Definition").setIcon(R.drawable.ic_action_play_circle_outline);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Remove the "select all" option
                menu.removeItem(android.R.id.selectAll);
                // Remove the "cut" option
                menu.removeItem(android.R.id.cut);
                // Remove the "copy all" option
                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tv_transcript:
                        int min = 0;
                        int max = tvtranscript.getText().length();
                        if (tvtranscript.isFocused()) {
                            final int selStart = tvtranscript.getSelectionStart();
                            final int selEnd = tvtranscript.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        // Perform your definition lookup with the selected text
                        final CharSequence selectedText = tvtranscript.getText().subSequence(min, max);

                        new MaterialDialog.Builder(getActivity())
                                .title(R.string.dictionary)
                                .content(selectedText)
                                .iconRes(R.drawable.ic_action_play_circle_outline)
                                .neutralText("AAAAAAAAAAAAAA")
                                .show();
                        //Toast.makeText(getActivity(), selectedText, Toast.LENGTH_SHORT).show();
                        // Finish and close the ActionMode
                        mode.finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        return rootView;
    }

    public void setTranscript(String text){
        tvtranscript.setText(text);
    }
}
