package apv.congnt.customview;

/**
 * Created by congn_000 on 10/29/2015.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OneAnswerView extends LinearLayout {

    TextView choose;
    public OneAnswerView(Context context) {
        super(context);
        init();
    }

    public OneAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OneAnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OneAnswerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public boolean active;

    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.answer_view, this, true);
        choose = (TextView)findViewById(R.id.chooice);
    }

    public void setActive(boolean active, AnswerView aw) {
        if(active != this.active) {
            this.active = active;
            if(active) {
                if(aw.aw_ShowTextWhenActive) {
                    ((TextView)findViewById(R.id.chooice)).setTextColor(getResources().getColor(R.color.aw_cevap_text_sel));
                } else {
                    findViewById(R.id.chooice).setVisibility(INVISIBLE);
                }

                getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_circle_ac));
            } else {
                if(aw.aw_ShowTextWhenActive) {
                    ((TextView)findViewById(R.id.chooice)).setTextColor(getResources().getColor(R.color.aw_cevap_text));
                } else {
                    findViewById(R.id.chooice).setVisibility(VISIBLE);
                }

                getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_circle));
            }
        }
    }
    public void clear(boolean active, AnswerView aw) {
        this.active = active;
        if(aw.aw_ShowTextWhenActive) {
            ((TextView)findViewById(R.id.chooice)).setTextColor(getResources().getColor(R.color.aw_cevap_text));
        } else {
            findViewById(R.id.chooice).setVisibility(VISIBLE);
        }
        getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_circle));
        choose.setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_circle));
    }
    public void setTrueAnswer(){
        choose.setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_circle_true));
        Drawable drawable = getResources().getDrawable(R.drawable.ic_action_check_circle);
        int primaryColor = getResources().getColor(R.color.Lime);
        drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        getChildAt(0).setBackgroundDrawable(drawable);
        choose.setTextColor(getResources().getColor(android.R.color.transparent));
    }

    public void setIndex(int index) {
        ((TextView) findViewById(R.id.chooice)).setText(String.valueOf((char) (((int) 'A') + index)));
    }
}