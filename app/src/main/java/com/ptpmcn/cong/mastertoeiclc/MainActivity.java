package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import com.ptpmcn.cong.dictionary.Dictionary;

import apv.congnt24.customviews.SquarButton;
import apv.congnt24.data.sqlite.SQLiteFactory;
import apv.congnt24.data.sqlite.SQLiteHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.btn_lc)
    SquarButton btnLc;
    @Bind(R.id.btn_rc)
    SquarButton btnRc;
    @Bind(R.id.btn_dict)
    SquarButton btnDict;
    @Bind(R.id.btn_about)
    SquarButton btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteFactory.getSQLiteHelper(getApplicationContext(), "data.db").openDatabase("data.db");
        SQLiteFactory.getSQLiteHelper(getApplicationContext(), "dict.db").openDatabase("dict.db");
        Dictionary.getInstance().initTextToSpeech(this);
        ButterKnife.bind(this);

        //Check word in history
        if (getHistoryNotLearnYet() >= 5) {
            new AlertDialog.Builder(this).setTitle("Do you want to learn some words you've lookup?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, FlashCardActivity.class);
                            intent.putExtra("isnewword", true);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private int getHistoryNotLearnYet() {
        int tmp = 0;
        SQLiteHelper dict = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(getApplicationContext(), "dict.db");
        createHistoryTable();
        Cursor c = dict.getSQLiteDatabase().query("history", null, " count = ?", new String[]{"0"}, null, null, null);
        while (c.moveToNext()) {
            tmp++;
        }
        return tmp;
    }

    private void createHistoryTable() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT NOT NULL UNIQUE, phonetic TEXT, summary TEXT, mean TEXT, count INTEGER DEFAULT 0)";
        SQLiteFactory.getSQLiteHelper(getApplicationContext(), "dict.db").getSQLiteDatabase().execSQL(CREATE_TABLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_lc)
    public void openLCActivity() {
        startActivity(new Intent(this, LCMenuActivity.class));
    }

    @OnClick(R.id.btn_about)
    public void openAboutActivity() {
        startActivity(new Intent(getApplicationContext(), FlashCardActivity.class));
    }

    @OnClick(R.id.btn_rc)
    public void openPart5Activity() {
        startActivity(new Intent(getApplicationContext(), Part5Activity.class));
    }

    @OnClick(R.id.btn_dict)
    public void openDictActivity() {
        startActivity(new Intent(getApplicationContext(), DictActivity.class));
    }
}
