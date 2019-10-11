package com.wstv.webcam.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.activity.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, AppConstant.W_X_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected int getContentViewLayoutID() {
		return /*R.layout.pay_result*/0;
	}

	@Override
	protected void getBundleExtras(Bundle extras) {

	}

	@Override
	protected void initViewAndData() {
		setTitleStr("支付结果");
	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	protected void onEventComming(EventCenter center) {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == -1) {
				showToastCenter("支付等待中，请查询微信账单");
			} else if (resp.errCode == 0) {
				showToastCenter("支付成功");
				EventBus.getDefault().post(new EventCenter(2047));
			}
			finish();
		}
	}
}