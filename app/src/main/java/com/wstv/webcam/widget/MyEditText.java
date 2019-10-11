package com.wstv.webcam.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.wstv.webcam.R;

/**
 * <p>Description: </p>
 * MyEditText
 *
 * @author lilibin
 * @createDate 2019/5/10 13:52
 */

public class MyEditText extends android.support.v7.widget.AppCompatEditText {

    public OnFocusChangeListener listener;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setBackgroundResource(hasFocus ? R.drawable.et_underline_selected : R.drawable.et_underline_unselected);
                if (null != listener) {
                    listener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        listener = l;
    }
}
