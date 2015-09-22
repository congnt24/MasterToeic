package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Question;

/**
 * Created by cong on 8/30/2015.
 */
public class Part3Activity extends AppCompatActivity {



    public static final int QUESTIONNUMBER = 30;
    private List<Question> list = new ArrayList<>();
    private int count = 0;
    Context context;
    int part=3;
    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private TableLayout groupbutton;
    private LinearLayout playercontainer;
    private android.widget.TextView tvq1;
    private RadioButton radio1A;
    private RadioButton radio1B;
    private RadioButton radio1C;
    private RadioButton radio1D;
    private RadioGroup groupradio1;
    private android.widget.TextView tvq2;
    private RadioButton radio2A;
    private RadioButton radio2B;
    private RadioButton radio2C;
    private RadioButton radio2D;
    private RadioGroup groupradio2;
    private android.widget.TextView tvq3;
    private RadioButton radio3A;
    private RadioButton radio3B;
    private RadioButton radio3C;
    private RadioButton radio3D;
    private RadioGroup groupradio3;
    private LinearLayout chooselayout;
    private String[] listResult = new String[QUESTIONNUMBER];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part3);
        if (getIntent()!=null) {
            Bundle b = getIntent().getExtras();
            if (b.getInt("part") == 3){//Part 3
                part = 3;
                getSupportActionBar().setTitle("Part 3");
            }else if (b.getInt("part") == 4){//Part 4
                part = 4;
                getSupportActionBar().setTitle("Part 4");
            }
        }

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

        this.chooselayout = (LinearLayout) findViewById(R.id.choose_layout);
        this.groupradio3 = (RadioGroup) findViewById(R.id.group_radio3);
        this.radio3D = (RadioButton) findViewById(R.id.radio3D);
        this.radio3C = (RadioButton) findViewById(R.id.radio3C);
        this.radio3B = (RadioButton) findViewById(R.id.radio3B);
        this.radio3A = (RadioButton) findViewById(R.id.radio3A);
        this.tvq3 = (TextView) findViewById(R.id.tv_q3);
        this.groupradio2 = (RadioGroup) findViewById(R.id.group_radio2);
        this.radio2D = (RadioButton) findViewById(R.id.radio2D);
        this.radio2C = (RadioButton) findViewById(R.id.radio2C);
        this.radio2B = (RadioButton) findViewById(R.id.radio2B);
        this.radio2A = (RadioButton) findViewById(R.id.radio2A);
        this.tvq2 = (TextView) findViewById(R.id.tv_q2);
        this.groupradio1 = (RadioGroup) findViewById(R.id.group_radio1);
        this.radio1D = (RadioButton) findViewById(R.id.radio1D);
        this.radio1C = (RadioButton) findViewById(R.id.radio1C);
        this.radio1B = (RadioButton) findViewById(R.id.radio1B);
        this.radio1A = (RadioButton) findViewById(R.id.radio1A);
        this.tvq1 = (TextView) findViewById(R.id.tv_q1);
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.groupbutton = (TableLayout) findViewById(R.id.group_button);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
        groupradio1.setOnCheckedChangeListener(onChecked1);
        groupradio2.setOnCheckedChangeListener(onChecked2);
        groupradio3.setOnCheckedChangeListener(onChecked3);
    }
    private void initdata() {
        if (SQLiteHelper.sqLiteDatabase != null) {
            try {
                Cursor cs = SQLiteHelper.sqLiteDatabase.query("part"+part, null, null, null, null, null, null);
                while (cs.moveToNext()) {
                    cs.getColumnCount();
                    Question q = new Question();
                    q.setAudio(cs.getString(1));
                    q.setQuestion(cs.getString(2));
                    q.setAnswer(cs.getString(3));
                    q.setTranscript(cs.getString(4));
                    list.add(q);
                }
                initQuestion();
            }catch (Exception e){
                Toast.makeText(Part3Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Part3Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }
    private void initQuestion(){
        try {
            AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part" + part + "/" + list.get(count).getAudio() + ".mp3"));
            String[] questions = list.get(count).getQuestion().replaceAll("(?m)^\\s", "").split("\\n");
            tvq1.setText(questions[0]);
            radio1A.setText(questions[1]);
            radio1B.setText(questions[2]);
            radio1C.setText(questions[3]);
            radio1D.setText(questions[4]);
            tvq2.setText(questions[5]);
            radio2A.setText(questions[6]);
            radio2B.setText(questions[7]);
            radio2C.setText(questions[8]);
            radio2D.setText(questions[9]);
            tvq3.setText(questions[10]);
            radio3A.setText(questions[11]);
            radio3B.setText(questions[12]);
            radio3C.setText(questions[13]);
            radio3D.setText(questions[14]);
        }catch (Exception e){

        }
    }

    private RadioGroup.OnCheckedChangeListener onChecked1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            listResult[count*3] = getResult(checkedId);
        }
    };
    private RadioGroup.OnCheckedChangeListener onChecked2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            listResult[count*3+1] = getResult(checkedId);
        }
    };
    private RadioGroup.OnCheckedChangeListener onChecked3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            listResult[count*3+2] = getResult(checkedId);
        }
    };
    public String getResult(int checkedId){
        String result = "X";
        switch (checkedId){
            case R.id.radioA:
                result = "A";
                break;
            case R.id.radioB:
                result = "B";
                break;
            case R.id.radioC:
                result = "C";
                break;
            case R.id.radioD:
                result = "D";
                break;
        }
        return result;
    }

    View.OnClickListener onPrev = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count>0){
                count--;
                initQuestion();
                groupradio1.clearCheck();
                groupradio2.clearCheck();
                groupradio3.clearCheck();
            }
        }
    };
    View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                count++;
                initQuestion();
                groupradio1.clearCheck();
                groupradio2.clearCheck();
                groupradio3.clearCheck();
            }
            else {//neu tra loi het 10 cau thi tu ket thuc khi chon next
                finishTest();
            }
        }
    };
    View.OnClickListener onFinish = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finishTest();
        }
    };

    /**
     * Finish test method, show the result activity of current test
     */
    private void finishTest(){
        //Count correct answer
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("part", part);
        b.putStringArray("result", listResult);
        b.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
        b.putInt("total", QUESTIONNUMBER);
        intent.putExtras(b);
        startActivity(intent);
    }

}
