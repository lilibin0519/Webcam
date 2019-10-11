package com.wstv.webcam.widget;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.util.click.ClickProxy;

/**
 * <p>Description: </p>
 * ManageUserPop
 *
 * @author lilibin
 * @createDate 2019/7/16 16:06
 */

public class ManageUserPop extends PopupWindow {

    private final String showId;

    private String userId;

    private BaseActivity activity;

    public ManageUserPop(BaseActivity activity, String userId, String showId){
        this.activity = activity;
        this.userId = userId;
        this.showId = showId;
        View contentView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_manager, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);//设置Pop可点击
        this.setOutsideTouchable(true);
        this.update();//刷新状态
        this.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), (Bitmap) null));
        setAnimationStyle(R.style.BottomDialog_Animation);
        contentView.findViewById(R.id.close).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }));
        contentView.findViewById(R.id.black).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutUpOrBlack("0");
            }
        }));
        contentView.findViewById(R.id.shut_up).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutUpOrBlack("1");
            }
        }));
    }

    private void shutUpOrBlack(String hours){
        HttpService.shutUpOrBlack(activity, userId, showId, hours, new BaseCallback<EmptyResult>(activity, activity) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                activity.showToastCenter("设置完成");
                dismiss();
            }
        });
    }
}
