package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apv.congnt24.customviews.CustomFlashCard;
import apv.congnt24.customviews.ICustomColorFlashCard;
import apv.congnt24.data.sqlite.SQLiteFactory;
import apv.congnt24.data.sqlite.SQLiteHelper;

public class FlashCardActivity extends Activity implements ICustomColorFlashCard {

    List<String[]> list = new ArrayList<>();
    CustomFlashCard[] flashCards;
    LinearLayout parent;
    private SQLiteHelper dictSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        dictSQL = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(this, "dict.db");
        parent = (LinearLayout) findViewById(R.id.layout_parent);
        list = generateData();
        flashCards = new CustomFlashCard[list.size()];
        for (int i = 0; i < list.size(); i++) {
            flashCards[i]=new CustomFlashCard(this);
            flashCards[i].setId(0x1000+i);
            flashCards[i].init(list.get(i), this);
            parent.addView(flashCards[i]);
        }
    }

    public List<String[]> generateData(){
        List<String[]> tmplist = new ArrayList<String[]>();
        Cursor c =dictSQL.queryRandom("history", 5);
        while (c.moveToNext()){
            tmplist.add(new String[]{c.getString(c.getColumnIndex("word")),c.getString(c.getColumnIndex("summary"))});
        }
        return tmplist;
    }

    @Override
    public void changeFrontColor(View v) {
        v.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void changeBackColor(View v) {

        v.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void changeFrontTextColor(View v) {

        ((TextView)v).setTextColor(Color.BLACK);
    }

    @Override
    public void changeBackTextColor(View v) {

        ((TextView)v).setTextColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
