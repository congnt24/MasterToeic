package com.ptpmcn.cong.dictionary;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by cong on 10/28/2015.
 */
public class DialogDict {
    private static DialogDict ourInstance = new DialogDict();

    public static DialogDict getInstance() {
        return ourInstance;
    }

    private DialogDict() {
    }

    /**
     * Show a material diaog using materiadialog library
     * @param context: context of this
     * @param text1: Text Title
     * @param text2: Text below text1
     */
    public void showDialog(Activity context, String text1, String text2){
        new MaterialDialog.Builder(context)
                .title(text1)
                .content(text2)
                .iconRes(android.R.drawable.ic_menu_compass)
                .show();
    }
}
