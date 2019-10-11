package com.wstv.pad.activity.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.libin.mylibrary.base.activity.BaseAppCompatActivity;
import com.libin.mylibrary.base.net.NetUtils;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.OkHttpUtils;
import com.wstv.pad.R;
import com.wstv.pad.WsApp;
import com.wstv.pad.util.LoadingInterface;

/**
 * <p>Description: </p>
 * BaseActivity
 *
 * @author lilibin
 * @createDate 2018/12/8 11:58
 */

public abstract class BaseActivity extends BaseAppCompatActivity implements LoadingInterface {

    private MaterialDialog loadingMaterialDialog;

    protected TextView title;

    protected LinearLayout left;

    protected LinearLayout right;

    protected TextView leftText;

    protected TextView rightText;

    protected ImageView leftImage;

    protected ImageView rightImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).statusBarDarkFont(statusBarDarkFount(), 0.2f).statusBarColor(getStatusBarColor()).init();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        title = findViewById(R.id.title_bar_title);
        if (null != title){
            left = findViewById(R.id.title_bar_left);
            right = findViewById(R.id.title_bar_right);
            rightText = findViewById(R.id.title_bar_right_text);
            leftText = findViewById(R.id.title_bar_left_text);
            rightImage = findViewById(R.id.title_bar_right_image);
            leftImage = findViewById(R.id.title_bar_left_image);
            if (null != left) {
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLeftClick(v);
                    }
                });
            }

            if (null != right) {
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRightClick(v);
                    }
                });
            }
        }
    }

    public void onLeftClick(View v){
        hideKeyboard();
        finish();
    }

    public void onRightClick(View v){

    }

    protected void setTitleStr(String text){
        if (null != title) {
            title.setText(text);
        }
    }

    public String getTitleText(){
        if (null != title) {
            return title.getText().toString();
        } else {
            return "";
        }
    }

    protected void setLeftTextStr(String text){
        leftText.setText(text);
        left.setVisibility(View.VISIBLE);
        leftImage.setVisibility(View.GONE);
        leftText.setVisibility(View.VISIBLE);
    }

    protected void setRightTextStr(String text){
        rightText.setText(text);
        right.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.GONE);
        rightText.setVisibility(View.VISIBLE);
    }

    protected void setLeftImageRes(@DrawableRes int res){
        leftImage.setImageResource(res);
        left.setVisibility(View.VISIBLE);
        leftImage.setVisibility(View.VISIBLE);
        leftText.setVisibility(View.GONE);
    }

    protected void setRightImageRes(@DrawableRes int res){
        rightImage.setImageResource(res);
        right.setVisibility(View.VISIBLE);
        rightImage.setVisibility(View.VISIBLE);
        rightText.setVisibility(View.GONE);
    }

    protected int getStatusBarColor(){
        return R.color.colorPrimary;
    }

    protected boolean statusBarDarkFount(){
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImmersionBar.with(this).init();
    }


    protected boolean checkNetStatus() {
        if (!NetUtils.isNetworkConnected(this)) {
            showNetErrorDialog();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showLoadingDialog() {
        getLoadingDialog();
        if (!loadingMaterialDialog.isShowing()) {
            loadingMaterialDialog.show();
        }
    }

    @Override
    public MaterialDialog getLoadingDialog(){
        if (null == loadingMaterialDialog) {
            loadingMaterialDialog = new MaterialDialog.Builder(this)
                    .content(R.string.please_wait)
                    .contentGravity(GravityEnum.START)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(false).build();
//            loadingMaterialDialog.getProgressBar().setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_progress));
        }
        return loadingMaterialDialog;
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingMaterialDialog != null && loadingMaterialDialog.isShowing())
            loadingMaterialDialog.dismiss();
    }

    protected void showNetErrorDialog() {
        new MaterialDialog.Builder(this)
                .content("当前无网络连接，是否设置？")
                .cancelable(false)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Intent intent = null;
                        // 先判断当前系统版本
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {  // 3.0以上
                            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                        }
                        startActivity(intent);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if(NoDoubleClickUtils.checkDoubleClick()){
//                return true;
//            }
            // get current focus,Generally it is EditText
            View view = getCurrentFocus();
            if (isShouldHideSoftKeyBoard(view, ev)) {
                hideSoftKeyBoard(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top +view.getHeight(), right = left
                    + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // If click the EditText event ,ignore it
                return false;
            } else {
                return true;
            }
        }
        // if the focus is EditText,ignore it;
        return false;
    }

    protected void hideSoftKeyBoard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != im)
                im.hideSoftInputFromWindow(token,
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public WsApp getApp() {
        return (WsApp) getApplication();
    }

    @Override
    protected void onPause() {
        Trace.e("onPause");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        onLeftClick(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            onLeftClick(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
