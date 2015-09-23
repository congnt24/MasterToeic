package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
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
 * Created on 8/29/2015. This activity will display Part1 screen of TOEIC TEST
 * @author Nguyen Trung Cong
 *
 */
public class Part1Activity extends AppCompatActivity {

    private Button btnprev;
    private Button btnfinish;
    private Button btnnext;
    private TextView tvcau;
    private LinearLayout playercontainer;
    private RadioGroup groupradio;
    private ViewPager viewPager;
    private List<Integer> listRadioButton = new ArrayList<>();
    //private LinearLayout imagecontainer;
    private List<Question> list = new ArrayList<>();
    private int count = 0;
    Context context;
    private boolean imgDisplay=true;
    private String[] listResult = new String[10];
    PagerAdapter pagerAdapter;
    private LinearLayout parentgroup;
    //private int sentenceCount=0;

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

        //this.imagecontainer = (LinearLayout) findViewById(R.id.image_container);
        this.groupradio = (RadioGroup) findViewById(R.id.group_radio);
        this.playercontainer = (LinearLayout) findViewById(R.id.player_container);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.btnfinish = (Button) findViewById(R.id.btn_finish);
        this.btnprev = (Button) findViewById(R.id.btn_prev);
        this.tvcau = (TextView) findViewById(R.id.tv_cau);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvcau.setText("Câu 1:");
        AudioCong.getInstance().setDefaultUi(playercontainer, getLayoutInflater());
       /* AudioCong.getInstance().setOnlyImageUi(imagecontainer, getLayoutInflater());*/
        btnprev.setOnClickListener(onPrev);
        btnnext.setOnClickListener(onNext);
        btnfinish.setOnClickListener(onFinish);
        groupradio.setOnCheckedChangeListener(onChecked);
        listRadioButton.add(R.id.radioA);
        listRadioButton.add(R.id.radioB);
        listRadioButton.add(R.id.radioC);
        listRadioButton.add(R.id.radioD);
    }
    /**
     * Initialize data for part from database sqlite
     */
    private void initdata() {
        if (SQLiteHelper.sqLiteDatabase != null) {
            try {
                Cursor cs = SQLiteHelper.sqLiteDatabase.query("part1", null, null, null, null, null, null);
                while (cs.moveToNext()) {
                    Question q = new Question();
                    q.setAudio(cs.getString(1));
                    q.setAnswer(cs.getString(2));
                    q.setTranscript(cs.getString(3));
                    list.add(q);
                }
                AudioCong.getInstance().init(context, new File(context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".mp3"));
                pagerAdapter = new PagerAdapter(getSupportFragmentManager()
                        , 1//part 1
                        , context.getFilesDir() + "/part1/" + list.get(count).getAudio() + ".jpg" //Question: img path
                        , ""+list.get(count).getTranscript());  //transcript
                viewPager.setAdapter(pagerAdapter);
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
            TranscriptFragment.getInstance().setTranscript("" + list.get(count).getTranscript());
        }catch (Exception e){
            Toast.makeText(Part1Activity.this, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onPrev = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count>0){
                count--;
                tvcau.setText("Câu "+(count+1)+":");
                initQuestion();
                groupradio.clearCheck();
            }
        }
    };
    private View.OnClickListener onNext = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (count<9){
                count++;
                tvcau.setText("Câu "+(count+1)+":");
                initQuestion();
                groupradio.clearCheck();
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

    private RadioGroup.OnCheckedChangeListener onChecked = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (listRadioButton.contains(checkedId) ) {
                listResult[count] = getResult(checkedId);
            }
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
        b.putStringArray("result", listResult);
        b.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
        b.putInt("total", 10);
        intent.putExtras(b);
        startActivity(intent);
    }
    public String[] ListToArray(List<String> list){
        String[] tmp = new String[10];
        for (int i = 0; i < list.size(); i++) {
            tmp[i] = list.get(i);
        }
        return tmp;
    }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
