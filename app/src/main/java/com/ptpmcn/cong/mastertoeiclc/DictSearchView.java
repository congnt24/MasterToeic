package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ptpmcn.cong.dictionary.Dictionary;

import java.util.List;

import apv.congnt24.data.sqlite.SQLiteFactory;
import apv.congnt24.data.sqlite.SQLiteHelper;

/**
 * Created by cong on 11/7/2015.
 */
public class DictSearchView extends SearchView implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    public static final String CITY_NAME = "word";
    private CursorAdapter mAdapter;
    private EditText ed_searchView;
    private Activity activity;
    private SQLiteHelper dictSQL;

    public DictSearchView(Context context) {
        super(context);
    }

    public DictSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity) {
        this.activity = activity;
        dictSQL = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(activity, "dict.db");
        setQueryHint(activity.getString(R.string.search_hint));
        ed_searchView = ((EditText) findViewById(android.support.v7.appcompat.R.id.search_src_text));
        ed_searchView.setHintTextColor(getResources().getColor(R.color.my_primary_light));
        setOnQueryTextListener(this);
        setOnSuggestionListener(this);
        loadHints();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        List<String> historyList = dictSQL.getLikeWord("data", "word", query.toLowerCase(), 15);
        MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, CITY_NAME});
        for (int i = 0; i < historyList.size(); i++) {
            cursor.addRow(new Object[]{i, historyList.get(i)});
        }
        mAdapter.changeCursor(cursor);
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {

        mAdapter.getCursor().moveToPosition(position);
        ed_searchView.setText("" + mAdapter.getCursor().getString(1));
        clearFocus();
        //Show Dialog
        Dictionary.getInstance().showDialogAndAddToHistory(activity, mAdapter.getCursor().getString(1));
        return false;
    }

    private void loadHints() {
        final String[] from = new String[]{CITY_NAME};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(activity,
                R.layout.hint_row,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setSuggestionsAdapter(mAdapter);
    }
}
