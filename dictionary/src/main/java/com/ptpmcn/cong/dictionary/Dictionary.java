package com.ptpmcn.cong.dictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ptpmcn.cong.dbhandler.BaseSQLiteHelper;
import com.ptpmcn.cong.dbhandler.DictSQLiteHelper;
import com.ptpmcn.cong.dbhandler.SQLiteFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 11/1/2015.
 */
public class Dictionary {
    private ImageButton btn_voidce;
    private ImageButton btn_photo;
    private AutoCompleteTextView auto_search;
    private ListView lv_history;
    private static Dictionary ourInstance = new Dictionary();
    private Context context;

    BaseSQLiteHelper dictSQL;
    ArrayAdapter<String> adapter, his_adapter;
    List<String> list, listHistory;

    public static Dictionary getInstance() {
        return ourInstance;
    }

    private Dictionary() {
    }

    public Dictionary init(Context context){
        this.context = context;
//Init database
        dictSQL = SQLiteFactory.getInstance().setContext(context).getSQLiteHelper(DictSQLiteHelper.class);
        if (dictSQL.openDatabase("dict.db")){
            ((DictSQLiteHelper)dictSQL).createHistoryTable();
            Toast.makeText(context, "Ket noi thanh cong den CSDL", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Khong the ket noi den CSDL", Toast.LENGTH_SHORT).show();
        }
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
        ImageButton bvoidce = (ImageButton) rootView.findViewById(R.id.btn_voice);
        ImageButton bphoto = (ImageButton) rootView.findViewById(R.id.btn_photo);
        ListView lview = (ListView) rootView.findViewById(R.id.lv_history);
        setAutoView(auto);
        setBtnVoidceView(bvoidce);
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
        this.btn_voidce = btnVoidceView;
    }

    public void setBtnPhotoceView(ImageButton btnPhotoceView) {
        this.btn_voidce = btnPhotoceView;
    }

    public void setListView(ListView listView) {
        this.lv_history = listView;
    }
    public void initEvent(){
        listHistory = loadHistory();
        his_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_activated_1, listHistory);
        lv_history.setAdapter(his_adapter);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_activated_1, list);
        auto_search.setAdapter(adapter);
        auto_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = dictSQL.getLikeWord("data", auto_search.getText().toString(), 15);
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
                Cursor c = dictSQL.getOneRow("data", "word", txt.trim());
                c.moveToNext();
                showDialog(c);
                ContentValues cv = new ContentValues();
                cv.put("word", txt);
                cv.put("phonetic", c.getString(2));
                cv.put("summary", c.getString(3));
                cv.put("mean", c.getString(4));
                dictSQL.insert("history", cv);
                his_adapter.clear();
                his_adapter.addAll(loadHistory());
                his_adapter.notifyDataSetChanged();
            }
        });
    }

    private void showDialog(Cursor c) {
        DialogDict.getInstance().showDialog((Activity) context, "/"+c.getString(0)+"/", c.getString(1)+"\n"+c.getString(2)+"\n"+c.getString(3));
    }

    public List<String> loadHistory(){
        List<String> tmp = new ArrayList<>();
        Cursor c = ((DictSQLiteHelper)dictSQL).sqLiteDatabase.query("history", null, null, null, null, null, "id DESC");
        while (c.moveToNext()){
            tmp.add(c.getString(1));
        }
        c.close();
        return tmp;
    }
}
