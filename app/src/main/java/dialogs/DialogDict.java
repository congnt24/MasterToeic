package dialogs;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ptpmcn.cong.mastertoeiclc.R;

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
     * @param activity: context of this
     * @param text1: Text Title
     * @param text2: Text below text1
     */
    public void showDialog(Activity activity, String text1, String text2){
        new MaterialDialog.Builder(activity)
                .title(text1)
                .content(text2)
                .iconRes(R.drawable.ic_action_play_circle_outline)
                .show();
    }
}
