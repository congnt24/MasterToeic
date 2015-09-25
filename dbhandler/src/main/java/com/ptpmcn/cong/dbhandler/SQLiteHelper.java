package com.ptpmcn.cong.dbhandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.Random;

/**
 * Created by cong on 8/29/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "data.db";
    public SQLiteDatabase sqLiteDatabase;
    private  static SQLiteHelper instance;
    Context context;

    public static SQLiteHelper getInstance(Context context){
        if (instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }
    public boolean openDatabase(String dbname){
        File file = new File(context.getFilesDir()+"/"+dbname);
        if (file.exists()) {
            sqLiteDatabase = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor queryAll(String tableName){
        return sqLiteDatabase.query(tableName, null, null, null, null, null, null);
    }
    public Cursor queryRandom(String tableName, int amount){
        return sqLiteDatabase.query(tableName, null, null, null, null, null, "RANDOM()", String.valueOf(amount));
    }
}
