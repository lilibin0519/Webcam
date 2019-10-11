package com.wstv.pad.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.callback.LoginCallback;
import com.wstv.pad.http.model.EmptyResult;
import com.wstv.pad.util.DownloadReceiver;
import com.wstv.pad.util.UpdateUtil;
import com.wstv.pad.util.UserUtil;
import com.wstv.pad.util.click.ClickProxy;

import butterknife.Bind;
import okhttp3.Response;

/**
 * Created by Kindred on 2019/3/10.
 */

public class LoginPhoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.activity_login_phone_submit)
    Button login;

    @Bind(R.id.activity_login_phone_number)
    EditText phoneNumber;

    @Bind(R.id.activity_login_phone_code)
    EditText code;

    private DownloadReceiver receiver;

    public AlertDialog dialog;

    @Override
    protected void initViewAndData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        login.setOnClickListener(new ClickProxy(LoginPhoneActivity.this));
        requestUpdate();
    }

    private void requestUpdate(){
        HttpService.requestUpdate(this, getVersionCode(this), new BaseCallback<EmptyResult<String>>(this, false, this) {

            @Override
            public EmptyResult<String> parseNetworkResponse(Response response, int id) throws Exception {
                return super.parseNetworkResponse(response, id);
            }

            @Override
            public void onSuccess(EmptyResult<String> emptyResult, int id) {
                if (getApp().isLogin()) {
                    showLoadingDialog();
                    UserUtil.loginCam(LoginPhoneActivity.this);
                    return;
                }
            }

            @Override
            protected void onFailed(String errCode, String errMsg, EmptyResult<String> emptyResult) {
                if ("1016".equals(errCode)) {
                    initDownload(emptyResult.detail);
                } else {
                    showToastCenter("网络请求失败");
                }
            }
        });
    }

    private void initDownload(String url){
        receiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        intentFilter.addAction("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
        registerReceiver(receiver, intentFilter);
        UpdateUtil dm = new UpdateUtil(this);
        if (dm.checkDownloadManagerEnable()) {
            dialog = new AlertDialog.Builder(this)/*.setCancelable(false)*/.setMessage("万秀直播更新中，请稍后...").show();
            if (DownloadReceiver.downloadId != 0L) {
                dm.clearCurrentTask(DownloadReceiver.downloadId); // 先清空之前的下载
            }
            DownloadReceiver.downloadId = dm.download(url, "万秀更新", "万秀更新");
        }else{
            showToastCenter("请开启下载管理器");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_phone_submit:
                toLogin();
                break;
        }
    }

    private void sendSMS() {
        if (TextUtils.isEmpty(phoneNumber.getText().toString()) || !phoneNumber.getText().toString().startsWith("1") || phoneNumber.getText().toString().length() != 11) {
            showToastCenter("手机号不正确");
            return;
        }
        String timeStamp = PreferenceUtil.readString(AppConstant.SEND_SMS_TIME_STAMP);
        long time = TextUtils.isEmpty(timeStamp) ? 0 : Long.parseLong(timeStamp);
        if (System.currentTimeMillis() - time < 60 * 1000) {
            showToastCenter("请勿频繁发送验证码，剩余" + (time / 1000) + "s");
            return;
        }
        HttpService.sendSMS(this, phoneNumber.getText().toString(), new BaseCallback<EmptyResult>(this, false, this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                showToastCenter("验证码已发送");
                PreferenceUtil.write(AppConstant.SEND_SMS_TIME_STAMP, String.valueOf(System.currentTimeMillis()));
            }

            @Override
            protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
                showToastCenter(TextUtils.isEmpty(errMsg) ? "验证码发送失败" : errMsg);
            }
        });
    }

    private void toLogin() {
        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            showToastCenter("请输入包房号");
            return;
        }
        if (TextUtils.isEmpty(code.getText().toString())) {
            showToastCenter("请输入密码");
            return;
        }
        HttpService.loginOrRegister(LoginPhoneActivity.this, phoneNumber.getText().toString(), code.getText().toString(), new LoginCallback(LoginPhoneActivity.this));
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver){
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login_phone;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }
}
