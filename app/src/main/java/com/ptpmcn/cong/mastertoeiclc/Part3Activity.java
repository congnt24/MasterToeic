package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.PagerAdapter;
import fragments.QuestionFragment;
import fragments.TranscriptFragment;
import model.Question;

/**
 * Created by cong on 8/30/2015.
 */
public class Part3Activity extends AppCompatActivity {



    public static final int QUESTIONNUMBER = 30;
    public List<Question> list = new ArrayList<>();
    public int count = 0;
    Context context;
    int part=3;
    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private LinearLayout playercontainer;
    //private String[] listResult = new String[QUESTIONNUMBER];
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private static Part3Activity instance;
    private Chronometer chrono;
    public boolean isReviewMode = false;
    private String time;
    public String[] listResult;

    public static Part3Activity getInstance(){
        if (instance == null)
            instance = new Part3Activity();
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part3);
        Log.d("LOG", "ACTIVITY PART 3: ");
        instance = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        context = getApplicationContext();
        initialize();
        if (getIntent()!=null) {
            Bundle b = getIntent().getExtras();
            if(b!=null) {
                if (b.getInt("part", 0) !=0) {//Part 2,3 or 4
                    part = b.getInt("part");
                    try {
                        getSupportActionBar().setTitle("Part "+part);
                    }catch (NullPointerException e){
                        Log.e("ActionBar", "No ActionBar");
                    }
                }
                if( b.getBoolean("reviewmode", false)){//review mode
                    isReviewMode = true;
                    listResult = b.getStringArray("result");
                    list = b.getParcelableArrayList("question");
                    time = b.getString("time");
                    count = 0;
                }
            }
        }
        if (!isReviewMode){
            initTestMode();
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
        }else{//Revire mode
            chrono.setText(time);
        }
        initdata();
    }


    @Override
    protected void onPause() {
        super.onPause();
        AudioCong.getInstance().pause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LCMenuActivity.class));
    }
    public void initialize(){
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        chrono = (Chronometer) findViewById(R.id.chronometer);
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());

        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
    }

    private void initTestMode() {

    }

    private void initdata() {
        if (SQLiteHelper.getInstance(context) != null) {
            try {
                if (!isReviewMode){
                    Cursor cs = SQLiteHelper.getInstance(context).queryRandom("part" + part, 10);
                    while (cs.moveToNext()) {
                        cs.getColumnCount();
                        Question q = new Question();
                        q.setAudio(cs.getString(1));
                        q.setQuestion(part!=2?cs.getString(2):"1).\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "2).\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "3).\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------\n" +
                                "-----------" //Question:
                        );
                        q.setAnswer(cs.getString(3));
                        q.setTranscript(cs.getString(4));
                        list.add(q);
                    }
                    cs.close();
                }
                AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part" + part + "/" + list.get(count).getAudio() + ".mp3"));
                pagerAdapter = new PagerAdapter(getSupportFragmentManager()
                        , part//part
                        , list.get(count).getQuestion()
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
            AudioCong.getInstance().pause();
            String[] questions = list.get(count).getQuestion().replaceAll("(?m)^\\s", "").split("\\n");
            QuestionFragment.getInstance().initQuestion(questions, count);
            TranscriptFragment.getInstance().setTranscript("" + list.get(count).getTranscript());
            AudioCong.getInstance().play();
        } catch (Exception e) {
            Log.e("InitQuestion", "Error initialize Question");
        }
    }


    View.OnClickListener onPrev = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count>0){
                QuestionFragment.getInstance().clearGroupCheck();
                count--;
                initQuestion();
            }
        }
    };
    View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                QuestionFragment.getInstance().clearGroupCheck();
                count++;
                initQuestion();
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
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("part", part);
        b.putString("time", chrono.getText().toString());
        b.putStringArray("result", QuestionFragment.getInstance().listResult);
        b.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
        b.putInt("total", QUESTIONNUMBER);
        intent.putExtras(b);
        startActivity(intent);
        if (chrono!=null)
            chrono.stop();
    }

}
