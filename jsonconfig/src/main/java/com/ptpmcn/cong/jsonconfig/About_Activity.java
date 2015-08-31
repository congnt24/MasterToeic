package com.ptpmcn.cong.jsonconfig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cong on 8/29/2015.
 */
public class About_Activity extends AppCompatActivity {

    public TextView tv_AppName, tv_Infor;
    public ImageView iv_Banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tv_AppName = (TextView) findViewById(R.id.tv_AppName);
        tv_Infor = (TextView) findViewById(R.id.tv_Information);
        iv_Banner = (ImageView) findViewById(R.id.iv_Banner);
        JsonParser.getInstance().LoadJson(this, "JsonConfig");
        tv_AppName.setText(JsonModel.AppConfig.AppName);
        tv_Infor.setText("Version: "+JsonModel.AppConfig.Version+"\n" +
                "Developed by: "+JsonModel.AppConfig.Developers+"\n" +
                "ReleaseDate: "+JsonModel.AppConfig.ReleaseDate);
    }
}
