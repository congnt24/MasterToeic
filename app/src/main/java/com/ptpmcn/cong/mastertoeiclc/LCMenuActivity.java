package com.ptpmcn.cong.mastertoeiclc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ptpmcn.cong.dbhandler.SQLiteHelper;
import com.ptpmcn.cong.jsonconfig.About_Activity;

public class LCMenuActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnpart1;
    private Button btnpart2;
    private Button btnpart3;
    private Button btnpart4;
    private Button btnabout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcmenu);
        initialize();
    }

    public void initialize(){

        this.btnabout = (Button) findViewById(R.id.btn_about);
        this.btnpart4 = (Button) findViewById(R.id.btn_part4);
        this.btnpart3 = (Button) findViewById(R.id.btn_part3);
        this.btnpart2 = (Button) findViewById(R.id.btn_part2);
        this.btnpart1 = (Button) findViewById(R.id.btn_part1);
        this.tvTitle = (TextView) findViewById(R.id.tv_Title);
        SQLiteHelper.getInstance(getApplicationContext()).openDatabase("data.db");
        btnpart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Part1Activity.class));
            }
        });
        btnpart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Part3Activity.class);
                Bundle b = new Bundle();
                b.putInt("part", 2);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnpart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Part3Activity.class);
                Bundle b = new Bundle();
                b.putInt("part", 3);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnpart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Part3Activity.class);
                Bundle b = new Bundle();
                b.putInt("part", 4);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), About_Activity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
