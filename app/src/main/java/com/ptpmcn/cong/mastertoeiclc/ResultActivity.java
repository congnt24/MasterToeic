package com.ptpmcn.cong.mastertoeiclc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.Question;

/**
 * Created by cong on 8/31/2015.
 */
public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvtime;
    private TextView tvcorrect;
    private ImageView ivBanner;
    private Button btnktlai;
    private Button btnxemlai;
    private String[] result;
    private List<Question> list;
    private List<Integer> listcorrect = new ArrayList<>();
    private int part, total;
    private String time;
    private LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initialize();
        if (getIntent() != null) {
            Bundle b = getIntent().getExtras();
            part = b.getInt("part", 0);
            time = b.getString("time");
            result = b.getStringArray("result");
            total = b.getInt("total", 0);
            list = b.getParcelableArrayList("question");
            tvResult.setText("Kết Quả Part " + part);
            tvtime.setText("Thời gian: " + time);
            tvcorrect.setText("Số câu đúng: " + KiemTraKetQua(result, list));
        }
    }


    private int KiemTraKetQua(String[] list1, List<Question> list2) {
        int countCorrect = 0;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < list1.length; i++) {
                View row = inflater.inflate(R.layout.result_row, null);
                TextView stt = (TextView) row.findViewById(R.id.tv_stt2);
                TextView answer = (TextView) row.findViewById(R.id.tv_resultanswer);
                stt.setText(i + "");
                if (list1.length < 30) {//Part1
                    ((TextView) row.findViewById(R.id.tv_correctanswer)).setText(list2.get(i).getAnswer());
                    if (list1[i] != null) {
                        answer.setText(list1[i]);
                        if (list1[i].equalsIgnoreCase(list2.get(i).getAnswer())) {
                            listcorrect.add(i);
                            countCorrect++;
                            answer.setTextColor(Color.GREEN);
                        } else {
                            answer.setTextColor(Color.RED);
                        }
                    } else {
                        answer.setText("X");
                        answer.setTextColor(Color.RED);
                    }
                }else{//Part2,3,4
                    try {
                        ((TextView) row.findViewById(R.id.tv_correctanswer)).setText(list2.get(i / 3).getAnswer().substring(i % 3, i % 3 + 1));
                        if (list1[i] != null) {
                            answer.setText(list1[i]);
                            if (list1[i].equalsIgnoreCase(list2.get(i /3).getAnswer().substring(i % 3, i % 3 + 1))) {
                                listcorrect.add(i);
                                countCorrect++;
                                answer.setTextColor(Color.GREEN);
                            } else {
                                answer.setTextColor(Color.RED);
                            }
                        } else {
                            answer.setText("X");
                            answer.setTextColor(Color.RED);
                        }
                    }catch (Exception e){
                        Log.d("ERROR", "Unknown exception");
                    }
                }
                historyContainer.addView(row);

            }
        return  countCorrect;
    }

    private void initialize(){
        this.btnxemlai = (Button) findViewById(R.id.btn_xemlai);
        this.btnktlai = (Button) findViewById(R.id.btn_ktlai);
        this.ivBanner = (ImageView) findViewById(R.id.iv_Banner);
        this.tvcorrect = (TextView) findViewById(R.id.tv_correct);
        this.tvtime = (TextView) findViewById(R.id.tv_time);
        this.tvResult = (TextView) findViewById(R.id.tv_Result);
        historyContainer = (LinearLayout) findViewById(R.id.group_result_row);
        btnxemlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Bundle bundle = new Bundle();
                switch(part){
                    case 1:
                        intent = new Intent(ResultActivity.this, Part1Activity.class);
                        bundle.putBoolean("reviewmode", true);
                        bundle.putStringArray("result", result);
                        bundle.putString("time", time);
                        bundle.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(ResultActivity.this, Part3Activity.class);
                        bundle.putBoolean("reviewmode", true);
                        bundle.putStringArray("result", result);
                        bundle.putString("time", time);
                        bundle.putInt("part", part);
                        bundle.putParcelableArrayList("question", (ArrayList<? extends Parcelable>) list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        btnktlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Bundle bundle = new Bundle();
                switch(part){
                    case 1:
                        intent = new Intent(ResultActivity.this, Part1Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(ResultActivity.this, Part3Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        bundle.putInt("part", part);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }

            }
        });
    }
}
