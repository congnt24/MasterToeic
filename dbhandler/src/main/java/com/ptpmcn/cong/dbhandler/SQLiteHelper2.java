package com.ptpmcn.cong.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.List;

/**
 * Created by cong on 8/29/2015.
 */
public class SQLiteHelper2 extends BaseSQLiteHelper {
    private SQLiteDatabase sqLiteDatabase;
    private static SQLiteHelper2 instance;

    public SQLiteHelper2(Context context) {
        super(context);
    }

    @Override
    public boolean openDatabase(String dbname) {
        File file = new File(context.getFilesDir()+"/"+dbname);
        if (!file.exists()) {
            //Try to get db from assets foder
            copyDataBase(dbname);
        }
        sqLiteDatabase = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);
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
    public List getLikeWord(String tableName, String word, int limit) {
//        return sqLiteDatabase.rawQuery("SELECT word FROM ? WHERE word LIKE \"?%\" LIMIT ?",new String[]{tableName, word, String.valueOf(limit)});
        return null;
    }

    @Override
    public Cursor getOneRow(String tableName, String column, String arg) {
        return null;
    }

    public static SQLiteHelper2 getInstance(Context context){
        if (instance == null){
            instance = new SQLiteHelper2(context);
        }
        return instance;
    }
}
