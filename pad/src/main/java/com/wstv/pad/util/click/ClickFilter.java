package com.wstv.pad.util.click;

import android.view.View;

import java.lang.reflect.Field;

/**
 * <p>Description: </p>
 * ClickFilter 采用反射的处理方式，为点击事件添加代理（三方控件亦适用）
 *
 * @author lilibin
 * @createDate 2018/8/14 10:50
 */

public class ClickFilter {
    public static void setFilter(View view) {
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Class listInfoType = field.getType();
            Object listinfo = field.get(view);
            Field onclickField = listInfoType.getField("mOnClickListener");
            View.OnClickListener origin = (View.OnClickListener) onclickField.get(listinfo);
            onclickField.set(listinfo, new ClickProxy(origin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}