package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import adapter.PagerAdapter;
import fragments.QuestionFragment;
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
    private LinearLayout chooselayout;
    private String[] listResult = new String[QUESTIONNUMBER];
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (getIntent()!=null) {
            Bundle b = getIntent().getExtras();
            if (b.getInt("part") == 3){//Part 3
                part = 3;
                getSupportActionBar().setTitle("Part 3");
            }else if (b.getInt("part") == 4){//Part 4
                part = 4;
                getSupportActionBar().setTitle("Part 4");
            }else if (b.getInt("part") == 2){//Part 4
                part = 2;
                getSupportActionBar().setTitle("Part 2");
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
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.groupbutton = (TableLayout) findViewById(R.id.group_button);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
//        groupradio1.setOnCheckedChangeListener(onChecked1);
//        groupradio2.setOnCheckedChangeListener(onChecked2);
//        groupradio3.setOnCheckedChangeListener(onChecked3);
    }
    private void initdata() {
        if (SQLiteHelper.getInstance(context) != null) {
            try {
                Cursor cs = SQLiteHelper.getInstance(context).queryRandom("part"+part, 10);
                while (cs.moveToNext()) {
                    cs.getColumnCount();
                    Question q = new Question();
                    q.setAudio(cs.getString(1));
                    q.setQuestion(cs.getString(2));
                    q.setAnswer(cs.getString(3));
                    q.setTranscript(cs.getString(4));
                    list.add(q);
                }
                AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part" + part + "/" + list.get(count).getAudio() + ".mp3"));
                pagerAdapter = new PagerAdapter(getSupportFragmentManager()
                        , 3//part
                        , list.get(count).getQuestion() //Question:
                        , ""+list.get(count).getTranscript());  //transcript
                viewPager.setAdapter(pagerAdapter);
            }catch (Exception e){
                Toast.makeText(Part3Activity.this, "Dữ liệu không khả dụng1", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Part3Activity.this, "Dữ liệu không khả dụng2", Toast.LENGTH_SHORT).show();
        }
    }
    private void initQuestion(){
        try {
            AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part" + part + "/" + list.get(count).getAudio() + ".mp3"));
            String[] questions = list.get(count).getQuestion().replaceAll("(?m)^\\s", "").split("\\n");
            QuestionFragment.getInstance().setQuestion(questions);
        } catch (Exception e) {

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
//                groupradio1.clearCheck();
//                groupradio2.clearCheck();
//                groupradio3.clearCheck();
            }
        }
    };
    View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                count++;
                initQuestion();
//                groupradio1.clearCheck();
//                groupradio2.clearCheck();
//                groupradio3.clearCheck();
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
