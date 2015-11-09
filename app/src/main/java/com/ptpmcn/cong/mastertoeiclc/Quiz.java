package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class Quiz extends Activity {
    private ToggleButton[] tb_words = new ToggleButton[5];
    private ToggleButton[] tb_summary = new ToggleButton[5];
    private String[] words = new String[5];
    private String[] summary = new String[5];
    private LinearLayout leftLayout, rightLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        leftLayout = new LinearLayout(this);
        addContentView(leftLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rightLayout = new LinearLayout(this);
        addContentView(rightLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initializeData();
        initializeUI();
    }

    /**
     * Query 5 random row from "history" table, set to 2 array
     */
    private void initializeData() {
        //.......
        words = new String[]{"1", "2", "3", "4", "5"};
        summary = new String[]{"s1", "s2", "s3", "s4", "s5"};
    }

    private void initializeUI() {
        for (int i = 0; i < tb_words.length; i++) {
            tb_words[i] = new ToggleButton(this);
            tb_words[i].setTextOff(words[i]);
            tb_words[i].setTextOn(words[i]);
            leftLayout.addView(tb_words[i]);
            tb_summary[i] = new ToggleButton(this);
            tb_summary[i].setTextOff(summary[i]);
            tb_summary[i].setTextOn(summary[i]);
            rightLayout.addView(tb_summary[(i+1)%5]);
        }
    }
}
