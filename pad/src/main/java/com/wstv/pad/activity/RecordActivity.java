package com.wstv.pad.activity;

import android.os.Bundle;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseRecyclerActivity;
import com.wstv.pad.holder.record.WatchRecordViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.record.WatchRecord;
import com.wstv.pad.http.model.record.WatchRecordResult;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/15.
 */

public class RecordActivity extends BaseRecyclerActivity<WatchRecord> implements OnToolsItemClickListener<WatchRecord> {

    private int currentPage = 1;

    @Override
    protected String getTitleStr() {
        return "我看过的";
    }

    @Override
    public DefaultAdapterViewListener<WatchRecord> getViewListener() {
        return new WatchRecordViewListener();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_watch_record;
    }

    @Override
    public void onItemClick(int position, WatchRecord item) {

    }

    @Override
    protected void initList() {
        super.initList();
        content.setHasBottom(true);
        content.setLoadMoreEnable(true);
        content.setRefreshEnable(true);
    }

    @Override
    public void initView() {
//        setRightTextStr("清空");
        requestData();
    }

    private void requestData() {
        HttpService.watchRecord(this, String.valueOf(currentPage), String.valueOf(AppConstant.DEFAULT_PAGE_SIZE), PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<WatchRecordResult>(this, this) {
                    @Override
                    public void onSuccess(WatchRecordResult watchRecordResult, int id) {
                        content.setHasBottom(null != watchRecordResult.detail && watchRecordResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        content.setLoadMoreEnable(null != watchRecordResult.detail && watchRecordResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        data.addAll(watchRecordResult.detail);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }
}
