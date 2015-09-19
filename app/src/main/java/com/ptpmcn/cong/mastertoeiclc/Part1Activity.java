package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 8/29/2015.
 */
public class Part1Activity extends AppCompatActivity {

    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private LinearLayout playercontainer;
    private RadioGroup groupradio;
    private LinearLayout imagecontainer;
    private List<String[]> list = new ArrayList<>();
    private int count = 0;
    Context context;
    private boolean imgDisplay=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);
        context = getApplicationContext();
        initialize();
        initdata();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioCong.getInstance().pause();
    }


    public void initialize(){

        this.imagecontainer = (LinearLayout) findViewById(R.id.image_container);
        this.groupradio = (RadioGroup) findViewById(R.id.group_radio);
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());
        AudioCong.getInstance().setOnlyImageUi(imagecontainer, getLayoutInflater());
        imagecontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgDisplay) {
                    AudioCong.getInstance().setOnlyLyricUi(imagecontainer, getLayoutInflater());
                    imgDisplay = false;
                } else {
                    AudioCong.getInstance().setOnlyImageUi(imagecontainer, getLayoutInflater());
                    AudioCong.getInstance().initImage(new File(context.getFilesDir() + "/part1/" + list.get(count)[0] + ".jpg"));
                    imgDisplay = true;
                }
            }
        });
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
    }

    private void initdata() {
        if (SQLiteHelper.sqLiteDatabase != null) {
            Cursor cs = SQLiteHelper.sqLiteDatabase.query("part1", null, null, null, null, null, null);
            while (cs.moveToNext()) {
                list.add(new String[]{cs.getString(1), cs.getString(2)});
            }
            initQuestion();
        }else{
            Toast.makeText(Part1Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }
    private void initQuestion(){
        try {
            AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part1/" + list.get(count)[0] + ".mp3"))
                    .initImage(new File(context.getFilesDir() + "/part1/" + list.get(count)[0] + ".jpg"));
        }catch (Exception e){

        }
    }

    View.OnClickListener onPrev = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count>0){
                count--;
                initQuestion();
            }
        }
    };
    View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                count++;
                initQuestion();
            }
            else {//Ket thuc

            }
        }
    };
    View.OnClickListener onFinish = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("part", 1);
            b.putInt("correct", 1);
            b.putInt("total", 10);
            intent.putExtras(b);
            startActivity(intent);
        }
    };





    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.showLyric) {
            if (imgDisplay) {
                Toast.makeText(Part1Activity.this, "Show Transcript", Toast.LENGTH_SHORT).show();
                AudioCong.getInstance().setOnlyLyricUi(imagecontainer, getLayoutInflater());
                imgDisplay = false;
            } else {
                AudioCong.getInstance().setOnlyImageUi(imagecontainer, getLayoutInflater());
                AudioCong.getInstance().initImage(new File(context.getFilesDir() + "/part1/" + list.get(count)[0] + ".jpg"));
                imgDisplay = true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
