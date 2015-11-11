package com.ptpmcn.cong.mastertoeiclc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apv.congnt24.customviews.CustomFlashCard;
import apv.congnt24.customviews.ICustomColorFlashCard;
import apv.congnt24.data.sqlite.SQLiteFactory;
import apv.congnt24.data.sqlite.SQLiteHelper;

public class FlashCardActivity extends AppCompatActivity implements ICustomColorFlashCard {

    List<String[]> list = new ArrayList<>();
    CustomFlashCard[] flashCards;
    LinearLayout parent;
    private SQLiteHelper dictSQL;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz");
        dictSQL = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(this, "dict.db");
        parent = (LinearLayout) findViewById(R.id.layout_parent);
        if(getIntent()!=null){
            if (getIntent().getBooleanExtra("isnewword", false)){
                list = generateNewWord();
            }else{
                list = generateData();
            }
        }else{
            list = generateData();
        }

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
    public List<String[]> generateNewWord(){
        List<String[]> tmplist = new ArrayList<String[]>();
        Cursor c =dictSQL.getSQLiteDatabase().query("history", null
                , "count = ?", new String[]{"0"}
                , null, null, "count ASC", "5");
        while (c.moveToNext()){
            tmplist.add(new String[]{c.getString(c.getColumnIndex("word")),c.getString(c.getColumnIndex("summary"))});
        }
        return tmplist;
    }
    @Override
    public void changeFrontColor(View v) {
        v.setBackgroundResource(R.drawable.card_boder);
    }

    @Override
    public void changeBackColor(View v) {

//        v.setBackgroundColor(Color.BLACK);
        v.setBackgroundResource(R.drawable.card_boder);
    }

    @Override
    public void changeFrontTextColor(View v) {
        ((TextView)v).setTextSize(40);
        ((TextView)v).setTextColor(Color.BLACK);
    }

    @Override
    public void changeBackTextColor(View v) {
        ((TextView)v).setTextSize(40);
        ((TextView)v).setTextColor(Color.BLUE);
    }

    @Override
    public void onFlipCard(View v) {
        TextView tv = (TextView) v;
        String word = tv.getText().toString();
        Log.d("SQL", "SQL: "+word);
        dictSQL.getSQLiteDatabase().execSQL("UPDATE history SET count=count+1 WHERE word = ?", new String[]{word});
    }
    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, MainActivity.class));
//        finish();
        super.onBackPressed();
    }
}
