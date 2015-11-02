package com.ptpmcn.cong.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by cong on 10/31/2015.
 */
public abstract class BaseSQLiteHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "data.db";
    Context context;
    public BaseSQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }
    public abstract boolean openDatabase(String dbname);
    public abstract Cursor queryAll(String tableName);
    public abstract Cursor queryRandom(String tableName, int amount);
    public abstract void insert(String tableName, ContentValues values);
    public abstract List getLikeWord(String tableName, String word, int limit);
    public abstract Cursor getOneRow(String tableName,String column, String arg);

    public void copyDataBase(String dbname){
        InputStream myInput = null;
        OutputStream myOutput=null;
        try {
            myInput = context.getAssets().open(dbname);
            String outFileName = context.getFilesDir() + "/" + dbname;
            myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
