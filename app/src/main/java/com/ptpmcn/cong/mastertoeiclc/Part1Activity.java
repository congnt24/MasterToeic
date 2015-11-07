package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import adapter.PagerAdapter;
import apv.congnt24.customviews.AnswerView;
import apv.congnt24.customviews.AudioCong;
import apv.congnt24.data.sqlite.SQLiteFactory;
import fragments.QuestionFragment;
import fragments.TranscriptFragment;
import model.Question;

/**
 * Created on 8/29/2015. This activity will display Part1 screen of TOEIC TEST
 * @author Nguyen Trung Cong
 *
 */
public class Part1Activity extends AppCompatActivity {

    Button btnfinish, btnprev, btnnext;
    TextView tvcau;
    LinearLayout playercontainer;
    ViewPager viewPager;
    Toolbar toolbar;
    List<Question> list = new ArrayList<>();
    private int count = 0;
    Context context;
    private String[] listResult = new String[10];
    PagerAdapter pagerAdapter;
    //Review mode
    private boolean isReviewMode = false;
    String time="";// Store time
    DictSearchView searchView;
    private Chronometer chrono;
    private AnswerView answerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Handle Review mode or test mode
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

    @Override
    protected void onPause() {
        super.onPause();
        AudioCong.getInstance().pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void initialize(){
        answerView = (AnswerView) findViewById(R.id.answer_view);
        answerView.setOnAnswerChange(new AnswerView.OnAnswerChange() {
            @Override
            public void onAnswerChange(AnswerView view, int index) {
                Toast.makeText(Part1Activity.this, "Choose: "+index, Toast.LENGTH_SHORT).show();
                listResult[count] = String.valueOf((char) (((int) 'A') + index));//ABCD
            }
        });
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        this.tvcau = (TextView) findViewById(R.id.tv_cau);
        chrono = (Chronometer) findViewById(R.id.chronometer);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvcau.setText("Câu 1:");
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());
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
        //
    }
    public void initTestMode(){
        //groupradio.setOnCheckedChangeListener(onChecked);
    }
    /**
     * Initialize data for part from database sqlite
     */
    private void initdata() {
        if (SQLiteFactory.getSQLiteHelper(context, "data.db") != null) {
            try {
                if (!isReviewMode) {
                    Cursor cs = SQLiteFactory.getSQLiteHelper(context, "data.db").queryRandom("part1", 10);
                    while (cs.moveToNext()) {
                        Question q = new Question();
                        q.setAudio(cs.getString(1));
                        q.setQuestion(cs.getString(2).split("\\)", 2)[1].trim());
                        q.setAnswer(cs.getString(3));
                        q.setTranscript(cs.getString(4));
                        list.add(q);
                    }
                    cs.close();
                }
                AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".mp3"));
                pagerAdapter = new PagerAdapter(getSupportFragmentManager()
                        , 1//part 1
                        , context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".jpg" //Question: img path
                        , list.get(count).getQuestion()+"\n"+list.get(count).getTranscript());  //transcript
                viewPager.setAdapter(pagerAdapter);
                if (isReviewMode){
                    answerView.clearAll();
                    autoCheckRadioButton(count);
                }
            }catch (Exception e){
                Toast.makeText(Part1Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Part1Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Using when user wanna change question
     */
    private void initQuestion(){
        try {
            viewPager.setCurrentItem(0, true);
            AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".mp3"));
            QuestionFragment.getInstance().setImageView(context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".jpg");
            TranscriptFragment.getInstance().setTranscript(list.get(count).getQuestion()+"\n"+list.get(count).getTranscript());
            if (isReviewMode){
                autoCheckRadioButton(count);
            }
            AudioCong.getInstance().play();
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
     * Finish test method, show the result activity of current test
     */
    private void finishTest(){
        //Count correct answer
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("part", 1);
        b.putString("time", chrono.getText().toString());
        b.putStringArray("result", listResult);
        b.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
        b.putInt("total", 10);
        intent.putExtras(b);
        startActivity(intent);
        if (chrono!=null)
            chrono.stop();
    }
    //Context Menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
        //user has long pressed your TextView
        menu.add(0, v.getId(), 0, "text that you want to show in the context menu - I use simply Copy");

        //cast the received View to TextView so that you can get its text
        TextView yourTextView = (TextView) v;

        //place your TextView's text in clipboard
        Toast.makeText(Part1Activity.this, yourTextView.getText(), Toast.LENGTH_SHORT).show();
    }

    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (DictSearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.init(this);
        menu.findItem(R.id.search).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search){
        }
        return super.onOptionsItemSelected(item);
    }

}