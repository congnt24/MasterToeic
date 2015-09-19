package com.ptpmcn.cong.mastertoeiclc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initialize();
        if (getIntent() != null) {
            Bundle b = getIntent().getExtras();
            tvResult.setText("Kết Quả Part "+b.getInt("part"));
            tvtime.setText("Thời gian: "+ b.getInt("time"));
            tvcorrect.setText("Số câu đúng: "+b.getInt("correct"));
        }
    }

    private void initialize(){
        this.btnxemlai = (Button) findViewById(R.id.btn_xemlai);
        this.btnktlai = (Button) findViewById(R.id.btn_ktlai);
        this.ivBanner = (ImageView) findViewById(R.id.iv_Banner);
        this.tvcorrect = (TextView) findViewById(R.id.tv_correct);
        this.tvtime = (TextView) findViewById(R.id.tv_time);
        this.tvResult = (TextView) findViewById(R.id.tv_Result);

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
