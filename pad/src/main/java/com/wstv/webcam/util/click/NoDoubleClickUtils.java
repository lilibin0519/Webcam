package com.wstv.webcam.util.click;

import android.util.Log;

/**
 * 防止按钮2连续多次点击
 * Created by zhouxu on 2018/9/17.
 */

public class NoDoubleClickUtils {
    static final String TAG = NoDoubleClickUtils.class.getSimpleName();
    private final static int SPACE_TIME = 500;//2次点击的间隔时间，单位ms
    private static long lastClickTime;

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick;
        if (currentTime - lastClickTime > SPACE_TIME) {
            isClick = false;
        } else {
            isClick = true;
        }
        lastClickTime = currentTime;
        Log.e(TAG, "isClick : " + String.valueOf(isClick));
        return isClick;
    }

    public synchronized static boolean checkDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick;
        if (currentTime - lastClickTime > SPACE_TIME) {
            isClick = false;
        } else {
            isClick = true;
        }
        lastClickTime = currentTime;
        Log.e(TAG, "checkDoubleClick : " + String.valueOf(isClick));
        return isClick;
    }
}