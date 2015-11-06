package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apv.congnt24.customviews.AnswerView;
import apv.congnt24.data.sqlite.SQLiteFactory;
import model.Question;

public class Part5Activity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private boolean isReviewMode;
    private String[] listResult = new String[10];
    private ArrayList<Question> list = new ArrayList<>();
    private android.widget.Button btnprev;
    private android.widget.Button btnfinish;
    private android.widget.Button btnnext;
    private android.widget.LinearLayout groupbutton;
    private android.widget.LinearLayout playercontainer;
    private android.widget.TextView tvcau;
    private AnswerView answerView;
    private android.widget.LinearLayout parentgroup;
    private android.widget.TextView question;
    private android.widget.Chronometer chrono;
    private int count=0;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part5);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SQLiteFactory.getSQLiteHelper(context, "data/data_reading.db").openDatabase("data/data_reading.db");
       if (getIntent()!=null) {
            Bundle b = getIntent().getExtras();
            if(b!=null){//review mode
                if( b.getBoolean("reviewmode", false)){
                    isReviewMode = true;
                    listResult = b.getStringArray("result");
                    list = b.getParcelableArrayList("question");
                    time = b.getString("time");
                }
            }
        }
        initialize();
        initdata();
    }

    private void initialize() {
        this.chrono = (Chronometer) findViewById(R.id.chronometer);
        this.question = (TextView) findViewById(R.id.question);
        this.parentgroup = (LinearLayout) findViewById(R.id.parent_group);
        this.answerView = (AnswerView) findViewById(R.id.answer_view);
        this.tvcau = (TextView) findViewById(R.id.tv_cau);
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.groupbutton = (LinearLayout) findViewById(R.id.group_button);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);

        answerView = (AnswerView) findViewById(R.id.answer_view);
        answerView.setOnAnswerChange(new AnswerView.OnAnswerChange() {
            @Override
            public void onAnswerChange(AnswerView view, int index) {
                Toast.makeText(Part5Activity.this, "Choose: " + index, Toast.LENGTH_SHORT).show();
                listResult[count] = String.valueOf((char) (((int) 'A') + index));//ABCD
            }
        });
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
        if (!isReviewMode){
            initTestMode();
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
        }else{
            chrono.setText(time);
        }
    }

    public void initTestMode(){
        //groupradio.setOnCheckedChangeListener(onChecked);
    }
    //Button click handler

    private View.OnClickListener onPrev = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count>0){
                count--;
                tvcau.setText("Câu " + (count + 1) + ":");
                answerView.clearAll();
                initQuestion();
            }
        }
    };
    private View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                count++;
                tvcau.setText("Câu "+(count +1)+":");
                answerView.clearAll();
                initQuestion();
            }
            else {//neu tra loi het 10 cau thi tu ket thuc khi chon next
                finishTest();
            }
        }
    };
    private View.OnClickListener onFinish = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finishTest();
        }
    };

    /**
     * Initialize data for part from database sqlite
     */
    private void initdata() {
        if (SQLiteFactory.getSQLiteHelper(context, "data/data_reading.db") != null) {
            try {
                if (!isReviewMode) {
                    Cursor cs = SQLiteFactory.getSQLiteHelper(context, "data/data_reading.db").queryRandom("part5", 10);
                    while (cs.moveToNext()) {
                        Question q = new Question();
                        q.setQuestion(cs.getString(1).split("\\)", 2)[1].trim()+"\n\n"+cs.getString(2)+"\n"+cs.getString(3)+"\n"+cs.getString(4)+"\n"+cs.getString(5));
                        q.setAnswer(String.valueOf((char) (cs.getInt(6)+64)));//String.valueOf((char)
                        list.add(q);
                    }
                    cs.close();
                } else
                if (isReviewMode){
                    answerView.clearAll();
                    autoCheckRadioButton(count);
                }
                initQuestion();
            }catch (Exception e){
                Toast.makeText(Part5Activity.this, "Dữ liệu không khả dụng3", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Part5Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Using when user wanna change question
     */
    private void initQuestion(){
        try {
            question.setText(list.get(count).getQuestion());
            if (isReviewMode){
                autoCheckRadioButton(count);
            }
        }catch (Exception e){
            //Toast.makeText(Part1Activity.this, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * After finish testting, user can't review all the test they just taking. In this review mode,
     * this method will be used to check the corrects answer and the answer what user choosed
     * @param i: the count = id of question
     */
    public void autoCheckRadioButton(int i){
        answerView.disableAll();
        int usercheck = listResult[i]==null?-1:(int)listResult[i].charAt(0)-65;
        int correct = (int)list.get(i).getAnswer().toUpperCase().charAt(0) - 65;
        if (usercheck!=-1)
            answerView.setActiveIndex(usercheck);
        answerView.setTrueAnswer(correct);
    }

    /**
     * Finish test method, show the result activity of current test
     */
    private void finishTest(){
        //Count correct answer
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("part", 5);
        b.putString("time", chrono.getText().toString());
        b.putStringArray("result", listResult);
        b.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
        b.putInt("total", 10);
        intent.putExtras(b);
        startActivity(intent);
        if (chrono!=null)
            chrono.stop();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
