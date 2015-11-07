package com.ptpmcn.cong.dictionary;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by cong on 10/28/2015.
 */
public class DialogDict {
    private static final DialogDict ourInstance = new DialogDict();

    public static DialogDict getInstance() {
        return ourInstance;
    }

    private DialogDict() {
    }

    /**
     * Show a material diaog using materiadialog library
     * @param activity: context of this
     */
    public void showDialog(Activity activity, final String title, String phonetic, String summary, String mean){
        /*new MaterialDialog.Builder(activity)
                .title(text1)
                .content(text2)
                .iconRes(android.R.drawable.ic_menu_compass)
                .show();*/
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dict_dialog);
        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.tv_phonetic)).setText(phonetic);
        ((TextView) dialog.findViewById(R.id.tv_summary)).setText(summary);
        ((TextView) dialog.findViewById(R.id.tv_mean)).setText(mean.replaceAll("&quot;", "\""));
        dialog.findViewById(R.id.ib_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(title);
            }
        });
        dialog.show();

    }

    private void playSound(String toSpeak) {
        Dictionary.getInstance().speech(toSpeak);
    }
}
