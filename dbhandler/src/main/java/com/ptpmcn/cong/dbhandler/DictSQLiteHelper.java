package com.ptpmcn.cong.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cong on 10/31/2015.
 */
public class DictSQLiteHelper extends BaseSQLiteHelper{
    public SQLiteDatabase sqLiteDatabase;
    private static DictSQLiteHelper instance;
    public DictSQLiteHelper(Context context) {
        super(context);
    }
    @Override
    public boolean openDatabase(String dbname) {
        File file = new File(context.getFilesDir()+"/"+dbname);
        if (!file.exists()) {
            //Try to get db from assets foder
            copyDataBase(dbname);
        }
        sqLiteDatabase = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        return sqLiteDatabase.isOpen();
    }

    @Override
    public Cursor queryAll(String tableName) {
        return sqLiteDatabase.query(tableName, null, null, null, null, null, null);
    }

    @Override
    public Cursor queryRandom(String tableName, int amount) {
        return sqLiteDatabase.query(tableName, null, null, null, null, null, "RANDOM()", String.valueOf(amount));
    }

    @Override
    public void insert(String tableName, ContentValues values) {
        sqLiteDatabase.insert(tableName, null, values);
    }

    @Override
    public List<String> getLikeWord(String tableName, String word, int limit) {
        Cursor c= sqLiteDatabase.rawQuery("SELECT word AS _id, word FROM "+tableName+" WHERE word LIKE \""+word+"%\" LIMIT ?", new String[]{ String.valueOf(limit)});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()){
            list.add(c.getString(c.getColumnIndex("word")));
        }
        c.close();
        return list;
    }

    @Override
    public Cursor getOneRow(String tableName, String column, String arg) {
        return sqLiteDatabase.query(tableName, null, column+"=?", arg==null?null:new String[]{arg}, null, null, null);
    }


    public void createHistoryTable(){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "word TEXT NOT NULL UNIQUE, phonetic TEXT, summary TEXT, mean TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    };

    public static DictSQLiteHelper getInstance(Context context){
        if (instance == null){
            instance = new DictSQLiteHelper(context);
        }
        return instance;
    }
}
