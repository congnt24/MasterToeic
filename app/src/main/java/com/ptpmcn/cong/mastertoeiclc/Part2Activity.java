package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 8/31/2015.
 */
public class Part2Activity extends AppCompatActivity {

    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private TableLayout groupbutton;
    private LinearLayout playercontainer;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioGroup groupradio;
    private RadioButton radioA1;
    private RadioButton radioB1;
    private RadioButton radioC1;
    private RadioGroup groupradio1;
    private RadioButton radioA2;
    private RadioButton radioB2;
    private RadioButton radioC2;
    private RadioGroup groupradio2;
    private List<String[]> list = new ArrayList<>();
    private int count = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part2);
        context = getApplicationContext();
        initialize();
        initdata();
    }

    private void initialize() {
        this.groupradio2 = (RadioGroup) findViewById(R.id.group_radio2);
        this.radioC2 = (RadioButton) findViewById(R.id.radioC2);
        this.radioB2 = (RadioButton) findViewById(R.id.radioB2);
        this.radioA2 = (RadioButton) findViewById(R.id.radioA2);
        this.groupradio1 = (RadioGroup) findViewById(R.id.group_radio1);
        this.radioC1 = (RadioButton) findViewById(R.id.radioC1);
        this.radioB1 = (RadioButton) findViewById(R.id.radioB1);
        this.radioA1 = (RadioButton) findViewById(R.id.radioA1);
        this.groupradio = (RadioGroup) findViewById(R.id.group_radio);
        this.radioC = (RadioButton) findViewById(R.id.radioC);
        this.radioB = (RadioButton) findViewById(R.id.radioB);
        this.radioA = (RadioButton) findViewById(R.id.radioA);
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.groupbutton = (TableLayout) findViewById(R.id.group_button);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
    }

    private void initdata() {
       /* if (SQLiteHelper.sqLiteDatabase != null) {
            Cursor cs = SQLiteHelper.sqLiteDatabase.query("part2", null, null, null, null, null, null);
            while (cs.moveToNext()) {
                list.add(new String[]{cs.getString(1), cs.getString(2)});
            }
            initQuestion();
        } else {
            Toast.makeText(Part2Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void initQuestion() {
        try {
            AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part2/" + list.get(count)[0] + ".mp3"));
        } catch (Exception e) {

        }
    }


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
            Toast.makeText(Part2Activity.this, "Show Transcript", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    View.OnClickListener onPrev = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (count > 0) count--;
            initQuestion();
        }
    };
    View.OnClickListener onNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (count < 29) count++;
            initQuestion();
        }
    };
    View.OnClickListener onFinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("part", 1);
            b.putInt("correct", 1);
            b.putInt("total", 30);
            intent.putExtras(b);
            startActivity(intent);
        }
    };
}
