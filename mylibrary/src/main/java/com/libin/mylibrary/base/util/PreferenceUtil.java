package com.libin.mylibrary.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.libin.mylibrary.base.MyApplication;


public class PreferenceUtil
{
    public static void write(String key, String value) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static void write(String key, int value) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static void write(String key, Boolean value) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static String readString(String key) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int readInt(String key) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static Boolean readBoolean(String key) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences(MyApplication.getAppContext().getApplicationInfo().packageName,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).commit();
    }


}
