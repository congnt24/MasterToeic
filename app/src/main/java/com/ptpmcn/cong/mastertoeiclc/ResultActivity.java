package com.ptpmcn.cong.mastertoeiclc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 8/31/2015.
 */
public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvtime;
    private TextView tvcorrect, tv_correctanswer, tv_resultanswer;
    private ImageView ivBanner;
    private Button btnktlai;
    private Button btnxemlai;
    private String[] result, correctanswer;
    private List<Integer> listcorrect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initialize();
        if (getIntent() != null) {
            Bundle b = getIntent().getExtras();
            result = b.getStringArray("result");
            correctanswer = b.getStringArray("correctanswer");
            tvResult.setText("Kết Quả Part "+b.getInt("part"));
            tvtime.setText("Thời gian: "+ b.getInt("time"));
            tvcorrect.setText("Số câu đúng: "+KiemTraKetQua(result, correctanswer));
            tv_resultanswer.setText("");
        }
    }

    private int KiemTraKetQua(String[] list1, String[] list2) {
        int countCorrect=0;
        for (int i = 0; i < list1.length; i++) {
            tv_resultanswer.append(list1[i]+" ");
            tv_correctanswer.append(list2[i]+" ");
            if (list1[i].equalsIgnoreCase(list2[i])){
                listcorrect.add(i);
                countCorrect++;
            }
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
        this.tv_correctanswer = (TextView) findViewById(R.id.tv_correctanswer);
        this.tv_resultanswer = (TextView) findViewById(R.id.tv_resultanswer);

        btnxemlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnktlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, Part1Activity.class));
            }
        });
    }
}
