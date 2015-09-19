package com.ptpmcn.cong.dbhandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by cong on 8/29/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "data.db";
    public static SQLiteDatabase sqLiteDatabase;
    Context context;

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
}
