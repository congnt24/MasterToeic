package com.ptpmcn.cong.dictionary;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apv.congnt24.data.sqlite.SQLiteFactory;
import apv.congnt24.data.sqlite.SQLiteHelper;


/**
 * Created by cong on 11/1/2015.
 */
public class Dictionary implements AdapterView.OnItemClickListener {
    private ImageButton btn_voice;
    private ImageButton btn_photo;
    private AutoCompleteTextView auto_search;
    private ListView lv_history;
    private static Dictionary ourInstance = new Dictionary();
    private Context context;
    private Activity activity;
    private TextToSpeech textToSpeech;
    private Dialog dialog;

    SQLiteHelper dictSQL;
    ArrayAdapter<String> adapter, his_adapter;
    List<String> list, listHistory;
    private IDictionaryHandler dictionaryHandler;

    public static Dictionary getInstance() {
        return ourInstance;
    }

    private Dictionary() {
    }

    public Dictionary init(Activity activity){
        this.context = activity;
        this.activity = activity;
        this.dictionaryHandler = (IDictionaryHandler) activity;
//Init database
        dictSQL = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(context, "dict.db");
        if (dictSQL.openDatabase("dict.db")){
            dictSQL.createHistoryTable();
            Toast.makeText(context, "Ket noi thanh cong den CSDL", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Khong the ket noi den CSDL", Toast.LENGTH_SHORT).show();
        }
        return this;
    }
    public Dictionary initTextToSpeech(Activity activity){
        //Text to speech
        textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        return this;
    }
    //---------Set Default View-----------
    public Dictionary setDefaultUi(ViewGroup container, LayoutInflater inflater){
        if (container==null){
            throw new NullPointerException("Container or Inflater cannot be null");
        }
        if (inflater==null){
            throw new IllegalArgumentException("Container or Inflater cannot be null");
        }
        View rootView=inflater.inflate(R.layout.default_dictionary_layout, container, true);
        AutoCompleteTextView auto = (AutoCompleteTextView) rootView.findViewById(R.id.auto_search);
        ImageButton bvoice = (ImageButton) rootView.findViewById(R.id.btn_voice);
        ImageButton bphoto = (ImageButton) rootView.findViewById(R.id.btn_photo);
        ListView lview = (ListView) rootView.findViewById(R.id.lv_history);
        setAutoView(auto);
        setBtnVoidceView(bvoice);
        setBtnPhotoceView(bphoto);
        setListView(lview);
        //Init event
        initEvent();
        return this;
    }

    public void setAutoView(AutoCompleteTextView autoView) {
        this.auto_search = autoView;
    }

    public void setBtnVoidceView(ImageButton btnVoidceView) {
        this.btn_voice = btnVoidceView;
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictionaryHandler.onClickSpeechToText();
            }
        });
    }
    public void setBtnPhotoceView(ImageButton btnPhotoceView) {
        this.btn_photo = btnPhotoceView;
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(activity);
                dialog.setContentView(R.layout.select_image_dialog);
                dialog.setTitle("ACTION");
                dialog.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dictionaryHandler.onClickCamera();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dictionaryHandler.onClickGallery();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void setListView(ListView listView) {
        this.lv_history = listView;
    }
    public void initEvent(){
        listHistory = loadHistory();
        his_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_activated_1, listHistory);
        lv_history.setAdapter(his_adapter);
        lv_history.setOnItemClickListener(this);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        auto_search.setAdapter(adapter);
        auto_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = dictSQL.getLikeWord("data", "word", auto_search.getText().toString(), 15);
                adapter.clear();
                adapter.addAll(list);
                adapter.notifyDataSetChanged();
                auto_search.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        auto_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String txt = parent.getItemAtPosition(position).toString();
                auto_search.setText(txt);
                auto_search.selectAll();
                showDialogAndAddToHistory(activity, txt);
                his_adapter.clear();
                his_adapter.addAll(loadHistory());
                his_adapter.notifyDataSetChanged();
            }
        });
    }

    private void showDialog(Cursor c, Activity activity) {
        DialogDict.getInstance().showDialog(activity
                , c.getString(c.getColumnIndex("word"))
                , " /" + c.getString(c.getColumnIndex("phonetic")) + "/"
                , c.getString(c.getColumnIndex("summary"))
                , c.getString(c.getColumnIndex("mean")));
    }
    private void showDialog(String title, Activity activity) {
        DialogDict.getInstance().showDialog(activity
                , title
                , " /" + title + "/"
                , "Từ khóa không tồn tại"
                , "");
    }
    public List<String> loadHistory(){
        List<String> tmp = new ArrayList<>();
        Cursor c = dictSQL.queryAll("history", "id", "DESC");
        while (c.moveToNext()){
            tmp.add(c.getString(1));
        }
        c.close();
        return tmp;
    }
    public void showDialogAndAddToHistory(Activity activity, String txt){
        SQLiteHelper sqLiteHelper = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(activity, "dict.db");
        Cursor c = sqLiteHelper.getOneRow("data", "word", txt.trim().toLowerCase());
        if (c.moveToNext()) {
            showDialog(c, activity);
            ContentValues cv = new ContentValues();
            cv.put("word", txt);
            cv.put("phonetic", c.getString(1));
            cv.put("summary", c.getString(2));
            cv.put("mean", c.getString(3));
            sqLiteHelper.insert("history", cv);
        }else{
            showDialog(txt, activity);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SQLiteHelper sqLiteHelper = (SQLiteHelper) SQLiteFactory.getSQLiteHelper(activity, "dict.db");
        Cursor c = sqLiteHelper.getOneRow("data", "word", his_adapter.getItem(position).trim().toLowerCase());
        if (c.moveToNext()) {
            showDialog(c, activity);
        }
    }
    public void setSearchView(String txt){
        auto_search.setText(txt);
        auto_search.clearFocus();
        auto_search.showDropDown();
    }
    public void setSearchViewAndShowDialog(String txt){
        auto_search.setText(txt);
        auto_search.clearFocus();
        showDialogAndAddToHistory(activity, txt);
    }
    public void speech(String toSpeak){
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void close(){
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
