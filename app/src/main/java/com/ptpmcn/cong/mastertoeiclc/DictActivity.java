package com.ptpmcn.cong.mastertoeiclc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.ptpmcn.cong.dictionary.Dictionary;
public class DictActivity extends Activity {
    FrameLayout dict_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
        dict_container = (FrameLayout) findViewById(R.id.dict_container);
        Dictionary.getInstance().init(getApplication()).setDefaultUi(dict_container, getLayoutInflater());

    }
}
