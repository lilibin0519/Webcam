package com.wstv.webcam.activity;

import android.os.Bundle;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseRecyclerActivity;
import com.wstv.webcam.holder.follow.FollowViewListener;
import com.wstv.webcam.http.model.follow.Follow;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/3/12.
 */

public class FollowListActivity extends BaseRecyclerActivity<Follow> {

    @Override
    protected String getTitleStr() {
        return "关注";
    }

    @Override
    public DefaultAdapterViewListener<Follow> getViewListener() {
        return new FollowViewListener();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_follow;
    }

    @Override
    public void initView() {
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
