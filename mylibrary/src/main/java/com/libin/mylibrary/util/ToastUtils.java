package com.libin.mylibrary.util;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.widget.Toast;

import com.libin.mylibrary.base.MyApplication;


public class ToastUtils {

    private static Toast sToast = null;

    @SuppressLint("ShowToast")
    private static Toast setToast(String content, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getAppContext(), content, duration);
        } else {
            sToast.setText(content);
            sToast.setDuration(duration);
        }
        return sToast;
    }

    public static void showToastLong(String content) {
        setToast(content, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(String content) {
        setToast(content, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(int textId) {
        showToastShort(MyApplication.getAppContext().getString(textId));
    }

    public static void showToastLong(int textId) {
        showToastLong(MyApplication.getAppContext().getString(textId));
    }
    
    public static void showToastGravityCenter(String content){
        setToastGravity(content, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0).show();
    }
    
    @SuppressLint("ShowToast")
    private static Toast setToastGravity(String content, int duration,int position,int xOffset,int yOffset){
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getAppContext(), content, duration);
        } else {
            sToast.setText(content);
            sToast.setDuration(duration);
        }
        sToast.setGravity(position, xOffset, yOffset);
        return sToast;
    }
    
}
