package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 9/11/2015.
 */
public class Part5Activity extends AppCompatActivity {

    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private TableLayout groupbutton;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioButton radioD;
    private RadioGroup groupradio;
    private LinearLayout parentgroup;
    private LinearLayout questioncontainer;

    Context context;


    private List<String[]> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part5);
        context = getApplicationContext();
        initiaalize();
    }

    void initiaalize(){
        this.questioncontainer = (LinearLayout) findViewById(R.id.question_container);
        this.parentgroup = (LinearLayout) findViewById(R.id.parent_group);
        this.groupradio = (RadioGroup) findViewById(R.id.group_radio);
        this.radioD = (RadioButton) findViewById(R.id.radioD);
        this.radioC = (RadioButton) findViewById(R.id.radioC);
        this.radioB = (RadioButton) findViewById(R.id.radioB);
        this.radioA = (RadioButton) findViewById(R.id.radioA);
        this.groupbutton = (TableLayout) findViewById(R.id.group_button);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
    }



    private void initdata() {
       /* if (SQLiteHelper.sqLiteDatabase != null) {
            try {
                Cursor cs = SQLiteHelper.sqLiteDatabase.query("part5", null, null, null, null, null, null);
                while (cs.moveToNext()) {
                    cs.getColumnCount();
                    list.add(new String[]{cs.getString(1)//audio name
                            , cs.getString(2)//3question
                            , cs.getString(3)//3 answer
                            , cs.getString(4)//transcript
                    });
                }
                initQuestion();
            }catch (Exception e){
                Toast.makeText(this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void initQuestion() {

    }
}
