package apv.congnt.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by congn_000 on 10/29/2015.
 */
public class SquarButton extends Button {
    private int ratioWidth;
    private int ratioHeigh;

    public SquarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SquarButton,
                0, 0);
        try {
            ratioWidth = a.getInteger(R.styleable.SquarButton_ratioWidth, 1);
            ratioHeigh = a.getInteger(R.styleable.SquarButton_ratioHeigh, 1);
        } finally {
            a.recycle();
        }

    }


    //always match width
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int heigh = getMeasuredWidth();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        // set the dimensions
        size = widthWithoutPadding;
        int w = (size + getPaddingLeft() + getPaddingRight());
        int h = (size + getPaddingTop() + getPaddingBottom())* ratioHeigh / ratioWidth;
        setMeasuredDimension(w, h);
    }
}
