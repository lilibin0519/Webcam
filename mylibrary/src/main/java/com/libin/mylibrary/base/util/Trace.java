package com.libin.mylibrary.base.util;

import android.util.Log;

/**
 * 作者：王鹏飞
 * 创建时间：2015/12/30 11:15
 * 邮箱：15291967179@163.com
 * 描述：  Log 管理类
 */
public class Trace
{
    private static final boolean isShowLog = true;

    private static final String TAG = "everyenjoy.trace";

    public static void i(String msg)
    {
        i(TAG, msg);
    }

    public static void i(String tag, String msg)
    {
        if (isShowLog)
        {
            Log.i(tag, msg);
        }
    }

    public static void d(String msg)
    {
        d(TAG, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isShowLog)
        {
            Log.d(tag, msg);
        }
    }


    public static void e(String msg)
    {
        e(TAG, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isShowLog)
        {
            Log.e(tag, msg);
        }
    }

    public static void w(String msg)
    {
        w(TAG, msg);
    }

    public static void w(String tag, String msg)
    {
        if (isShowLog)
        {
            Log.w(tag, msg);
        }
    }
    
    public static void v(String msg)
    {
        v(TAG, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isShowLog)
        {
            Log.v(tag, msg);
        }
    }
    
}
