package apv.congnt.customview;

/**
 * Created by congn_000 on 10/29/2015.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnswerView extends LinearLayout implements View.OnClickListener {

    private boolean isDisable = false;

    public interface OnAnswerChange {
        void onAnswerChange(AnswerView view, int index);
    }

    public boolean aw_ShowTextWhenActive;
    boolean aw_changeOnClick, aw_canCancelAnswer;
    TextView numberTextView;
    OneAnswerView viewler[];
    OnAnswerChange change;

    public AnswerView(Context context) {
        super(context);
        init(context, null);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setOnAnswerChange(OnAnswerChange event) {
        change = event;
    }

    protected void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AnswerView, 0, 0);

            aw_ShowTextWhenActive = a.getBoolean(R.styleable.AnswerView_aw_ShowTextWhenActive, false);
            aw_changeOnClick = a.getBoolean(R.styleable.AnswerView_aw_changeOnClick, true);
            aw_canCancelAnswer = a.getBoolean(R.styleable.AnswerView_aw_canCancelAnswer, true);

            int count = a.getInt(R.styleable.AnswerView_aw_NumberOfAnswers, 5);

            if(a.getBoolean(R.styleable.AnswerView_aw_ShowNumber, false)) {
                numberTextView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.answer_number, this, false);
                setNumber(a.getInt(R.styleable.AnswerView_aw_Number, 1));
                addView(numberTextView);
            }

            a.recycle();

            viewler = new OneAnswerView[count];

            for(int i = 0 ; i < count; ++i) {
                viewler[i] = (OneAnswerView)LayoutInflater.from(getContext()).inflate(R.layout.answer_one_view, this, false);
                viewler[i].setIndex(i);
                viewler[i].setOnClickListener(this);
                addView(viewler[i]);
            }
        }
    }

    public void resize(int count) {
        int index = getActiveIndex();

        removeAllViews();

        if(numberTextView != null) {
            addView(numberTextView);
        }

        for(OneAnswerView oav : viewler) {
            oav.setOnClickListener(null);
        }

        viewler = new OneAnswerView[count];
        for(int i = 0 ; i < count; ++i) {
            viewler[i] = (OneAnswerView)LayoutInflater.from(getContext()).inflate(R.layout.answer_one_view, this, false);
            viewler[i].setIndex(i);
            viewler[i].setOnClickListener(this);
            viewler[i].setActive(index == i, this);
            addView(viewler[i]);
        }
    }

    public void setNumber(int number) {
        if(numberTextView != null) {
            numberTextView.setText(Integer.toString(number));
        } else {
            // throw exception maybe?
        }
    }

    public void setActiveChar(char index) {
        setActiveIndex(index - 'A');
    }

    public char getActiveChar() {
        return (char)((int)'A' + (int)getActiveIndex());
    }

    public void setActiveIndex(int index) {
        for(int i = 0 ; i < viewler.length; ++i) {
            viewler[i].setActive(index == i, this);
        }
    }

    public int getActiveIndex() {
        for(int i = 0 ; i < getChildCount(); ++i) {
            if(viewler[i].active)
                return i;
        }
        return -1;
    }
    public void clearAll(){
        for(int i = 0 ; i < viewler.length; ++i) {
            viewler[i].clear(false, this);
        }
    }
    public void setTrueAnswer(int index){
        for(int i = 0 ; i < viewler.length; ++i) {
            if (index == i){
                viewler[i].setTrueAnswer();
            }
        }
    }
    public void disableAll(){
        isDisable = true;
    }
    public void enableAll(){
        isDisable = false;
    }

    @Override
    public void onClick(View v) {
        if (!isDisable)
        for(int i = 0 ; i < viewler.length; ++i) {
            if(viewler[i] == v) {
                if(aw_changeOnClick) {
                    ((OneAnswerView)v).setActive(!aw_canCancelAnswer || !((OneAnswerView) v).active, this);
                }
                if(change != null) {
                    change.onAnswerChange(this, ((OneAnswerView)v).active ? i : -1);
                }
            } else if(aw_changeOnClick) {
                viewler[i].setActive(false, this);
            }
        }
    }
}