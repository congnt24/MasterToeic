package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.ptpmcn.cong.jsonconfig.About_Activity;

import apv.congnt24.customviews.SquarButton;
import apv.congnt24.data.sqlite.SQLiteFactory;
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
        ButterKnife.bind(this);
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
    public void openAboutActivity(){
        startActivity(new Intent(getApplicationContext(), About_Activity.class));
    }
    @OnClick(R.id.btn_rc)
    public void openPart5Activity(){
        startActivity(new Intent(getApplicationContext(), Part5Activity.class));
    }
    @OnClick(R.id.btn_dict)
    public void openDictActivity(){
        startActivity(new Intent(getApplicationContext(), DictActivity.class));
    }
}
