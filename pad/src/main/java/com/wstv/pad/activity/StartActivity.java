package com.wstv.pad.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.util.UserUtil;
import com.wstv.pad.util.click.ClickProxy;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kindred on 2019/3/10.
 */

public class StartActivity extends BaseActivity {

    @Bind(R.id.activity_start_login_phone)
    LinearLayout loginPhone;

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    @Override
    protected void initViewAndData() {
        if (getApp().isLogin()) {
            showLoadingDialog();
            UserUtil.loginCam(this);
            return;
        }
        loginPhone.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(LoginPhoneActivity.class);
            }
        }));
        ButterKnife.findById(this, R.id.activity_start_login_w).setVisibility(isWeixinAvilible(this) ? View.VISIBLE : View.GONE);
        ButterKnife.findById(this, R.id.activity_start_login_w).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthReq();
            }
        }));
        regToWx();
    }

    private void AuthReq(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
        req.state = "wechat_sdk_微信登录";
        api.sendReq(req);
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, AppConstant.W_X_APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(AppConstant.W_X_APP_ID);
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_start;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.transparent;
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
}
