package com.wstv.pad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     *作用是返回一个默认的值，如果MeasureSpec没有强制限制的话则使用提供的大小.否则在允许范围内可任意指定大小
     * 第一个参数size为提供的默认大小，第二个参数为测量的大小
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        // 默认高度，为了自动获取到focus
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = width;
//        // 这个之前是默认的拉伸图像
//        if (this.width > 0 && this.height > 0) {
//            width = this.width;
//            height = this.height;
//        }
//        setMeasuredDimension(width, height);
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }


    private int width,height;

    public void setMeasure(int width, int height) {
        this.width = width;
        this.height = height;
    }
}