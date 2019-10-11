package com.wstv.pad.watcher;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.wstv.pad.util.PatternUtils;


/**
 * <p>Description: </p>
 * RMBInputTextWatcher
 *
 * @author lilibin
 * @createDate 2017/9/28 12:55
 */

public class RMBInputTextWatcher implements TextWatcher {

    private WatcherCallback callback;

    public RMBInputTextWatcher(WatcherCallback callback){
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String temp = "";
        if (".".equals(s.toString())) {
            temp = "0.";
        } else if (s.length() > 1 && !s.toString().contains(".") && s.toString().indexOf("0") == 0) {
            temp = s.subSequence(1, s.length()).toString();
        } else if (!PatternUtils.isPositiveTwoDecimals(s.toString()) && s.length() > 0 && s.toString().lastIndexOf(".") != s.length() - 1 && s.toString().lastIndexOf(".0") != s.length() - 2) {
            temp = s.subSequence(0, s.length() - 1).toString();
        }
        if (!TextUtils.isEmpty(temp)) {
            s.replace(0, s.length(), temp);
        }

        if (callback != null) {
            callback.afterTextChange(s.toString());
        }
    }
}
