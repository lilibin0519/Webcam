package com.wstv.webcam.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.http.model.user.LoginWResult;
import com.wstv.webcam.http.model.user.UserResult;
import com.wstv.webcam.util.UserUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * <p>Description: </p>
 * WXEntryActivity
 *
 * @author lilibin
 * @createDate 2019/4/4 16:37
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, AppConstant.W_X_APP_ID,true);
        //将应用的appid注册到微信
        api.registerApp(AppConstant.W_X_APP_ID);
        Trace.d("------------------------------------");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result =  api.handleIntent(getIntent(), this);
            if(!result){
                Trace.d("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Trace.d("baseReq:"+ GsonUtils.gsonString(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Trace.d("baseResp:"+ GsonUtils.gsonString(baseResp));
        Trace.d("baseResp:"+baseResp.errStr+","+baseResp.openId+","+baseResp.transaction+","+baseResp.errCode);
        String result = "";
        switch(baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
//                showMsg(1,result);
                Trace.d("result :"+ result);
                String code = ((SendAuth.Resp) baseResp).code;
                requestOpenId(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
//                showMsg(2,result);
                Trace.d("result :"+ result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
//                showMsg(1,result);
                Trace.d("result :"+ result);
                showToastCenter("微信登录失败[openId request refuse]");
                finish();
                break;
            default:
                result = "发送返回";
//                showMsg(0,result);
                Trace.d("result :"+ result);
                finish();
                break;
        }
        PreferenceUtil.write(AppConstant.KEY_W_BIND, false);
    }

    private void requestOpenId(String code) {
        Trace.e("w code : " + code);
//        showToastCenter("code : " + code);
        if (PreferenceUtil.readBoolean(AppConstant.KEY_W_BIND)) {
            HttpService.bindW(this, code, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<EmptyResult>(this, this) {
                @Override
                public void onSuccess(EmptyResult result, int id) {
                    showToastCenter("微信绑定成功");
                    EventBus.getDefault().post(new EventCenter(2051));
                    finish();
                }
            });
        } else {
            HttpService.getAccessToken(this, code, new BaseCallback<LoginWResult>(this, this) {
                @Override
                public void onSuccess(LoginWResult result, int id) {
                    loginByW(result);
                }
            });
        }
    }

    private void loginByW(LoginWResult result) {
        HttpService.loginW(this, result.detail.access_token, result.detail.openid, new BaseCallback<UserResult>(this, this) {
            @Override
            public void onSuccess(UserResult userResult, int id) {
                getApp().setUserBean(userResult.detail);
                PreferenceUtil.write(AppConstant.KEY_PARAM_USER_ID, userResult.detail.userId);
                UserUtil.loginCam(WXEntryActivity.this);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewAndData() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
