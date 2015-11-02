package com.ptpmcn.cong.dbhandler;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cong on 10/31/2015.
 */
public class SQLiteFactory {
    private static SQLiteFactory instance;
    private Context context;
    private SQLiteFactory() {
    }
    public static SQLiteFactory getInstance(){
        if (instance==null)
            instance = new SQLiteFactory();
        return instance;
    }
    public SQLiteFactory setContext(Context context){
        this.context = context;
        return this;
    }
    public BaseSQLiteHelper getSQLiteHelper(Class<? extends BaseSQLiteHelper> helperClass) {
        try {
            Method method = helperClass.getMethod("getInstance", Context.class);
            if (method != null) {
                return (BaseSQLiteHelper) method.invoke(helperClass, context);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
