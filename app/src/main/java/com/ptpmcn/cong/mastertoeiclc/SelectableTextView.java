package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by cong on 11/8/2015.
 */
public class SelectableTextView extends TextView {
    private static final int TRANSLATEID = 1;
    private IMenuHandler iMenuHandler;

    public SelectableTextView(Context context) {
        super(context);
        setTextIsSelectable(true);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextIsSelectable(true);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextIsSelectable(true);
    }

    public void init(IMenuHandler iMenuHandler) {
        this.iMenuHandler = iMenuHandler;
        setCustomSelectionActionModeCallback(new MarkTextSelectionActionModeCallback());
    }

    private class MarkTextSelectionActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add(0, getId(), 0, "Dịch").setIcon(R.drawable.ic_translate_black_48dp);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.removeItem(android.R.id.selectAll);
            menu.removeItem(android.R.id.cut);
            menu.removeItem(android.R.id.copy);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == getId()) {
                int min = 0;
                int max = getText().length();
                if (isFocused()) {
                    final int selStart = getSelectionStart();
                    final int selEnd = getSelectionEnd();

                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));
                }
                // Perform your definition lookup with the selected text
                final CharSequence selectedText = getText().subSequence(min, max);
                iMenuHandler.onCompleteSelectTextView(selectedText.toString());
                //Toast.makeText(getActivity(), selectedText, Toast.LENGTH_SHORT).show();
                // Finish and close the ActionMode
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
