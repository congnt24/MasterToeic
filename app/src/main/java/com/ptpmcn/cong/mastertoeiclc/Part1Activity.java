package com.ptpmcn.cong.mastertoeiclc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cong.audiocong.AudioCong;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.ptpmcn.cong.dbhandler.SQLiteHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import adapter.PagerAdapter;
import apv.congnt.customview.AnswerView;
import dialogs.DialogDict;
import fragments.QuestionFragment;
import fragments.TranscriptFragment;
import model.Question;


/**
 * Created on 8/29/2015. This activity will display Part1 screen of TOEIC TEST
 * @author Nguyen Trung Cong
 *
 */
public class Part1Activity extends AppCompatActivity {

    public static TessBaseAPI baseApi;
    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private static final int REQUEST_SELECT_PHOTO= 11;
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
    private SimpleCursorAdapter mAdapter;
    //Review mode
    private boolean isReviewMode = false;
    String time="";// Store time

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
    private AnswerView answerView;

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
                    list = b.getParcelableArrayList("question");
                    time = b.getString("time");
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
        baseApi = new TessBaseAPI();
        baseApi.init(getFilesDir().getPath(), "eng");
    }
    public void initTestMode(){
        //groupradio.setOnCheckedChangeListener(onChecked);
    }
    /**
     * Initialize data for part from database sqlite
     */
    private void initdata() {
        if (SQLiteHelper.getInstance(context) != null) {
            try {
                if (!isReviewMode) {
                    Cursor cs = SQLiteHelper.getInstance(context).queryRandom("part1", 10);
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
            //Show Dialog
            DialogDict.getInstance().showDialog(Part1Activity.this, "" + mAdapter.getCursor().getString(1), "Define of " + mAdapter.getCursor().getString(1));

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
        if (id == R.id.cameracapture) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            return true;
        }
        if (id == R.id.galleryselect){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT PHOTO"), REQUEST_SELECT_PHOTO);
            return true;
        }
        if (id == R.id.search){
            mSearchCheck = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            baseApi.setImage((Bitmap) extras.get("data"));
            String text = baseApi.getUTF8Text();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }else
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                baseApi.setImage(bm);
                String text = baseApi.getUTF8Text();
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
    }
}