package com.wstv.pad.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseRecyclerActivity;
import com.wstv.pad.holder.cash.CashHeader;
import com.wstv.pad.holder.cash.CashViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.account.AccountLog;
import com.wstv.pad.http.model.account.AccountLogResult;
import com.wstv.pad.http.model.user.UserResult;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/25.
 */

public class CashActivity extends BaseRecyclerActivity<AccountLog> implements OnToolsItemClickListener<AccountLog> {

    private CashHeader header;

    private int currentPage = 1;

    @Override
    protected String getTitleStr() {
        return "余额";
    }

    @Override
    public DefaultAdapterViewListener<AccountLog> getViewListener() {
        return new CashViewListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_cash;
    }

    @Override
    public void initView() {
        setRightTextStr("充值");
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_cash, content.getRecyclerView(), false);
        header = new CashHeader(this, headerView, getApp().getUserBean().coins);
        adapter.addHead(header);
        content.setRefreshEnable(true);
        content.setRefreshing(true);
    }

    private void requestLog(){
        HttpService.accountLog(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), currentPage, AppConstant.DEFAULT_PAGE_SIZE,
                new BaseCallback<AccountLogResult>(this, this) {
                    @Override
                    public void onSuccess(AccountLogResult accountLogResult, int id) {
                        if (currentPage == 1) {
                            data.clear();
                        }
                        content.setLoadMoreEnable(null != accountLogResult.detail && accountLogResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        content.setHasBottom(null != accountLogResult.detail && accountLogResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        data.addAll(accountLogResult.detail);
                        adapter.notifyDataSetChanged();
                        content.complete();
                    }

                    @Override
                    protected void onFailed(String errCode, String errMsg, AccountLogResult accountLogResult) {
                        content.complete();
                    }
                });
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        readyGo(RechargeActivity.class);
    }

    @Override
    public void onItemClick(int position, AccountLog item) {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComming(EventCenter center) {
        if (center.getEventCode() == 2047) {
            requestUserInfo();
        }
    }

    private void requestUserInfo(){
        HttpService.getUserInfo(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<UserResult>(this, this) {
            @Override
            public void onSuccess(UserResult userResult, int id) {
                getApp().setUserBean(userResult.detail);
                header.setAccount(userResult.detail.coins);
                requestLog();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, UserResult userResult) {
                content.complete();
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        requestUserInfo();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestLog();
    }

//    private void requestPay(OrderBean data) {
//        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//        // 将该app注册到微信
//        msgApi.registerApp(data.appid);
//
//        PayReq request = new PayReq();
//        request.appId = data.appid;
//        request.partnerId = data.partnerid;
//        request.prepayId = data.prepayid;
//        prepayId = data.prepayid;
//        request.packageValue = data.mpackage;
//        request.nonceStr = data.noncestr;
//        request.timeStamp = data.timestamp;
//        request.sign = data.sign;
//        msgApi.sendReq(request);
//    }
}
