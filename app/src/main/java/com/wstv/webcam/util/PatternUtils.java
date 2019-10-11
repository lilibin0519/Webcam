package com.wstv.webcam.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create date: 2017/9/8.
 * Create time: 21:50
 * ${FILE_NAME}
 *
 * @author lilibin
 * @version 1.0
 */
public class PatternUtils {

    public static boolean isMobile(String phoneNumber){
        String regex = "^1\\d{10}$";
        return patternByRegex(phoneNumber, regex);
    }

    public static boolean isDecimals(String str){
        String regex = "^(0|[1-9]+[0-9]*)([.][0-9]+)*$";
        return patternByRegex(str, regex);
    }

    public static boolean isPositiveTwoDecimals(String str){
        String regex = "^(0|[1-9]+[0-9]*)([.](0[1-9]{1}|([1-9]{1}[1-9]{0,1})))*$";
        return patternByRegex(str, regex);
    }

    public static boolean patternByRegex(String str, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return !TextUtils.isEmpty(str) && matcher.matches();
    }
}
