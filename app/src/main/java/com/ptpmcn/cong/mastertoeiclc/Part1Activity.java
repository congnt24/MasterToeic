package com.ptpmcn.cong.mastertoeiclc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

     Button btnprev, btnfinish, btnnext;
     TextView tvcau;
     LinearLayout playercontainer;
     RadioGroup groupradio;
     ViewPager viewPager;
     Toolbar toolbar;
     List<Integer> listRadioButton = new ArrayList<>();
     List<Question> list = new ArrayList<>();
    private int count = 0;
    Context context;
    private String[] listResult = new String[10];
    PagerAdapter pagerAdapter;
    private SimpleCursorAdapter mAdapter;
    //Review mode
    private boolean isReviewMode = false;

    SearchView searchView;
    EditText ed_searchView;
    private boolean mSearchCheck;
    public static final String CITY_NAME = "cityName";
    private static final String[] COUNTRIES = {
            "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
            "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
            "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil",
            "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
            "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros",
            "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus",
            "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
            "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
            "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada",
            "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)",
            "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan",
            "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
            "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco",
            "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
            "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
            "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore",
            "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka",
            "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
            "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia",
            "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States",
            "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)",
            "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
    };
    private Chronometer chrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
        //Enable Icon on actionbar
//        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
//        ab.setDisplayHomeAsUpEnabled(true);
        //Handle Review mode or test mode
        if (getIntent()!=null) {
            Bundle b = getIntent().getExtras();
            if(b!=null){//review mode
                if( b.getBoolean("reviewmode", false)){
                    isReviewMode = true;
                    listResult = b.getStringArray("result");
                }
            }
        }
            initialize();
            initdata();
            loadHints();

    }

    private void loadHints() {
        final String[] from = new String[]{CITY_NAME};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.hint_row,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioCong.getInstance().pause();
    }

    public void initialize(){
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
        if (!isReviewMode){
            initTestMode();
            chrono = (Chronometer) findViewById(R.id.chronometer);
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
        }
    }
    public void initTestMode(){
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
                if (isReviewMode){
                    Log.d("A", "AAAAAAAAAA "+isReviewMode);
                    autoCheckRadioButton(count);
                }
                cs.close();
            }catch (Exception e){
                Toast.makeText(Part1Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Part1Activity.this, "Dữ liệu không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }

    public void autoCheckRadioButton(int i){
        Log.d("A", "AAAAAAAAAA " + (int)listResult[i].charAt(0));
        int check = (int)listResult[i].charAt(0)-65;
        groupradio.check(check);
        switch (check){
            case 0:
                ((RadioButton)findViewById(R.id.radioA)).setChecked(true);
                break;
            case 1:
                ((RadioButton)findViewById(R.id.radioB)).setChecked(true);
                break;
            case 2:
                ((RadioButton)findViewById(R.id.radioC)).setChecked(true);
                break;
            case 3:
                ((RadioButton)findViewById(R.id.radioD)).setChecked(true);
                break;
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
            if (isReviewMode){
                autoCheckRadioButton(count);
            }
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
        chrono.stop();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(this.getString(R.string.search_hint));
        ed_searchView = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        ed_searchView.setHintTextColor(getResources().getColor(R.color.my_primary_light));


        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnQueryTextListener(onQuerySearchView);
        searchView.setOnSuggestionListener(onQuerySuggestion);
        menu.findItem(R.id.search).setVisible(true);

        mSearchCheck = false;
        return true;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mSearchCheck = false;
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            //if (mSearchCheck) {
                // implement your search here
                giveSuggestions(query);
            //}
            return false;
        }
    };

    private void giveSuggestions(String query) {
        final MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, CITY_NAME});
        for (int i = 0; i < COUNTRIES.length; i++) {
            if (COUNTRIES[i].toLowerCase().contains(query.toLowerCase()))
                cursor.addRow(new Object[]{i, COUNTRIES[i]});
        }
        mAdapter.changeCursor(cursor);
    }
    private SearchView.OnSuggestionListener onQuerySuggestion = new SearchView.OnSuggestionListener() {
        @Override
        public boolean onSuggestionSelect(int position) {
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {
            mAdapter.getCursor().moveToPosition(position);
            ed_searchView.setText("" + mAdapter.getCursor().getString(1));
            searchView.clearFocus();
            //Hiding input
            /*InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            //check if no view has focus:
            View v=this.getCurrentFocus();
            if(v==null)
                return;

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWA*/
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.showLyric) {
            return true;
        }
        if (id == R.id.search){
            mSearchCheck = true;
        }

        return super.onOptionsItemSelected(item);
    }

}